package Adventure_generator.DTOs.Requests;

/*
 * 
 */

public class AdventureRequest {
    private double longitude;
    private double latitude;
    private String mood;
    private String weather;

    // Constructor
    public AdventureRequest(){
        this.longitude = 0;
        this.latitude = 0;
        this.mood = "Neutral"; 
        this.weather = "clear";
    }
    public AdventureRequest(double lon, double lat, String mood, String weather){
        this.longitude = lon;
        this.latitude = lat;
        this.mood = mood;
        this.weather = weather;
    }
    // Getter & setter
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

    @Override
    public String toString() {
        return "AdventureRequest [longitude=" + longitude + ", latitude=" + latitude + ", mood=" + mood + ", weather="
                + weather + "]";
    }

    
}
