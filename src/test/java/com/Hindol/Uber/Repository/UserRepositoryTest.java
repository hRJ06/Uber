package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends AbstractUnitTest{
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("hindol.roy@gmail.com")
                .name("Hindol Roy")
                .build();
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmail_whenEmailPresent_thenReturnUser() {
        /* Arrange */
        String email = "hindol.roy@gmail.com";
        userRepository.save(user);
        /* Act */
        Optional<User> user = userRepository.findByEmail(email);
        /* Assert */
        assertThat(user).isPresent();
        User foundUser = user.get();
        assertThat(foundUser.getName()).isEqualTo("Hindol Roy");
        assertThat(foundUser.getEmail()).isEqualTo("hindol.roy@gmail.com");
    }

    @Test
    void testFindByEmail_whenEmailNotPresent_thenReturnEmptyUser() {
        /* Arrange */
        String email = "hindol.roy@gmail.com";
        /* Act */
        Optional<User> user = userRepository.findByEmail(email);
        /* Assert */
        assertThat(user).isNotPresent();
    }
}