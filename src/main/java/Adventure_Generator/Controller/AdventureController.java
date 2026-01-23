package Adventure_generator.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.DTOs.Requests.AdventureRequest;
import Adventure_generator.DTOs.Response.AdventureResponse;
import Adventure_generator.Model.Adventure;
import Adventure_generator.Model.User;
import Adventure_generator.Service.AdventureService;


@RestController
@RequestMapping(path = "/api/adventures")
public class AdventureController {

    private final AdventureService adventureService;

    public AdventureController(AdventureService adventureService) {
        this.adventureService = adventureService;
    }

    /**
     * Generate and save a new adventure based on mood, weather, and distance preference
     * Requires authentication - saves adventure to the logged-in user
     */
    @PostMapping(value = "/generate", produces = "application/json")
    public ResponseEntity<AdventureResponse> generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        String mood = adventureRequest.getMood();
        String weather = adventureRequest.getWeather();
        Boolean longDistance = adventureRequest.getLongDistance();

        try {
            if (mood != null && weather != null) {
                // Generate adventure text
                String adventureText = adventureService.generateAdventure(mood, weather, longDistance);
                
                // Get currently authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User currentUser = (User) authentication.getPrincipal();
                
                // Save adventure to database
                Adventure savedAdventure = adventureService.saveAdventure(
                    adventureText, 
                    currentUser, 
                    mood, 
                    weather, 
                    longDistance
                );
                
                // Return response with adventure ID
                AdventureResponse response = new AdventureResponse(
                    adventureText,
                    savedAdventure.getId(),
                    currentUser.getUserName()
                );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest()
                    .body(new AdventureResponse("Mood and weather are required.", 0L, "N/A"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                .body(new AdventureResponse("An error occurred while generating adventure.", 0L, "N/A"));
        }
    }
    
    /**
     * Get all adventures for the currently authenticated user.
     * Returns adventures ordered by newest first.
     */
    @GetMapping(value = "/history", produces = "application/json")
    public ResponseEntity<List<Adventure>> getUserAdventureHistory() {
        try {
            // Get currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();
            
            // Fetch user's adventures
            List<Adventure> adventures = adventureService.getUserAdventures(currentUser.getId());
            
            return ResponseEntity.ok(adventures);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
