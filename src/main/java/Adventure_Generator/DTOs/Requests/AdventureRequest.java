package Adventure_generator.DTOs.Requests;

/**
 * Request DTO for adventure generation endpoint.
 * 
 * Contains user preferences and location data used to generate
 * personalized adventure recommendations:
 * - Geographic coordinates for weather lookup
 * - Mood preference (e.g., 'energetic', 'relaxed')
 * - Weather preference (e.g., 'clear', 'rain', 'snow')
 * - Distance preference (local vs. long-distance)
 * 
 * Used by POST /api/adventures/generate
 */
public class AdventureRequest {
    
    private double longitude;
    private double latitude;
    
    private String mood;
    private String weather;
    private Boolean longDistance; 

    /** Default constructor with neutral preferences */
    public AdventureRequest(){
        this.longitude = 0;
        this.latitude = 0;
        this.mood = "Neutral"; 
        this.weather = "clear";
    }

    /**
     * Constructs an AdventureRequest with all fields.
     * @param longitude User's longitude
     * @param latitude User's latitude
     * @param mood User's mood preference
     * @param weather Preferred weather condition
     * @param longDistance Whether long-distance travel is preferred
     */
    public AdventureRequest(double longitude, double latitude, String mood, String weather, Boolean longDistance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.mood = mood;
        this.weather = weather;
        this.longDistance = longDistance;
    }
    
    // Getters & Setters
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getWeather() {
        return weather;
    }
    
    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Boolean getLongDistance() {
        return longDistance;
    }
    
    public void setLongDistance(Boolean longDistance) {
        this.longDistance = longDistance;
    }

    @Override
    public String toString() {
        return "AdventureRequest [longitude=" + longitude + ", latitude=" + latitude + ", mood=" + mood + ", weather="
                + weather + ", longDistance=" + longDistance + "]";
    }
}
