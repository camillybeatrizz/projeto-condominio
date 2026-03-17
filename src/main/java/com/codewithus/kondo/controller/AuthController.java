package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.auth.LoginRequestDTO;
import com.codewithus.kondo.dto.auth.LoginResponseDTO;
import com.codewithus.kondo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto){
        return authService.login(dto);
    }
}
