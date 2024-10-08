package com.Hindol.Uber.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    private String name;
    @Email
    private String email;
    private String password;
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private Long phoneNo;
}
