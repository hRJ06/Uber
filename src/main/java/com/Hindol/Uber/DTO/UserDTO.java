package com.Hindol.Uber.DTO;

import com.Hindol.Uber.Entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Long phoneNo;
    private Set<Role> roles;
}
