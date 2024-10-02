package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.DTO.UserDTO;

public interface AuthService {

    String[] login(String email, String password);
    UserDTO signUp(SignUpDTO signUpDTO);
    DriverDTO onboardNewDriver(Long userId, String vehicleId);
    String refreshToken(String refreshToken);
}
