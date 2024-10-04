package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.DTO.UserDTO;
import com.Hindol.Uber.Entity.Enum.Role;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Repository.UserRepository;
import com.Hindol.Uber.Security.JWTService;
import com.Hindol.Uber.Service.DriverService;
import com.Hindol.Uber.Service.RiderService;
import com.Hindol.Uber.Service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementationTestIT {
    @Spy
    private ModelMapper modelMapper;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RiderService riderService;
    @Mock
    private WalletService walletService;
    @Mock
    private DriverService driverService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;

    private User user;

    @InjectMocks
    private AuthServiceImplementation authServiceImplementation;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .roles(Set.of(Role.RIDER))
                .build();
    }

    @Test
    void testLogin_whenSuccess() {
        /* Arrange */
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        /* Act */
        String[] tokens = authServiceImplementation.login(user.getEmail(), user.getPassword());
        /* Assert */
        assertThat(tokens).hasSize(2);
        assertThat(tokens[0]).isEqualTo("accessToken");
        assertThat(tokens[1]).isEqualTo("refreshToken");
    }

    @Test
    void testSignUp_whenSuccess() {
        /* Arrange */
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        /* Act */
        SignUpDTO signUpDTO = SignUpDTO.builder()
                .name("Test")
                .email("test@gmail.com")
                .password("password")
                .build();
        UserDTO userDTO = authServiceImplementation.signUp(signUpDTO);
        /* Assert */
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getEmail()).isEqualTo(signUpDTO.getEmail());
        verify(riderService, times(1)).createNewDriver(any(User.class));
        verify(walletService, times(1)).createNewWallet(any(User.class));
    }
}