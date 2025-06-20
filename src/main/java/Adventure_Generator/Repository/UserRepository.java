package Adventure_Generator.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Adventure_Generator.Model.User;

/*
 * CRUD
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
