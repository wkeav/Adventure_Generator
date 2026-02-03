package Adventure_generator.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import Adventure_generator.Model.Adventure;

/**
 * Repository for Adventure entity with custom finders by user and mood.
 */
@Repository
public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    List<Adventure> findByUserId(Long userId);

    List<Adventure> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Adventure> findByUserIdAndMood(Long userId, String mood);

    boolean existsByUserId(Long userId);
}