package Adventure_generator.Util;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class EmailVerificationUtil {
    public String generateVerificationToken(){
        return UUID.randomUUID().toString(); // Generate simple random token
    }
    public LocalDateTime getTokenExpire(){
        return LocalDateTime.now().plusHours(24); // Expiration date is 24 hrs 
    }
    public boolean isTokenExpired(LocalDateTime tokenExpire){
        return LocalDateTime.now().isAfter(tokenExpire);
    }
}
