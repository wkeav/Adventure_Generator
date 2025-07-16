package Adventure_generator.API;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import Adventure_generator.DTOs.Response.WeatherResponse;
import reactor.core.publisher.Mono;

@Component
public class WeatherApiClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${weather.api.key}")
    private String API_KEY;

    public WeatherApiClient(){
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    // Weather GET request 
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
