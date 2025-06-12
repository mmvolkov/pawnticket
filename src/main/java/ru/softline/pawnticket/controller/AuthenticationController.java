package ru.softline.pawnticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.softline.pawnticket.dto.LoginResponseDto;
import ru.softline.pawnticket.dto.LoginUserDto;
import ru.softline.pawnticket.dto.RegisterUserDto;
import ru.softline.pawnticket.dto.UserResponseDto;
import ru.softline.pawnticket.entity.User;
import ru.softline.pawnticket.security.JwtService;
import ru.softline.pawnticket.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        UserResponseDto responseDto = UserResponseDto.builder()
                .id(registeredUser.getId())
                .username(registeredUser.getUsername())
                .email(registeredUser.getEmail())
                .firstName(registeredUser.getFirstName())
                .lastName(registeredUser.getLastName())
                .role(registeredUser.getRole())
                .createdAt(registeredUser.getCreatedAt())
                .updatedAt(registeredUser.getUpdatedAt())
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(loginResponse);
    }
}