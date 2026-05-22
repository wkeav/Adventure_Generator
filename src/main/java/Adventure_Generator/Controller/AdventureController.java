package Adventure_generator.Controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import Adventure_generator.Service.AdventureHistoryService;
import Adventure_generator.Service.AdventureService;

import Adventure_generator.Service.GeminiAdventureService;

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


    private static final Logger logger = LoggerFactory.getLogger(AdventureController.class);
   
    private final AdventureService adventureService;
    private final GeminiAdventureService geminiAdventureService;
    private final UserRepository userRepository;
    private final AdventureHistoryService adventureHistoryService;

    public AdventureController(AdventureService adventureService,
                               GeminiAdventureService geminiAdventureService,
                               UserRepository userRepository,
                               AdventureHistoryService adventureHistoryService  ) {
        this.adventureService = adventureService;
        this.geminiAdventureService = geminiAdventureService;
        this.userRepository = userRepository;
        this.adventureHistoryService = adventureHistoryService;
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
    @PostMapping(value = "/generate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AdventureResponse> generateAdventure(@RequestBody AdventureRequest adventureRequest) {
        String mood = adventureRequest.getMood();
        String weather = adventureRequest.getWeather();
        Boolean longDistance = adventureRequest.getLongDistance();

        // Validate input parameters
        if (mood == null || weather == null) {
            logger.warn("Invalid adventure request: mood or weather is null");
            return ResponseEntity.badRequest()
                .body(new AdventureResponse("Mood and weather are required.", 0L, "N/A"));
        }

        try {
            // Try Gemini first, fall back to static JSON if unavailable
            String adventureText;

            if (Boolean.TRUE.equals(adventureRequest.getAiGenerated())) {
                // AI Mode — call Gemini
                logger.debug("AI mode enabled — calling Gemini");
                try {
                    adventureText = geminiAdventureService.generateAdventure(
                        mood, weather, longDistance
                    );
                } catch (Exception e) {
                    logger.warn("Gemini failed, falling back to static JSON: {}", e.getMessage());
                    adventureText = adventureService.generateAdventure(mood, weather, longDistance);
                }
            } else {
                // Default mode — static JSON
                logger.debug("Default mode — using static adventures.json");
                adventureText = adventureService.generateAdventure(mood, weather, longDistance);
            }

            // Get currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User '" + username + "' not found in database. Please logout and register/login again."));
            
            logger.info("Generating adventure for user: {}", username);
            
            // Save adventure to database
            Adventure savedAdventure = adventureService.saveAdventure(
                adventureText, 
                currentUser, 
                mood, 
                weather, 
                longDistance
            );
            
            // Track adventure in history
            adventureHistoryService.startAdventure(currentUser, savedAdventure);
            
            // Return response with adventure ID
            AdventureResponse response = new AdventureResponse(
                adventureText,
                savedAdventure.getId(),
                currentUser.getUserName()
            );
            
            logger.info("Successfully generated adventure id={} for user={}", savedAdventure.getId(), username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Runtime error in generateAdventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new AdventureResponse("An error occurred while generating adventure: " + e.getMessage(), 0L, "N/A"));
        } catch (Exception e) {
            logger.error("Unexpected error in generateAdventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(new AdventureResponse("An unexpected error occurred while generating adventure.", 0L, "N/A"));
        }
    }
    
    /**
     * Retrieves adventure history for the authenticated user.
     * 
     * Returns all adventures created by the current user, ordered by creation date
     * descending (newest first). User identity extracted from JWT token.
     * 
     * @return ResponseEntity with List of Adventure entities or error
     */
    @GetMapping(value = "/history", produces = "application/json")
    public ResponseEntity<List<Adventure>> getUserAdventureHistory() {
        try {
            // Get currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            logger.debug("Fetching adventure history for user: {}", username);
            
            // Fetch user's adventures
            List<Adventure> adventures = adventureService.getUserAdventures(currentUser.getId());
            
            logger.info("Retrieved {} adventures for user: {}", adventures.size(), username);
            return ResponseEntity.ok(adventures);
        } catch (RuntimeException e) {
            logger.error("Runtime error fetching adventure history: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching adventure history: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Chat endpoint for interactive conversation with Gemini AI.
     * 
     * @param body request body containing "message" field
     * @return ResponseEntity with chat reply or error
     */
    @PostMapping(value = "/chat", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        try {
            String message = body.get("message");
            if (message == null || message.trim().isEmpty()) {
                logger.warn("Chat endpoint called with empty message");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message cannot be empty"));
            }
            
            logger.debug("Processing chat message");
            String reply = geminiAdventureService.chat(message);
            
            logger.debug("Chat reply generated successfully");
            return ResponseEntity.ok(Map.of("reply", reply));
        } catch (Exception e) {
            logger.error("Error processing chat message: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                .body(Map.of("error", "An error occurred processing your message."));
        }
    }

    /**
     * Mood prediction endpoint.
     * 
     * Predicts user mood based on weather conditions, time of day, and season.
     * 
     * Request body should contain:
     * {
     *   "weather": "sunny",      // required
     *   "timeOfDay": "morning",  // required
     *   "season": "summer"       // required
     * }
     * 
     * @param body request body with weather, timeOfDay, and season
     * @return ResponseEntity with mood prediction or error
     */
    @PostMapping(value = "/mood/predict", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> predictMood(@RequestBody Map<String, String> body) {
        try {
            String weather = body.get("weather");
            String timeOfDay = body.get("timeOfDay");
            String season = body.get("season");
            
            // Validate required parameters
            if (weather == null || timeOfDay == null || season == null) {
                logger.warn("Mood prediction called with missing parameters");
                return ResponseEntity.badRequest().build();
            }
            
            logger.debug("Predicting mood for weather={}, timeOfDay={}, season={}", weather, timeOfDay, season);
            String prediction = geminiAdventureService.predictMood(weather, timeOfDay, season);
            
            logger.debug("Mood prediction completed successfully");
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            logger.error("Error predicting mood: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
