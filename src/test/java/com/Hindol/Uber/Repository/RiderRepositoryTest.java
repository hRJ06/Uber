package com.Hindol.Uber.Repository;

import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

class RiderRepositoryTest extends AbstractUnitTest{
    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Rider rider;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("hindol.roy@gmail.com")
                .name("Hindol Roy")
                .build();
        rider = Rider.builder()
                .user(user)
                .rating(0.0)
                .build();
        riderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindByUser_whenUserPresent_thenReturnUser() {
        /* Arrange */
        userRepository.save(user);
        riderRepository.save(rider);
        /* Act */
        Optional<Rider> rider = riderRepository.findByUser(user);
        /* Assert */
        assertThat(rider).isPresent();
        Rider foundRider = rider.get();
        assertThat(foundRider.getUser()).isEqualTo(user);
        assertThat(foundRider.getRating()).isEqualTo(0.0);
    }

    @Test
    void testFindByUser_whenUserNotPresent_thenReturnEmptyUser() {
        /* Arrange */
        userRepository.save(user);
        /* Act */
        Optional<Rider> rider = riderRepository.findByUser(user);
        /* Assert */
        assertThat(rider).isNotPresent();
    }
}