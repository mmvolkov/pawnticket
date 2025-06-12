package ru.softline.pawnticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softline.pawnticket.dto.LoginUserDto;
import ru.softline.pawnticket.dto.RegisterUserDto;
import ru.softline.pawnticket.entity.User;
import ru.softline.pawnticket.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto input) {
        if (userRepository.existsByUsername(input.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(input.getUsername())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .role(User.Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}