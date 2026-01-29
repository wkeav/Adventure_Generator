package Adventure_generator.Controller;

/**
 * Weather Controller
 * 
 * REST API controller for weather data retrieval.
 * Fetches current weather information based on user's geographic coordinates.
 * 
 * Endpoints:
 * - GET /api/weather?lat={latitude}&lon={longitude} - Get current weather
 * 
 * Security:
 * - Requires JWT authentication (protected endpoint)
 * 
 * External Dependencies:
 * - OpenWeatherMap API via WeatherApiClient
 * 
 * @author Astra K. Nguyen
 * @version 1.0.0
 * @since 2026-01-28
 */

import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.DTOs.Response.WeatherResponse;
import Adventure_generator.Service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/api/weather", produces = "application/json")
    public WeatherResponse getWeather(@RequestParam double lat, double lon) {
        return weatherService.getCurrentWeather(lat,lon);
        }
    
}
