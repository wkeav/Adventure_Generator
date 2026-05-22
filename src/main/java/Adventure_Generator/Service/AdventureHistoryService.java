package Adventure_generator.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.AdventureHistory;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.AdventureHistoryRepository;

/**
 * Service for managing adventure history and completion tracking.
 * 
 * Handles:
 * - Creating new adventure history records when adventures are generated
 * - Updating adventure status (started → completed)
 * - Recording ratings and reviews
 * - Retrieving user's adventure statistics
 * - Calculating completion metrics
 */
@Service
public class AdventureHistoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdventureHistoryService.class);
    private final AdventureHistoryRepository adventureHistoryRepository;

    public AdventureHistoryService(AdventureHistoryRepository adventureHistoryRepository) {
        this.adventureHistoryRepository = adventureHistoryRepository;
    }

    /**
     * Creates a new adventure history record when user generates an adventure.
     * Sets status to "started" and timestamp to current time.
     * 
     * @param user the user who started the adventure
     * @param adventure the adventure that was generated
     * @return the saved AdventureHistory record
     */
    @Transactional
    public AdventureHistory startAdventure(User user, Adventure adventure) {
        AdventureHistory history = new AdventureHistory();
        history.setUser(user);
        history.setAdventure(adventure);
        history.setStatus("started");
        history.setStartedAt(LocalDateTime.now());
        
        AdventureHistory saved = adventureHistoryRepository.save(history);
        logger.debug("Started adventure tracking for user={}, adventure={}", user.getUserName(), adventure.getId());
        return saved;
    }

    /**
     * Marks an adventure as completed.
     * 
     * @param historyId the adventure history record ID
     * @return updated AdventureHistory record
     */
    @Transactional
    public AdventureHistory completeAdventure(Long historyId) {
        AdventureHistory history = adventureHistoryRepository.findById(historyId)
            .orElseThrow(() -> new RuntimeException("Adventure history not found"));
        
        history.setStatus("completed");
        history.setCompletedAt(LocalDateTime.now());
        
        AdventureHistory updated = adventureHistoryRepository.save(history);
        logger.debug("Completed adventure history id={}", historyId);
        return updated;
    }

    /**
     * Updates rating and notes for a completed adventure.
     * 
     * @param historyId the adventure history record ID
     * @param rating rating from 1-5
     * @param notes user's notes/review
     * @return updated AdventureHistory record
     */
    @Transactional
    public AdventureHistory rateAdventure(Long historyId, Integer rating, String notes) {
        AdventureHistory history = adventureHistoryRepository.findById(historyId)
            .orElseThrow(() -> new RuntimeException("Adventure history not found"));
        
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        history.setRating(rating);
        history.setNotes(notes);
        
        AdventureHistory updated = adventureHistoryRepository.save(history);
        logger.debug("Rated adventure history id={}, rating={}", historyId, rating);
        return updated;
    }

    /**
     * Retrieves all adventure history for a user, newest first.
     * 
     * @param userId the user's ID
     * @return list of adventure history records
     */
    @Transactional(readOnly = true)
    public List<AdventureHistory> getUserHistory(Long userId) {
        return adventureHistoryRepository.findByUserIdOrderByStartedAtDesc(userId);
    }

    /**
     * Retrieves only completed adventures for a user.
     * 
     * @param userId the user's ID
     * @return list of completed adventure history records
     */
    @Transactional(readOnly = true)
    public List<AdventureHistory> getCompletedAdventures(Long userId) {
        return adventureHistoryRepository.findCompletedByUserId(userId);
    }

    /**
     * Retrieves in-progress (started but not completed) adventures.
     * 
     * @param userId the user's ID
     * @return list of in-progress adventure history records
     */
    @Transactional(readOnly = true)
    public List<AdventureHistory> getInProgressAdventures(Long userId) {
        return adventureHistoryRepository.findInProgressByUserId(userId);
    }

    /**
     * Gets count of completed adventures for a user.
     * 
     * @param userId the user's ID
     * @return number of completed adventures
     */
    @Transactional(readOnly = true)
    public Long getCompletionCount(Long userId) {
        return adventureHistoryRepository.countCompletedByUserId(userId);
    }

    /**
     * Gets average rating of all completed adventures.
     * 
     * @param userId the user's ID
     * @return average rating (null if no ratings)
     */
    @Transactional(readOnly = true)
    public Double getAverageRating(Long userId) {
        return adventureHistoryRepository.getAverageRatingByUserId(userId);
    }

    /**
     * Checks if user has already started tracking this adventure.
     * 
     * @param userId the user's ID
     * @param adventureId the adventure's ID
     * @return true if history exists
     */
    @Transactional(readOnly = true)
    public boolean hasStartedAdventure(Long userId, Long adventureId) {
        return adventureHistoryRepository.findByUserIdAndAdventureId(userId, adventureId).isPresent();
    }
}
