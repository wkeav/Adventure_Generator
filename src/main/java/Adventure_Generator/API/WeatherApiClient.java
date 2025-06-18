package Adventure_Generator.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import Adventure_Generator.DTOs.Response.WeatherResponse;

@Component
public class WeatherApiClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${weather.api.key}")
    private String API_KEY;

    public WeatherApiClient(){
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    // Weather request 
    public WeatherResponse getWeatherByCoordinates(double latitude, double longitude){
        String url = String.format("?lat=%.2f&lon=%.2f&appid=%s", latitude,longitude,this.API_KEY);

        try{
            webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();
                
        } catch (Exception e){
            return new WeatherResponse(20.0, "clear sky", "Unknown");
        }

    }


}
