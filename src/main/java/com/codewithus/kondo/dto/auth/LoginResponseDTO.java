package com.codewithus.kondo.dto.auth;

public record LoginResponseDTO (
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
