package Adventure_generator.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
/*
 * This is what client can send 
 */
public class LoginRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
    
    // Empty constructor
    public LoginRequest(){

    }

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
}
