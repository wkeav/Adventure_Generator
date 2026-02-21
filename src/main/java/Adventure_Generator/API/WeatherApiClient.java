package Adventure_generator.API;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import Adventure_generator.DTOs.Response.WeatherResponse;

/**
 * API client for OpenWeatherMap weather data service.
 * 
 * Handles HTTP communication with OpenWeatherMap API:
 * - Constructs API requests with coordinates and API key
 * - Parses JSON responses
 * - Converts temperatures from Kelvin to Celsius
 * - Extracts relevant weather data (temp, feels-like, description, icon)
 * 
 * Uses Spring WebClient for reactive HTTP calls.
 * API key is injected from application.properties via @Value.
 * 
 * @see WeatherResponse
 */
@Component
public class WeatherApiClient {
    
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${weather.api.key}")
    private String API_KEY;

    /** Initializes WebClient with OpenWeatherMap base URL */
    public WeatherApiClient(){
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    /**
     * Fetches current weather data for given geographic coordinates.
     * 
     * Calls OpenWeatherMap API with lat/lon, parses JSON response,
     * converts Kelvin to Celsius, and extracts weather details.
     * 
     * @param latitude Latitude in degrees
     * @param longitude Longitude in degrees
     * @return WeatherResponse with temperature, description, city name, and icon
     */
    public WeatherResponse getWeatherByCoordinates(double latitude, double longitude){
        String url = String.format("?lat=%.2f&lon=%.2f&appid=%s", latitude,longitude,this.API_KEY);

        // Get raw JSON as a map 
        Map<String, Object> rawResponse = webClient.get()
                .uri(url) // sets the endpoint
                .retrieve()     //prepare 
                .bodyToMono(Map.class) // Json as a map
                .block();

        // Extracting only what we need
        Map<String, Object> main = (Map<String,Object>) rawResponse.get("main");
        Double tempKelvin = ((Number) main.get("temp")).doubleValue();
        Double tempCelsius = tempKelvin - 273.15;
        Double feelsLikeKelvin = ((Number) main.get("feels_like")).doubleValue();
        Double feelsLikeCelsius = feelsLikeKelvin - 273.15;

        List<Map<String,Object>> weatherList = (List<Map<String, Object>>)rawResponse.get("weather");
        Map<String,Object> weather = weatherList.get(0);
        String description = (String) weather.get("description");
        String icon = (String) weather.get("icon");

        String name = (String) rawResponse.get("name");

        return new WeatherResponse(tempCelsius, description,name,icon,feelsLikeCelsius);
    }


}
