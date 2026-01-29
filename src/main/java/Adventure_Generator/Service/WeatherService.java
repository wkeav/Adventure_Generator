package Adventure_generator.Service;

/**
 * Weather Service
 * 
 * Business logic layer for weather data retrieval and validation.
 * Validates geographic coordinates and delegates API calls to WeatherApiClient.
 * 
 * Features:
 * - Geographic coordinate validation
 * - Weather data retrieval by latitude/longitude
 * - Integration with OpenWeatherMap API
 * 
 * Validation:
 * - Latitude: -90 to 90 degrees
 * - Longitude: -180 to 180 degrees
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 * @since 2026-01-28
 */

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Adventure_generator.API.WeatherApiClient;
import Adventure_generator.DTOs.Response.WeatherResponse;
import ch.qos.logback.classic.Logger;

@Service
public class WeatherService {
    @Autowired
    private WeatherApiClient weatherApiClient;

    public WeatherResponse getCurrentWeather(double lat, double lon){
        if(lat < -90 || lat > 90 || lon < -180 || lon > 180){
            throw new IllegalArgumentException("Invalid coordinates, try again.");
        }
        return weatherApiClient.getWeatherByCoordinates(lat, lon);

    
    }
}
