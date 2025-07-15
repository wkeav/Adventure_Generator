package Adventure_generator.DTOs.Response;

import java.time.LocalDateTime;

public class UserData {
    private long userId;
    private String email;
    private String userName;
    private LocalDateTime createdAt;

    public UserData(){

    }
    public UserData(long userId, String email, String userName, LocalDateTime createdAt) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.createdAt = createdAt;
    }
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
