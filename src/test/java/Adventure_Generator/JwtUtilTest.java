package Adventure_generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Adventure_generator.DTOs.Response.UserData;
import Adventure_generator.Util.JwtUtil;

@SpringBootTest(classes = Adventure_generator.AdventureGeneratorApplication.class)
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndParseToken(){
        UserData userData = new UserData(1,"test@test.com", "Test", LocalDateTime.now()); 
        String token = jwtUtil.generateToken(userData);

        assertNotNull(token); // Checks to ensure token isn't null
        assertFalse(token.isEmpty()); 

        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("Test", username);
        
        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    public void testExpiredToken() throws InterruptedException{
        UserData userData = new UserData(1,"test@test.com", "Test", LocalDateTime.now()); 
        String token = jwtUtil.generateTokenWithCustomExpiry(userData, 1); // 1 second 

        Thread.sleep(2000); // for token to be expired
        assertFalse(jwtUtil.validateToken(token, userData.getUserName()));
    }

    @Test
    public void testTokenForWrongUser(){
        UserData userData1 = new UserData(1,"test1@test.com", "Test1", LocalDateTime.now());
        UserData userData2 = new UserData(2,"test2@test.com", "Test2", LocalDateTime.now());
        String token = jwtUtil.generateToken(userData1);

        assertFalse(jwtUtil.validateToken(token, userData2.getUserName()));
    }

    @Test
    public void testInvalidToken() {
        UserData userData = new UserData(1,"test@test.com", "Test", LocalDateTime.now());
        String fakeToken = "not.a.jwt";
        assertFalse(jwtUtil.validateToken(fakeToken, userData.getUserName()));
    }

}
