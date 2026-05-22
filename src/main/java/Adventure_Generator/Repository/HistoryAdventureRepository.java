// package Adventure_generator.Repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import Adventure_generator.Entity.AdventureHistory;

// /**
//  * Repository for AdventureHistory entity.
//  * 
//  * P
//  * 
//  */

// @Repository
// public interface HistoryAdventureRepository extends JpaRepository<AdventureHistory, Long> {
//     /**
//      * Finds all adventure history records for a specific user.
//      * Ordered by most recent first (startedAt descending).
//      * 
//      * @param userId the user's ID
//      * @return list of AdventureHistory records for the user, sorted by newest first
//      */
//     List<AdventureHistory> findByUserIdOrderByStartedAtDesc(Long userId); 

//     /**
//      * Finds all completed adventures for a user.
//      * 
//      * @param userId the user's ID
//      * @return list of completed adventure history records
//      */
//     List<AdventureHistory> findByUserCompletedAdventureHistories(Long userId);    package Adventure_generator.Repository;
    
//     import java.util.List;
//     import java.util.Optional;
    
//     import org.springframework.data.jpa.repository.JpaRepository;
//     import org.springframework.data.jpa.repository.Query;
//     import org.springframework.data.repository.query.Param;
//     import org.springframework.stereotype.Repository;
    
//     import Adventure_generator.Entity.AdventureHistory;
    
//     /**
//      * Repository for AdventureHistory entity.
//      * 
//      * Provides database access for adventure history records.
//      * Includes custom queries for retrieving user's adventure completion data.
//      */
//     @Repository
//     public interface AdventureHistoryRepository extends JpaRepository<AdventureHistory, Long> {
    
//         /**
//          * Finds all adventure history records for a specific user.
//          * Ordered by most recent first (startedAt descending).
//          * 
//          * @param userId the user's ID
//          * @return list of AdventureHistory records for the user, sorted by newest first
//          */
//         List<AdventureHistory> findByUserIdOrderByStartedAtDesc(Long userId);
    
//         /**
//          * Finds all completed adventures for a user.
//          * 
//          * @param userId the user's ID
//          * @return list of completed adventure history records
//          */
//         @Query("SELECT ah FROM AdventureHistory ah WHERE ah.user.id = :userId AND ah.status = 'completed' ORDER BY ah.completedAt DESC")
//         List<AdventureHistory> findCompletedByUserId(@Param("userId") Long userId);
    
//         /**
//          * Finds count of completed adventures for a user.
//          * Useful for displaying "Adventures Completed: X" statistic.
//          * 
//          * @param userId the user's ID
//          * @return count of completed adventures
//          */
//         @Query("SELECT COUNT(ah) FROM AdventureHistory ah WHERE ah.user.id = :userId AND ah.status = 'completed'")
//         Long countCompletedByUserId(@Param("userId") Long userId);
    
//         /**
//          * Checks if user has already started/tracked a specific adventure.
//          * 
//          * @param userId the user's ID
//          * @param adventureId the adventure's ID
//          * @return Optional containing AdventureHistory if found
//          */
//         Optional<AdventureHistory> findByUserIdAndAdventureId(Long userId, Long adventureId);
    
//         /**
//          * Finds all started but not yet completed adventures for a user.
//          * 
//          * @param userId the user's ID
//          * @return list of in-progress adventure history records
//          */
//         @Query("SELECT ah FROM AdventureHistory ah WHERE ah.user.id = :userId AND ah.status = 'started' ORDER BY ah.startedAt DESC")
//         List<AdventureHistory> findInProgressByUserId(@Param("userId") Long userId);
    
//         /**
//          * Gets average rating for all completed adventures by a user.
//          * 
//          * @param userId the user's ID
//          * @return average rating (null if no ratings exist)
//          */
//         @Query("SELECT AVG(ah.rating) FROM AdventureHistory ah WHERE ah.user.id = :userId AND ah.status = 'completed' AND ah.rating IS NOT NULL")
//         Double getAverageRatingByUserId(@Param("userId") Long userId);
//     }

// }
