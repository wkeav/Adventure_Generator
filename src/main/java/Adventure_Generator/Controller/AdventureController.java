package Adventure_generator.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.DTOs.Requests.AdventureRequest;
import Adventure_generator.DTOs.Response.AdventureResponse;
import Adventure_generator.Service.AdventureService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "/api/adventures")
public class AdventureController {
    AdventureResponse adventureResponse;
    @Autowired
    AdventureService adventureService;

    @PostMapping(value = "/generate" ,produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        String mood = adventureRequest.getMood();
        String weather = adventureRequest.getWeather();

        String adventure = adventureService.generateAdventure(mood, weather);
        AdventureResponse response = new AdventureResponse(adventure, 0, "N/A");
        return ResponseEntity.ok(response);
    }
    
    
}
