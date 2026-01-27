package Adventure_generator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test 
    public void testRegisterUser_Success() throws Exception{
        String json = "{ \"email\": \"autotest1@example.com\", \"password\": \"pass1234\", \"confirmPassword\": \"pass1234\", \"userName\": \"AutoTest1\" }";
        mockMvc.perform(post("/api/auth/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful!"))
                .andExpect(jsonPath("$.userData.email").value("autotest1@example.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testRegisterUser_PasswordMismatch() throws Exception {
        String json = "{ \"email\": \"autotest2@example.com\", \"password\": \"pass1234\", \"confirmPassword\": \"w12313\", \"userName\": \"AutoTest2\" }";
        mockMvc.perform(post("/api/auth/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
