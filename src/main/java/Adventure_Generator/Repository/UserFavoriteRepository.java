package Adventure_generator.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Entity.UserFavourite;

/**
 * Repository for UserFavourite entity managing user-adventure favorite relationships.
 * 
 * Provides Spring Data JPA derived query methods for:
 * - Finding favorites by user and adventure combination
 * - Retrieving all favorites for a specific user
 * - Deleting favorite relationships
 * 
 * @see UserFavourite
 */
@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavourite, Long> {
    
    /**
     * Find a specific favorite relationship between a user and an adventure.
     * @param user The user entity
     * @param adventure The adventure entity
     * @return Optional containing the UserFavourite if exists, empty otherwise
     */
    Optional<UserFavourite> findByUserAndAdventure(User user, Adventure adventure);
    
    /**
     * Find all favorites for a specific user.
     * @param userId The user's ID
     * @return List of all favorites for the user, empty list if none
     */
    List<UserFavourite> findAllByUserId(Long userId);
    
    /**
     * Delete a favorite relationship between a user and an adventure.
     * @param user The user entity
     * @param adventure The adventure entity
     */
    void deleteByUserAndAdventure(User user, Adventure adventure);
}
