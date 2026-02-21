package Adventure_generator.DTOs.Response;

/**
 * Response DTO for weather data from external weather API.
 * 
 * Contains current weather information for a location:
 * - Temperature (actual and feels-like)
 * - Weather description and icon
 * - City name
 * 
 * Used by GET /api/weather endpoint to return weather data to the frontend.
 */
public class WeatherResponse {
    
    /** Actual temperature in Celsius */
    private double temperature;
    
    /** Human-readable weather description (e.g., 'clear sky', 'light rain') */
    private String description;
    
    /** Name of the city for this weather data */
    private String cityName;
    
    /** Weather icon code from API (used to display weather icons) */
    private String icon;
    
    /** 'Feels like' temperature in Celsius (accounting for wind chill, humidity) */
    private double feelLike;

    /** Default no-arg constructor */
    public WeatherResponse(){}
    
    /**
     * Constructs a WeatherResponse with all fields.
     * @param temperature Actual temperature in Celsius
     * @param description Weather description
     * @param cityName City name
     * @param icon Weather icon code
     * @param feelLike Feels-like temperature
     */
    public WeatherResponse(double temperature, String description, String cityName, String icon, double feelLike){
        this.temperature = temperature;
        this.description = description;
        this.cityName = cityName;
        this.icon = icon;
        this.feelLike = feelLike;
    }

    // Getters & Setters
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public double getFeelLike() {
        return feelLike;
    }
    
    public void setFeelLike(double feelLike) {
        this.feelLike = feelLike;
    }
    
    @Override
    public String toString() {
        return "WeatherResponse [temperature=" + temperature + ", description=" + description + ", cityName=" + cityName
                + ", icon=" + icon + ", feelLike=" + feelLike + "]";
    }
   

    
}
