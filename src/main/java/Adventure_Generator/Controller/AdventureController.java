package Adventure_Generator.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import Adventure_Generator.Model.AdventureRequest;
import Adventure_Generator.Model.AdventureResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(path = "/")
public class AdventureController {
    
    @PostMapping(value = "generate" ,produces = "application/json", consumes = "application/json")
    public ResponseEntity generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    
}
