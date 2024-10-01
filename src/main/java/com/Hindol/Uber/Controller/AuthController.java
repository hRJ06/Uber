package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.SignUpDTO;
import com.Hindol.Uber.DTO.UserDTO;
import com.Hindol.Uber.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        return ResponseEntity.ok(authService.signUp(signUpDTO));
    }
}
