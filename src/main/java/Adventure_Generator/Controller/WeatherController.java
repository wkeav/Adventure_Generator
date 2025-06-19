package Adventure_Generator.Controller;

import org.springframework.web.bind.annotation.RestController;

import Adventure_Generator.DTOs.Response.WeatherResponse;
import Adventure_Generator.Service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/api/weather")
    public WeatherResponse getWeather(@RequestParam double lat, double lon) {
        return weatherService.getCurrentWeather(lat,lon);
        }
    
}
