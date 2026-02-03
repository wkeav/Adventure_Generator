package Adventure_generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import Adventure_generator.Controller.AdventureController;
import Adventure_generator.DTOs.Requests.AdventureRequest;
import Adventure_generator.DTOs.Response.AdventureResponse;
import Adventure_generator.Entity.Adventure;
import Adventure_generator.Entity.User;
import Adventure_generator.Service.AdventureService;

@ExtendWith(MockitoExtension.class)
class AdventureControllerTest {

    @Mock
    private AdventureService adventureService;

    @InjectMocks
    private AdventureController adventureController;

    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("controller@test.com");
        user.setUserName("controllerUser");
        user.setPassword("secret");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private void setAuthentication(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("POST /generate saves adventure and returns response")
    void generateAdventure_success() {
        User user = buildUser();
        setAuthentication(user);

        AdventureRequest request = new AdventureRequest();
        request.setMood("happy");
        request.setWeather("clear");
        request.setLongDistance(false);

        when(adventureService.generateAdventure(eq("happy"), eq("clear"), eq(false))).thenReturn("Picnic");
        Adventure saved = new Adventure("Picnic", user, "happy", "clear", false, false);
        saved.setId(5L);
        when(adventureService.saveAdventure(eq("Picnic"), eq(user), eq("happy"), eq("clear"), eq(false))).thenReturn(saved);

        ResponseEntity<AdventureResponse> response = adventureController.generateAdventure(request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAdventureIdea()).isEqualTo("Picnic");
        assertThat(response.getBody().getAdventureId()).isEqualTo(5L);
        assertThat(response.getBody().getUsername()).isEqualTo("controllerUser");
    }

    @Test
    @DisplayName("GET /history returns adventures for user")
    void getHistory_success() {
        User user = buildUser();
        setAuthentication(user);

        Adventure adv = new Adventure("Hike", user, "energetic", "clear", false, false);
        adv.setId(2L);
        when(adventureService.getUserAdventures(user.getId())).thenReturn(List.of(adv));

        ResponseEntity<List<Adventure>> response = adventureController.getUserAdventureHistory();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getAdventure()).isEqualTo("Hike");
    }
}
