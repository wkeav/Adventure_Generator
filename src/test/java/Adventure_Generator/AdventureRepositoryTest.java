package Adventure_generator;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.AdventureRepository;
import Adventure_generator.Repository.UserRepository;

@DataJpaTest
class AdventureRepositoryTest {

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    private UserRepository userRepository;

    private User buildUser() {
        User user = new User();
        user.setEmail("repo@test.com");
        user.setUserName("repoUser");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    private Adventure buildAdventure(User user, String adventureText, String mood, String weather, boolean longDistance) {
        return adventureRepository.save(new Adventure(adventureText, user, mood, weather, longDistance, false));
    }

    @Test
    @DisplayName("findByUserId returns adventures for the user")
    void findByUserId_returnsResults() {
        User user = buildUser();
        Adventure adv1 = buildAdventure(user, "Hike", "energetic", "clear", false);
        Adventure adv2 = buildAdventure(user, "Movie", "relaxed", "rain", true);

        List<Adventure> results = adventureRepository.findByUserId(user.getId());

        assertThat(results).hasSize(2);
        assertThat(results).extracting(Adventure::getAdventure).containsExactlyInAnyOrder(
            adv1.getAdventure(),
            adv2.getAdventure()
        );
    }

    @Test
    @DisplayName("existsByUserId returns true when user has adventures")
    void existsByUserId_returnsTrue() {
        User user = buildUser();
        buildAdventure(user, "Tennis", "happy", "clear", false);

        boolean exists = adventureRepository.existsByUserId(user.getId());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("findByUserIdAndMood filters by mood")
    void findByUserIdAndMood_filtersCorrectly() {
        User user = buildUser();
        buildAdventure(user, "Hike", "energetic", "clear", false);
        Adventure target = buildAdventure(user, "Read", "relaxed", "rain", true);

        List<Adventure> results = adventureRepository.findByUserIdAndMood(user.getId(), "relaxed");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAdventure()).isEqualTo(target.getAdventure());
    }
}
