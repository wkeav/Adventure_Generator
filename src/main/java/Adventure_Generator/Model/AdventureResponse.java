package Adventure_Generator.Model;

public class AdventureResponse {
    private String adventureIdea;
    private double temperature;
    private String weatherDescription;

    // Constructor
    public AdventureResponse(String adventureIdea, double temperature, String weatherDescription) {
        this.adventureIdea = adventureIdea;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }
    // Getter & setter
    public String getAdventureIdea() {
        return adventureIdea;
    }

    public void setAdventureIdea(String adventureIdea) {
        this.adventureIdea = adventureIdea;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    } 

    
    
}
