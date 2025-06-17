package Adventure_Generator.API;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WeatherApiClient {
    private final WebClient webClient;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${weather.api.key}")
    private String API_KEY;

    public WeatherApiClient(){
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    //Get api call to weather api 
    

}
