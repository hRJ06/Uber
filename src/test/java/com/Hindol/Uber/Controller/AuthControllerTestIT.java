package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.LoginRequestDTO;
import com.Hindol.Uber.DTO.OnboardDriverDTO;
import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Repository.DriverRepository;
import com.Hindol.Uber.Repository.RiderRepository;
import com.Hindol.Uber.Repository.WalletRepository;
import com.Hindol.Uber.Security.JWTService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private RiderRepository riderRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    private SignUpDTO signUpDTO;
    private LoginRequestDTO loginRequestDTO, invalidLoginRequestDTO;

    @BeforeEach
    void setUp() {
        signUpDTO = SignUpDTO.builder()
                .name("Rider")
                .email("rider@gmail.com")
                .password("rider")
                .build();
        user = User.builder()
                .id(2L)
                .name("Rider")
                .email("rider@gmail.com")
                .password("rider")
                .build();
        loginRequestDTO = LoginRequestDTO.builder()
                .email("rider@gmail.com")
                .password("rider")
                .build();
        invalidLoginRequestDTO = LoginRequestDTO.builder()
                .email("rider@gmail.com")
                .password("ride")
                .build();
        walletRepository.deleteAll();
        riderRepository.deleteAll();
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSignUp_whenSuccess() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value(signUpDTO.getName()))
                .andExpect(jsonPath("$.data.email").value(signUpDTO.getEmail()));
    }

    @Test
    @WithMockUser(username = "driver@gmail.com", roles = {"ADMIN"})
    void testOnBoardNewDriver_whenNoDriverExist_thenFailure() throws Exception {
        OnboardDriverDTO onboardDriverDTO = OnboardDriverDTO.builder()
                .vehicleId("3897")
                .build();
        mockMvc.perform(post("/auth/onboardNewDriver/45")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onboardDriverDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.apiError.message").value("No User found with ID : 45"));
    }
    @Test
    @WithMockUser(username = "driver@gmail.com", roles = {"DRIVER"})
    void testOnBoardNewDriver_whenUnauthorizedRole_Failure() throws Exception {
        if (!userRepository.existsById(1L)) {
            userRepository.save(user);
        }
        OnboardDriverDTO onboardDriverDTO = OnboardDriverDTO.builder()
                .vehicleId("3897")
                .build();
        mockMvc.perform(post("/auth/onboardNewDriver/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onboardDriverDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.apiError.message").value("Access Denied"));
    }


    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void testOnBoardNewDriver_whenSuccess() throws Exception {
        /* Arrange */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        OnboardDriverDTO onboardDriverDTO = OnboardDriverDTO.builder()
                .vehicleId("3897")
                .build();

        mockMvc.perform(post("/auth/onboardNewDriver/{userId}", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onboardDriverDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testLoginUser_whenSuccess() throws Exception {
        /* Arrange */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void testLoginUser_whenBadCredentials_thenFailure() throws Exception {
        /* Arrange */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequestDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.apiError.message").value("Bad credentials"));
    }

    @Test
    void testRefreshToken_whenSuccess() throws Exception {
        /* Arrange */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String refreshToken = jwtService.generateRefreshToken(savedUser);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        mockMvc.perform(post("/auth/refresh")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void testRefreshToken_whenInvalidToken_thenFailure() throws Exception {
        /* String refreshToken = jwtService.generateRefreshToken(user); */
        Cookie cookie = new Cookie("refreshToken", "refreshToken");
        mockMvc.perform(post("/auth/refresh")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
