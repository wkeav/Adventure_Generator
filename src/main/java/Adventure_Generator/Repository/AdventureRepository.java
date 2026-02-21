package Adventure_generator.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Adventure_generator.Entity.Adventure;

/**
 * Repository for Adventure entity providing data access operations.
 * 
 * Provides Spring Data JPA derived query methods for:
 * - Finding adventures by user ID
 * - Ordering adventures by creation date
 * - Filtering by user and mood
 * - Checking existence of adventures for a user
 * 
 * @see Adventure
 */
@Repository
public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    /**
     * Find all adventures for a specific user.
     * @param userId The user's ID
     * @return List of adventures for the user
     */
    List<Adventure> findByUserId(Long userId);

    /**
     * Find all adventures for a user, ordered by creation date descending (newest first).
     * @param userId The user's ID
     * @return List of adventures ordered by createdAt DESC
     */
    List<Adventure> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find adventures for a user filtered by mood preference.
     * @param userId The user's ID
     * @param mood The mood filter (e.g., 'energetic', 'relaxed')
     * @return List of adventures matching the user and mood
     */
    List<Adventure> findByUserIdAndMood(Long userId, String mood);

    /**
     * Check if a user has any adventures.
     * @param userId The user's ID
     * @return true if user has at least one adventure, false otherwise
     */
    boolean existsByUserId(Long userId);
}