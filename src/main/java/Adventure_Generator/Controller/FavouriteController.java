package Adventure_generator.Controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Entity.UserFavourite;
import Adventure_generator.Repository.AdventureRepository;
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Service.FavouriteService;

/**
 * 
 */

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    private static final Logger logger = LoggerFactory.getLogger(FavouriteController.class);

    private final FavouriteService favouriteService;
    private final UserRepository userRepository;
    private final AdventureRepository adventureRepository;

    public FavouriteController(FavouriteService favouriteService,
                                UserRepository userRepository,
                                AdventureRepository adventureRepository) {
        this.favouriteService = favouriteService;
        this.userRepository = userRepository;
        this.adventureRepository = adventureRepository;
    }

    /**
     * POST /api/favourites/toggle/{adventureId}
     * Toggles favourite status for an adventure.
     */
    @PostMapping("/toggle/{adventureId}")
    public ResponseEntity<Map<String, Object>> toggleFavourite(@PathVariable Long adventureId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Adventure adventure = adventureRepository.findById(adventureId)
                    .orElseThrow(() -> new RuntimeException("Adventure not found"));

            boolean added = favouriteService.toggleFavourite(user, adventure);

            return ResponseEntity.ok(Map.of(
                "favourited", added,
                "message", added ? "Added to favourites!" : "Removed from favourites!"
            ));

        } catch (Exception e) {
            logger.error("Error toggling favourite", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/favourites
     * Returns all favourites for the logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserFavourites() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<UserFavourite> favourites = favouriteService.getUserFavourites(user.getId());

            List<Map<String, Object>> response = favourites.stream()
                    .map(fav -> Map.of(
                        "favouriteId", (Object) fav.getId(),
                        "adventureId", fav.getAdventure().getId(),
                        "adventure", fav.getAdventure().getAdventure(),
                        "mood", fav.getAdventure().getMood(),
                        "weather", fav.getAdventure().getWeather(),
                        "createdAt", fav.getCreatedAt().toString()
                    ))
                    .toList();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error fetching favourites", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
