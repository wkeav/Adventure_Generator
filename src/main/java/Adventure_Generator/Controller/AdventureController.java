package Adventure_Generator.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import Adventure_Generator.DTOs.Requests.AdventureRequest;
import Adventure_Generator.DTOs.Response.AdventureResponse;
import Adventure_Generator.Service.AdventureService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "/")
public class AdventureController {
    AdventureResponse adventureResponse;
    @Autowired
    AdventureService adventureService;

    @PostMapping(value = "generate-adventure" ,produces = "application/json", consumes = "application/json")
    public ResponseEntity generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        String mood = adventureRequest.getMood();
        String weather = adventureRequest.getWeather();

        String adventure = adventureService.generateAdventure(mood, weather);
        AdventureResponse response = new AdventureResponse(adventure, 0, "N/A");
        return ResponseEntity.ok(response);
    }
    
    
}
