package Adventure_generator.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Adventure_generator.Entity.AdventureHistory;

/**
 * Repository for AdventureHistory entity.
 * 
 * P
 * 
 */

@Repository
public interface HistoryAdventureRepository extends JpaRepository<AdventureHistory, Long> {
    /**
     * Finds all adventure history records for a specific user.
     * Ordered by most recent first (startedAt descending).
     * 
     * @param userId the user's ID
     * @return list of AdventureHistory records for the user, sorted by newest first
     */
    List<AdventureHistory> findByUserIdOrderByCreatedAtDesc(Long userId); 


}
