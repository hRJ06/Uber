package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.OnboardDriverDTO;
import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.Entity.Enum.Role;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;


@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;
    private SignUpDTO signUpDTO;
    private User user; /* RIDER */

    @BeforeEach
    void setUp() {
            signUpDTO = SignUpDTO.builder()
                    .name("Rider")
                    .email("rider@gmail.com")
                    .password("rider")
                    .build();
            user = User.builder()
                    .name("Rider")
                    .email("rider@gmail.com")
                    .password("ride")
                    .roles(Set.of(Role.RIDER))
                    .build();
    }

    @Test
    void testSignUp_whenSuccess() {
        webTestClient.post()
                .uri("/auth/signup")
                .bodyValue(signUpDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo(signUpDTO.getName())
                .jsonPath("$.data.email").isEqualTo(signUpDTO.getEmail());
    }

    /*  @Test
        @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    */
    void testOnBoardNewDriver_whenSuccess() {
        if(!userRepository.existsById(1L)) {
            userRepository.save(user);
        }
        OnboardDriverDTO onboardDriverDTO = OnboardDriverDTO.builder()
                .vehicleId("3897")
                .build();
        webTestClient.post()
                .uri("/auth/onboardNewDriver/1")
                .bodyValue(onboardDriverDTO)
                .exchange()
                .expectStatus().isCreated();
    }
}