package Adventure_generator.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.AdventureRepository;
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Service.FavouriteService;

/**
 * REST Controller for managing user favourites/bookmarks.
 * 
 * Provides endpoints to:
 * - Toggle favourites (add/remove adventures to user's bookmark list)
 * - Retrieve all favourites for a user
 * - Check if a specific adventure is favourited by a user
 * 
 * All endpoints require authentication via JWT token.
 * 
 * @author Adventure Generator Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {
    
    private final FavouriteService favouriteService;
    private final UserRepository userRepository;
    private final AdventureRepository adventureRepository;

    /**
     * Constructor for dependency injection.
     * 
     * @param favouriteService service layer for favourite operations
     * @param userRepository repository for user data access
     * @param adventureRepository repository for adventure data access
     */
    public FavouriteController(FavouriteService favouriteService, UserRepository userRepository, AdventureRepository adventureRepository) {
        this.favouriteService = favouriteService;
        this.userRepository = userRepository;
        this.adventureRepository = adventureRepository;
    }

    /**
     * Toggles the favourite status of an adventure for the authenticated user.
     * 
     * If the adventure is already favourited, it will be removed.
     * If it's not favourited, it will be added to the user's favourites.
     * 
     * HTTP Method: POST
     * Endpoint: /api/favourites/toggle/{adventureId}
     * 
     * @param adventureId the ID of the adventure to toggle (path variable)
     * @return ResponseEntity containing a map with:
     *         - "favourited": boolean indicating if the adventure is now favourited
     *         - "message": user-friendly confirmation message
     *         Returns 400 BAD_REQUEST if user or adventure not found
     * @throws RuntimeException if user or adventure cannot be found
     */
    @PostMapping("/toggle/{adventureId}")
    public ResponseEntity<Map<String, Object>> toggleFavourite(@PathVariable Long adventureId) {
        try {
            // Retrieve the current authenticated user from the security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Fetch user from database, throw exception if not found
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch adventure from database, throw exception if not found
            Adventure adventure = adventureRepository.findById(adventureId)
                    .orElseThrow(() -> new RuntimeException("Adventure not found"));

            // Toggle the favourite status and get the result
            boolean added = favouriteService.toggleFavourite(user, adventure);

            // Return success response with the new state
            return ResponseEntity.ok(Map.of(
                "favourited", added,
                "message", added ? "Added to favourites!" : "Removed from favourites!"
            ));

        } catch (Exception e) {
            // Return error response with exception details
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Retrieves all favourites for the authenticated user.
     * 
     * Returns a list of adventures that the user has bookmarked, including
     * adventure details like text, mood, weather, and creation timestamp.
     * 
     * HTTP Method: GET
     * Endpoint: /api/favourites/user
     * 
     * @return ResponseEntity containing a list of maps, each with:
     *         - "favouriteId": unique ID of the favourite record
     *         - "adventureId": ID of the adventure
     *         - "adventure": the adventure description text
     *         - "mood": the mood associated with the adventure
     *         - "weather": the weather conditions for the adventure
     *         - "createdAt": timestamp when the adventure was favourited
     *         Returns 400 BAD_REQUEST if user not found
     * @throws RuntimeException if user cannot be found
     */
    @GetMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUserFavourites() {
        try {
            // Retrieve the current authenticated user from the security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Fetch user from database, throw exception if not found
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Retrieve all favourites for the user from the service layer
            List<Map<String, Object>> favourites = favouriteService.getUserFavourites(user.getId());

            // Return the favourites in the response
            return ResponseEntity.ok(favourites);

        } catch (Exception e) {
            // Return error response if any exception occurs
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Checks if a specific adventure is favourited by the authenticated user.
     * 
     * Used primarily by the UI to determine whether to display a filled or
     * unfilled bookmark icon for a given adventure.
     * 
     * HTTP Method: GET
     * Endpoint: /api/favourites/check/{adventureId}
     * 
     * @param adventureId the ID of the adventure to check (path variable)
     * @return ResponseEntity containing a map with:
     *         - "isFavourited": boolean indicating if the adventure is favourited
     *         Returns 400 BAD_REQUEST if user or adventure not found
     * @throws RuntimeException if user or adventure cannot be found
     */
    @GetMapping("/check/{adventureId}")
    public ResponseEntity<Map<String, Boolean>> isFavourited(@PathVariable Long adventureId) {
        // Retrieve the current authenticated user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        
        // Fetch user from database, throw exception if not found
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Fetch adventure from database, throw exception if not found
        Adventure adventure = adventureRepository.findById(adventureId)
            .orElseThrow(() -> new RuntimeException("Adventure not found"));
        
        // Check if the adventure is favourited by the user
        boolean isFavourited = favouriteService.isFavourited(user, adventure);
        
        // Return the result
        return ResponseEntity.ok(Map.of("isFavourited", isFavourited));
    }
