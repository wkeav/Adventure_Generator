package Adventure_generator.DTOs.Response;

import java.time.LocalDateTime;

/**
 * Response DTO containing user account information.
 * 
 * Used in authentication responses to return non-sensitive user data.
 * Does NOT include the password hash for security.
 * 
 * Included in:
 * - AuthResponse (after successful login/registration)
 * - User profile endpoints
 */
public class UserData {
    
    private long userId;
    private String email;
    private String userName;

    private LocalDateTime createdAt;

    // CONSTUCTOR 
    public UserData(){
    }
    
    /**
     * Constructs UserData with all fields.
     * @param userId User's database ID
     * @param email User's email
     * @param userName User's display name
     * @param createdAt Account creation timestamp
     */
    public UserData(long userId, String email, String userName, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.createdAt = createdAt;
    }
    
    // Getters & Setters
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
