package Adventure_generator.DTOs.Response;

/**
 * Response payload for generated adventures.
 * Includes the adventure text, its persisted ID, and the requesting username.
 */
public class AdventureResponse {
    private String adventureIdea;
    private Long adventureId;
    private String username;

    public AdventureResponse(String adventureIdea, Long adventureId, String username) {
        this.adventureIdea = adventureIdea;
        this.adventureId = adventureId;
        this.username = username;
    }

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
