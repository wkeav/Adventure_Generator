package Adventure_Generator.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Adventure_Generator.DTOs.Response.UserData;


@Component
public class JwtUtil {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // when its expired

    @Value("${jwt.secret}")
    private String secret;

    
    public String generateToken(UserData userData){

    };
    private String doGenerateToken(){};
    public Boolean validateToken(String token, UserData userData){};
}
