package com.codigofacilito.fleet.service;

import com.codigofacilito.fleet.dto.UserRequestDto;
import com.codigofacilito.fleet.dto.UserResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de servicio para la gestión de usuarios (Punto Extra 2: CRUD de Usuario).
 */
public interface UserService {

    Mono<UserResponseDto> createUser(UserRequestDto requestDto);

    Mono<UserResponseDto> getUserById(Long id);

    Mono<UserResponseDto> getUserByUsername(String username);

    Flux<UserResponseDto> getAllUsers();

    Mono<UserResponseDto> updateUser(Long id, UserRequestDto requestDto);

    Mono<Void> deleteUser(Long id);
}
