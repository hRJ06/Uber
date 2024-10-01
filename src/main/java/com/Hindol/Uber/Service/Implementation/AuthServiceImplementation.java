package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.DTO.UserDTO;
import com.Hindol.Uber.Entity.Enum.Role;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Exception.RuntimeConflictException;
import com.Hindol.Uber.Repository.UserRepository;
import com.Hindol.Uber.Service.AuthService;
import com.Hindol.Uber.Service.RiderService;
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

    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) {
        userRepository.findByEmail(signUpDTO.getEmail()).orElseThrow(() -> new RuntimeConflictException("User already exists with Email : " + signUpDTO.getEmail()));
        User mappedUser = modelMapper.map(signUpDTO, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);
        riderService.createNewDriver(savedUser);
        /* TODO: Add wallet related service */
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public DriverDTO onboardNewDriver(String userId) {
        return null;
    }
}