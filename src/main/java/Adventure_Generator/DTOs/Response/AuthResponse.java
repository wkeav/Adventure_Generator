package Adventure_generator.DTOs.Response;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserData userData;
    private String error;

    public AuthResponse(boolean success, String message, String token, UserData userData, String error) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userData = userData;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AuthResponse [success=" + success + ", message=" + message + ", token=" + token + ", userData="
                + userData + ", error=" + error + "]";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    

    
}
