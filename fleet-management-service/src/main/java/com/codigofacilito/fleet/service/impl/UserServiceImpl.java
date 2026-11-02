package com.codigofacilito.fleet.service.impl;

import com.codigofacilito.fleet.dto.UserRequestDto;
import com.codigofacilito.fleet.dto.UserResponseDto;
import com.codigofacilito.fleet.exception.DuplicateResourceException;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.model.AppUser;
import com.codigofacilito.fleet.repository.UserRepository;
import com.codigofacilito.fleet.service.UserService;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para la gestión de usuarios.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserResponseDto> createUser(UserRequestDto requestDto) {
        return userRepository.existsByUsername(requestDto.getUsername())
                .flatMap(existsUser -> {
                    if (existsUser) {
                        return Mono.error(new DuplicateResourceException("El nombre de usuario '" + requestDto.getUsername() + "' ya está registrado"));
                    }
                    return userRepository.existsByEmail(requestDto.getEmail());
                })
                .flatMap(existsEmail -> {
                    if (existsEmail) {
                        return Mono.error(new DuplicateResourceException("El correo electrónico '" + requestDto.getEmail() + "' ya está registrado"));
                    }

                    AppUser user = new AppUser();
                    user.setUsername(requestDto.getUsername());
                    user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                    user.setEmail(requestDto.getEmail());
                    user.setFullName(requestDto.getFullName());
                    user.setRole(requestDto.getRole() != null ? requestDto.getRole() : "ROLE_USER");
                    user.setActive(requestDto.getActive() != null ? requestDto.getActive() : true);
                    user.setCreatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<UserResponseDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado con username: " + username)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<UserResponseDto> updateUser(Long id, UserRequestDto requestDto) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado con ID: " + id)))
                .flatMap(existingUser -> {
                    existingUser.setEmail(requestDto.getEmail());
                    existingUser.setFullName(requestDto.getFullName());
                    if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
                        existingUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                    }
                    if (requestDto.getRole() != null) {
                        existingUser.setRole(requestDto.getRole());
                    }
                    if (requestDto.getActive() != null) {
                        existingUser.setActive(requestDto.getActive());
                    }
                    return userRepository.save(existingUser);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado con ID: " + id)))
                .flatMap(userRepository::delete);
    }

    private UserResponseDto mapToDtoWithLinks(AppUser user) {
        UserResponseDto dto = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/users/" + user.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/users").withRel("all-users"));
        return dto;
    }
}
