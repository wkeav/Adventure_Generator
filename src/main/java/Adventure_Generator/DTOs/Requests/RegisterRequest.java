package Adventure_generator.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration endpoint.
 * 
 * Contains new user account data with validation constraints:
 * - Email (validated format, must be unique)
 * - Password (min 6 characters)
 * - Password confirmation (must match password)
 * - Username (min 3 characters, must be unique)
 * 
 * Used by POST /api/auth/register
 */
public class RegisterRequest {
    
    @Email
    @NotNull
    private String email;

    /** User's password (minimum 6 characters, will be hashed) */
    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    @Size(min = 3)
    private String userName;

    // CONSTRUCTOR
    public RegisterRequest(){
    }

    /**
     * Constructs a RegisterRequest with all fields.
     * @param email User's email address
     * @param password User's password
     * @param confirmPassword Password confirmation
     * @param userName User's display name
     */
    public RegisterRequest(String email, String password, String confirmPassword, String userName) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.userName = userName;
    }

    // Getters & Setters
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

    /**
     * Validates that password and confirmPassword match.
     * @return true if passwords match, false otherwise
     */
    public boolean isPasswordMatching(){
        return password != null && password.equals(confirmPassword);
    }
}
