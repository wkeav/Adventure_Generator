package Adventure_generator.DTOs.Response;

/**
 * Response DTO for adventure generation API endpoints.
 * 
 * Contains the generated adventure recommendation, its persisted database ID,
 * and the username of the requesting user.
 * 
 * Used by:
 * - POST /api/adventures/generate - Returns newly generated adventure
 * - GET /api/adventures/history - Returns list of user's adventures
 */
public class AdventureResponse {
    
    private String adventureIdea;
    private Long adventureId;
    private String username;

    /**
     * Constructs a new AdventureResponse with all fields.
     * @param adventureIdea The adventure recommendation text
     * @param adventureId The persisted adventure's database ID
     * @param username The username of the requesting user
     */
    public AdventureResponse(String adventureIdea, Long adventureId, String username) {
        this.adventureIdea = adventureIdea;
        this.adventureId = adventureId;
        this.username = username;
    }

    // Getters & Setters
    public String getAdventureIdea() {
        return adventureIdea;
    }

    public void setAdventureIdea(String adventureIdea) {
        this.adventureIdea = adventureIdea;
    }

    public Long getAdventureId() {
        return adventureId;
    }

    public void setAdventureId(Long adventureId) {
        this.adventureId = adventureId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AdventureResponse [adventureIdea=" + adventureIdea + ", adventureId=" + adventureId
                + ", username=" + username + "]";
    }
}
