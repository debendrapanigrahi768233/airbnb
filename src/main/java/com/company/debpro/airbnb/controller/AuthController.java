package com.company.debpro.airbnb.controller;


import com.company.debpro.airbnb.dto.LogInDto;
import com.company.debpro.airbnb.dto.LogInResponseDto;
import com.company.debpro.airbnb.dto.SignUpRequestDto;
import com.company.debpro.airbnb.dto.UserDto;
import com.company.debpro.airbnb.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(authService.signUp(signUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInDto loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String[] tokens = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", tokens[1]);
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new LogInResponseDto(tokens[0]));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LogInResponseDto> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        //new access token
        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LogInResponseDto(accessToken));
    }

}
