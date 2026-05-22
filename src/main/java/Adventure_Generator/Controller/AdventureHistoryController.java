package Adventure_generator.Controller;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Adventure_generator.Entity.AdventureHistory;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.UserRepository;
import Adventure_generator.Service.AdventureHistoryService;

/**
 * REST controller for adventure history and completion tracking.
 * 
 * Provides endpoints for:
 * - GET /api/history - Get all user's adventure history
 * - GET /api/history/completed - Get completed adventures
 * - GET /api/history/in-progress - Get in-progress adventures
 * - GET /api/history/stats - Get user's adventure statistics
 * - POST /api/history/{historyId}/complete - Mark adventure as completed
 * - PUT /api/history/{historyId}/rate - Rate a completed adventure
 * 
 * All endpoints require JWT authentication.
 */
@RestController
@RequestMapping(path = "/api/history")
public class AdventureHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(AdventureHistoryController.class);
    private final AdventureHistoryService adventureHistoryService;
    private final UserRepository userRepository;

    public AdventureHistoryController(AdventureHistoryService adventureHistoryService,
                                      UserRepository userRepository) {
        this.adventureHistoryService = adventureHistoryService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all adventure history for the authenticated user.
     * Ordered by most recent first.
     * 
     * @return ResponseEntity with list of adventure history records
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<AdventureHistory>> getUserHistory() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) auth.getPrincipal();

            logger.debug("Fetching adventure history for user: {}", username);
            User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<AdventureHistory> history = adventureHistoryService.getUserHistory(user.getId());
            logger.info("Retrieved {} history records for user: {}", history.size(), username);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            logger.error("Runtime error fetching user history: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching user history: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Retrieves only completed adventures for the authenticated user.
     * 
     * @return ResponseEntity with list of completed adventure history records
     */
    @GetMapping(value = "/completed", produces = "application/json")
    public ResponseEntity<List<AdventureHistory>> getCompletedAdventures() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) auth.getPrincipal();

            logger.debug("Fetching completed adventures for user: {}", username);
            User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<AdventureHistory> completedHistory = adventureHistoryService.getCompletedAdventures(user.getId());
            logger.info("Retrieved {} completed adventures for user: {}", completedHistory.size(), username);
            return ResponseEntity.ok(completedHistory);
        } catch (RuntimeException e) {
            logger.error("Runtime error fetching completed adventures: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching completed adventures: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Retrieves in-progress (started but not completed) adventures for the authenticated user.
     * 
     * @return ResponseEntity with list of in-progress adventure history records
     */
    @GetMapping(value = "/in-progress", produces = "application/json")
    public ResponseEntity<List<AdventureHistory>> getInProgressAdventures() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) auth.getPrincipal();

            logger.debug("Fetching in-progress adventures for user: {}", username);
            User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<AdventureHistory> inProgress = adventureHistoryService.getInProgressAdventures(user.getId());
            logger.info("Retrieved {} in-progress adventures for user: {}", inProgress.size(), username);
            return ResponseEntity.ok(inProgress);
        } catch (RuntimeException e) {
            logger.error("Runtime error fetching in-progress adventures: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching in-progress adventures: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Retrieves adventure statistics for the authenticated user.
     * 
     * Returns:
     * - completionCount: number of completed adventures
     * - averageRating: average rating of all completed adventures
     * 
     * @return ResponseEntity with statistics map
     */
    @GetMapping(value = "/stats", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getAdventureStats() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) auth.getPrincipal();

            logger.debug("Fetching adventure statistics for user: {}", username);
            User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            Long completionCount = adventureHistoryService.getCompletionCount(user.getId());
            Double averageRating = adventureHistoryService.getAverageRating(user.getId());

            Map<String, Object> stats = Map.of(
                "completionCount", completionCount,
                "averageRating", averageRating != null ? averageRating : 0.0
            );
            logger.info("Adventure stats for user: {}: completionCount={}, averageRating={}", username, completionCount, averageRating);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            logger.error("Runtime error fetching adventure stats: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching adventure stats: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Marks an adventure as completed.
     * 
     * @param historyId the ID of the adventure history record to complete
     * @return ResponseEntity with updated adventure history record
     */
    @PostMapping(value = "/{historyId}/complete", produces = "application/json")
    public ResponseEntity<AdventureHistory> completeAdventure(@PathVariable Long historyId) {
        try {
            logger.debug("Marking adventure as completed: historyId={}", historyId);
            AdventureHistory completed = adventureHistoryService.completeAdventure(historyId);
            logger.info("Adventure completed successfully: historyId={}", historyId);
            return ResponseEntity.ok(completed);
        } catch (RuntimeException e) {
            logger.error("Runtime error completing adventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error completing adventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    /**
     * Rates a completed adventure and optionally adds notes.
     * 
     * Request body should contain:
     * {
     *   "rating": 5,          // 1-5 scale (required)
     *   "notes": "Amazing!"   // optional
     * }
     * 
     * @param historyId the ID of the adventure history record to rate
     * @param payload map with rating and notes
     * @return ResponseEntity with updated adventure history record
     */
    @PutMapping(value = "/{historyId}/rate", produces = "application/json", consumes = "application/json")
    public ResponseEntity<AdventureHistory> rateAdventure(@PathVariable Long historyId,
                                                          @RequestBody Map<String, Object> payload) {
        try {
            if (payload.get("rating") == null) {
                logger.warn("Rate adventure called without rating: historyId={}", historyId);
                return ResponseEntity.badRequest().build();
            }
            
            Integer rating = ((Number) payload.get("rating")).intValue();
            String notes = (String) payload.get("notes");

            logger.debug("Rating adventure: historyId={}, rating={}", historyId, rating);
            AdventureHistory rated = adventureHistoryService.rateAdventure(historyId, rating, notes);
            logger.info("Adventure rated successfully: historyId={}, rating={}", historyId, rating);
            return ResponseEntity.ok(rated);
        } catch (RuntimeException e) {
            logger.error("Runtime error rating adventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logger.error("Unexpected error rating adventure: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

}
