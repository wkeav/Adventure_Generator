package Adventure_generator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Adventure_generator.Entity.User;

/**
 * Repository for User entity providing authentication and user lookup operations.
 * 
 * Provides Spring Data JPA derived query methods for:
 * - Finding users by email (for authentication)
 * - Finding users by username
 * - Checking email existence (for registration validation)
 * 
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by email address.
     * @param email The email to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by username.
     * @param userName The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByUserName(String userName);
    
    /**
     * Check if an email address is already registered.
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
