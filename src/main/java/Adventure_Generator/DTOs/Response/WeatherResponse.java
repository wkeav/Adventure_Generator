package Adventure_Generator.DTOs.Response;

public class WeatherResponse {
    private double temperature;
    private String description;
    private String cityName;

    public WeatherResponse(){}
    public WeatherResponse(double temperature, String description, String cityName){
        this.temperature = temperature;
        this.description = description;
        this.cityName = cityName;
    }

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
    @Override
    public String toString() {
        return "WeatherResponse [temperature=" + temperature + ", description=" + description + ", cityName=" + cityName
                + "]";
    }

    
}
