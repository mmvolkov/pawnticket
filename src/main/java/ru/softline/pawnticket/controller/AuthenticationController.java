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
import ru.softline.pawnticket.service.MapperService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final MapperService mapperService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(mapperService.toUserResponseDto(registeredUser));
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