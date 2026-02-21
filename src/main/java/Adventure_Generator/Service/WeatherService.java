package Adventure_generator.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Adventure_generator.API.WeatherApiClient;
import Adventure_generator.DTOs.Response.WeatherResponse;

/**
 * Service layer for weather data operations.
 * 
 * Handles business logic for:
 * - Geographic coordinate validation
 * - Weather data retrieval via external API
 * - Input sanitization for API calls
 * 
 * Validation Rules:
 * - Latitude: -90 to 90 degrees
 * - Longitude: -180 to 180 degrees
 * 
 * Delegates actual API calls to WeatherApiClient.
 */
@Service
public class WeatherService {
    
    @Autowired
    private WeatherApiClient weatherApiClient;

    /**
     * Retrieves current weather data for given coordinates.
     * 
     * @param lat Latitude in degrees (-90 to 90)
     * @param lon Longitude in degrees (-180 to 180)
     * @return WeatherResponse with current weather data
     * @throws IllegalArgumentException if coordinates are out of valid range
     */
    public WeatherResponse getCurrentWeather(double lat, double lon){
        if(lat < -90 || lat > 90 || lon < -180 || lon > 180){
            throw new IllegalArgumentException("Invalid coordinates, try again.");
        }
        return weatherApiClient.getWeatherByCoordinates(lat, lon);

    
    }
}
