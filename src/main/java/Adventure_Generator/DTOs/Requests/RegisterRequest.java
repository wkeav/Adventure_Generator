package Adventure_generator.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/*
 * This is what client can send. 
 */
public class RegisterRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    @Size(min = 3)
    private String userName;

    // Empty constructor 
    public RegisterRequest(){
    }

    public RegisterRequest(String email, String password, String confirmPassword, String userName) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.userName = userName;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isPasswordMatching(){
        return password != null && password.equals(confirmPassword);
    }
    


}
