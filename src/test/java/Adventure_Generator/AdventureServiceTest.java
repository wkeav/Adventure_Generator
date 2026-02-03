package Adventure_generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Repository.AdventureRepository;
import Adventure_generator.Service.AdventureService;

@ExtendWith(MockitoExtension.class)
class AdventureServiceTest {

    @Mock
    private AdventureRepository adventureRepository;

    @InjectMocks
    private AdventureService adventureService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("service@test.com");
        user.setUserName("serviceUser");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("saveAdventure persists and returns adventure")
    void saveAdventure_persists() {
        when(adventureRepository.save(any(Adventure.class))).thenAnswer(invocation -> {
            Adventure adv = invocation.getArgument(0);
            adv.setId(10L);
            return adv;
        });

        Adventure saved = adventureService.saveAdventure("Hike", user, "energetic", "clear", false);

        assertThat(saved.getId()).isEqualTo(10L);
        assertThat(saved.getAdventure()).isEqualTo("Hike");
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getMood()).isEqualTo("energetic");
        assertThat(saved.getWeather()).isEqualTo("clear");
        assertThat(saved.getIsLongDistance()).isFalse();
        verify(adventureRepository, times(1)).save(any(Adventure.class));
    }

    @Test
    @DisplayName("getUserAdventures delegates to repository")
    void getUserAdventures_returnsList() {
        Adventure adv = new Adventure("Read", user, "relaxed", "rain", true, false);
        when(adventureRepository.findByUserIdOrderByCreatedAtDesc(user.getId())).thenReturn(List.of(adv));

        List<Adventure> result = adventureService.getUserAdventures(user.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAdventure()).isEqualTo("Read");
        verify(adventureRepository, times(1)).findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}
