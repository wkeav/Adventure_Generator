package Adventure_generator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Adventure_generator.Entity.User;

/*
 * CRUD operations for database implement for User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    boolean existsByEmail(String email);
}
