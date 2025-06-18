package Adventure_Generator.Controller;

import org.springframework.web.bind.annotation.RestController;

import Adventure_Generator.DTOs.Response.WeatherResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class WeatherController {
    @GetMapping("/api/weather")
    public WeatherResponse getWeather(@RequestParam double lat, double lon) {
        return null;
    }
    
}
