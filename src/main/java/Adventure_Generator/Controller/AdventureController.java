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
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Service.AdventureService;


@RestController
@RequestMapping(path = "/api/adventures")
public class AdventureController {

    private final AdventureService adventureService;
    private final UserRepository userRepository;

    public AdventureController(AdventureService adventureService, UserRepository userRepository) {
        this.adventureService = adventureService;
        this.userRepository = userRepository;
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
                String username = (String) authentication.getPrincipal();
                User currentUser = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User '" + username + "' not found in database. Please logout and register/login again."));
                
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
            System.err.println("ERROR in generateAdventure: " + e.getClass().getName() + ": " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage());
            }
            return ResponseEntity.status(500)
                .body(new AdventureResponse("An error occurred while generating adventure: " + e.getMessage(), 0L, "N/A"));
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
            String username = (String) authentication.getPrincipal();
            User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Fetch user's adventures
            List<Adventure> adventures = adventureService.getUserAdventures(currentUser.getId());
            
            return ResponseEntity.ok(adventures);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
