package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.dto.auth.LoginRequestDTO;
import com.codewithus.kondo.dto.auth.LoginResponseDTO;
import com.codewithus.kondo.security.JwtService;
import com.codewithus.kondo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(),dto.senha())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpiration()
        );
    }
}
