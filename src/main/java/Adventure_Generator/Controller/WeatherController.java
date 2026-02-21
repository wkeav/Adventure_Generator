package Adventure_generator.Controller;

import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.DTOs.Response.WeatherResponse;
import Adventure_generator.Service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for weather data operations.
 * 
 * Provides HTTP endpoint for:
 * - GET /api/weather?lat={latitude}&lon={longitude} - Get current weather
 * 
 * Security:
 * - Requires JWT authentication (protected by SecurityConfig)
 * 
 * External Dependencies:
 * - OpenWeatherMap API via WeatherService and WeatherApiClient
 * 
 * Response includes temperature (Celsius), weather description, city name, and icon code.
 */
@RestController
public class WeatherController {
    
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    /**
     * Retrieves current weather data for given coordinates.
     * 
     * @param lat Latitude in degrees (-90 to 90)
     * @param lon Longitude in degrees (-180 to 180)
     * @return WeatherResponse with temperature, description, city, and icon
     * @throws IllegalArgumentException if coordinates are invalid (handled by service)
     */
    @GetMapping(value = "/api/weather", produces = "application/json")
    public WeatherResponse getWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.getCurrentWeather(lat, lon);
    }
}
