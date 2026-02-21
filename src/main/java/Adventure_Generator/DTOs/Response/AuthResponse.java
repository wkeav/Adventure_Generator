package Adventure_generator.DTOs.Response;

/**
 * Response DTO for authentication endpoints (login/register).
 * 
 * Contains authentication results including:
 * - Success status
 * - Message or error description
 * - JWT token (if successful)
 * - User data (if successful)
 * 
 * Used by:
 * - POST /api/auth/login
 * - POST /api/auth/register
 */
public class AuthResponse {

    private boolean success;
    private String message; // error message
    
    /** JWT token for authenticated requests (null if failed) */
    private String token;
    
    /** User data for the authenticated user (null if failed) */
    private UserData userData;
    
    private String error; // JWT error message

    /**
     * Constructs an AuthResponse with all fields.
     * @param success Whether authentication succeeded
     * @param message Success or error message
     * @param token JWT token (null if failed)
     * @param userData User data (null if failed)
     * @param error Error details (null if successful)
     */
    public AuthResponse(boolean success, String message, String token, UserData userData, String error) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.userData = userData;
        this.error = error;
    }

    // Getters & Setters
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthResponse [success=" + success + ", message=" + message + ", token=" + token + ", userData="
                + userData + ", error=" + error + "]";
    }
}
