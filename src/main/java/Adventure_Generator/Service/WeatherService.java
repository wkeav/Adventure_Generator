package Adventure_Generator.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Adventure_Generator.API.WeatherApiClient;
import Adventure_Generator.DTOs.Response.WeatherResponse;

@Service
public class WeatherService {
    @Autowired
    private WeatherApiClient weatherApiClient;
    
    public WeatherResponse getCurrentWeather(double lat, double lon){
        if(lat < -90 || lat > 90 || lon < -180 || lon > 180){
            throw new IllegalArgumentException("Invalid coordinates, try again.");
        }

        
    }
}
