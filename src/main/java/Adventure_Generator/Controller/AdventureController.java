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
import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Service.AdventureService;

/**
 * REST controller for adventure generation and management endpoints.
 * 
 * Provides HTTP endpoints for:
 * - POST /api/adventures/generate - Generate personalized adventure
 * - GET /api/adventures/history - Retrieve user's adventure history
 * 
 * Security:
 * - All endpoints require JWT authentication
 * - User extracted from SecurityContextHolder (set by JwtAuthenticationFilter)
 * - Adventures are user-scoped (users can only see their own)
 * 
 * Request Flow:
 * 1. Client sends JWT token in Authorization header
 * 2. JwtAuthenticationFilter validates token and sets Authentication
 * 3. Controller extracts username from SecurityContext
 * 4. Service layer processes business logic
 * 5. Response returned as JSON
 */
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
     * Generates and persists a new adventure based on user preferences.
     * 
     * Requires authenticated user. Generates adventure text filtered by mood, weather,
     * and distance preference, then saves to database associated with current user.
     * 
     * @param adventureRequest Contains mood, weather, and longDistance preferences
     * @return ResponseEntity with AdventureResponse (adventure text and ID) or error
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
     * Retrieves adventure history for the authenticated user.
     * 
     * Returns all adventures created by the current user, ordered by creation date
     * descending (newest first). User identity extracted from JWT token.
     * 
     * @return ResponseEntity with List of Adventure entities or 500 on error
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
