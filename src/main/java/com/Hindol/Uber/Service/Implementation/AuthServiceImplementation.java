package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.DTO.UserDTO;
import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Enum.Role;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Exception.RuntimeConflictException;
import com.Hindol.Uber.Repository.UserRepository;
import com.Hindol.Uber.Service.AuthService;
import com.Hindol.Uber.Service.DriverService;
import com.Hindol.Uber.Service.RiderService;
import com.Hindol.Uber.Service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    @Transactional
    public UserDTO signUp(SignUpDTO signUpDTO) {
        User user = userRepository.findByEmail(signUpDTO.getEmail()).orElse(null);
        if(user != null) {
            throw new RuntimeConflictException("User already exists with Email : " + signUpDTO.getEmail());
        }
        User mappedUser = modelMapper.map(signUpDTO, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);
        riderService.createNewDriver(savedUser);
        walletService.createNewWallet(savedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public DriverDTO onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User found with ID : " + userId));
        if(user.getRoles().contains(Role.DRIVER)) throw new RuntimeConflictException("User with ID : " + userId + " is already a driver");
        Driver driver = Driver.builder()
                .user(user)
                .vehicleId(vehicleId)
                .rating(0.0)
                .available(true)
                .build();
        user.getRoles().add(Role.DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(driver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }
}
