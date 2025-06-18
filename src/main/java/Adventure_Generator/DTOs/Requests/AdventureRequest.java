package Adventure_Generator.DTOs.Requests;

/*
 * 
 */

public class AdventureRequest {
    private double longitude;
    private double latitude;
    private String mood;

    // Constructor
    public AdventureRequest(){
        this.longitude = 0;
        this.latitude = 0;
        this.mood = "Neutral"; 
    }
    public AdventureRequest(double lon, double lat, String mood){
        this.longitude = lon;
        this.latitude = lat;
        this.mood = mood;
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
    @Override
    public String toString() {
        return "AdventureRequest [longitude=" + longitude + ", latitude=" + latitude + ", mood=" + mood + "]";
    }
    
}
