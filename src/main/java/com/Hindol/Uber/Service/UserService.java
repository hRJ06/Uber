package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.UserDTO;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Exception.ResourceNotFoundException;
import com.Hindol.Uber.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No User found with Email : " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User found with ID : " + id));
    }

    @Transactional
    public UserDTO updateUserById(Long id, Map<String, Object> fieldsToBeUpdated) {
        log.info("Updating User with ID : {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User found with ID : " + id));
        fieldsToBeUpdated.forEach((field, value) -> {
            Field fieldToBeChanged = ReflectionUtils.findRequiredField(User.class, field);
            fieldToBeChanged.setAccessible(true);
            ReflectionUtils.setField(fieldToBeChanged, user, value);
        });
        User updatedUser = userRepository.save(user);
        log.info("Successfully updated User with ID : {}", id);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
