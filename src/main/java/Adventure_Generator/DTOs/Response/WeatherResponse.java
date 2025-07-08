package Adventure_Generator.DTOs.Response;

public class WeatherResponse {
    private double temperature;
    private String description;
    private String cityName;
    private String icon;
    private double feelLike;

    public WeatherResponse(){}
    public WeatherResponse(double temperature, String description, String cityName,String icon, double feelLike){
        this.temperature = temperature;
        this.description = description;
        this.cityName = cityName;
        this.icon = icon;
        this.feelLike = feelLike;
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
