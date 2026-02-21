package Adventure_generator.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for user login endpoint.
 * 
 * Contains credentials for authentication:
 * - Email (validated format)
 * - Password (plain text, validated server-side)
 * 
 * Used by POST /api/auth/login
 */
public class LoginRequest {
    
    @Email
    @NotNull
    private String email;

    /** User's password (transmitted as plain text over HTTPS) */
    @NotNull
    private String password;
    
    // CONSTRUCTOR
    public LoginRequest(){
    }

    /**
     * Constructs a LoginRequest with email and password.
     * @param email The user's email
     * @param password The user's password
     */
    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
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
}
