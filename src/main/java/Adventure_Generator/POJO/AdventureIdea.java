package Adventure_generator.POJO;

/**
 * Plain Old Java Object (POJO) for adventure idea data parsed from JSON.
 * 
 * Used to deserialize adventure recommendations from the adventures.json resource file.
 * Contains the mood, weather, distance preference, and the adventure suggestion text.
 * 
 * This is a simple data transfer object without any business logic.
 */
public class AdventureIdea {
    
    private String mood;
    private String weather;
    
    /** The adventure recommendation text */
    private String adventure;
    private String distance;
    
    // Getters & Setters
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
    
    public String getAdventure() {
        return adventure;
    }
    
    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }
    
    public String getDistance() {
        return distance;
    }
    
    public void setDistance(String distance) {
        this.distance = distance;
    }
}
