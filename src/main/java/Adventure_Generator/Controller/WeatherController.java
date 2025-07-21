package Adventure_generator.Controller;

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
