package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.auth.LoginRequestDTO;
import com.codewithus.kondo.dto.auth.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
}
