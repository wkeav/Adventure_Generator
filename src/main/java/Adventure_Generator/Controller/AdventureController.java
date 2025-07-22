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

    private final AdventureService adventureService;

    public AdventureController(AdventureService adventureService){
        this.adventureService = adventureService;
    }

    @PostMapping(value = "/generate" ,produces = "application/json")
    public ResponseEntity<AdventureResponse> generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        String mood = adventureRequest.getMood();
        String weather = adventureRequest.getWeather();
        Boolean longDistance = adventureRequest.getLongDistance();

        try{
            if(mood != null && weather != null){
                String adventure = adventureService.generateAdventure(mood, weather,longDistance);
                AdventureResponse adventureResponse = new AdventureResponse(adventure, 0, "N/A");
                return ResponseEntity.ok(adventureResponse);
            }else{
                return ResponseEntity.badRequest()
                    .body(new AdventureResponse("Mood and weather are required.", 0, "N/A"));
            }
        }catch(Exception e){
            return ResponseEntity.status(500)
            .body(new AdventureResponse("An error occurred while generating adventure.", 0, "N/A"));
        }

    }
    
    
}
