package com.codigofacilito.fleet.service.impl;

import com.codigofacilito.fleet.dto.DriverRequestDto;
import com.codigofacilito.fleet.dto.DriverResponseDto;
import com.codigofacilito.fleet.exception.DuplicateResourceException;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.model.Driver;
import com.codigofacilito.fleet.repository.DriverRepository;
import com.codigofacilito.fleet.service.DriverService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para la administración de conductores.
 */
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Mono<DriverResponseDto> createDriver(DriverRequestDto requestDto) {
        return driverRepository.existsByLicenseNumber(requestDto.getLicenseNumber())
                .flatMap(existsLicense -> {
                    if (existsLicense) {
                        return Mono.error(new DuplicateResourceException("El número de licencia '" + requestDto.getLicenseNumber() + "' ya está registrado"));
                    }
                    return driverRepository.existsByEmail(requestDto.getEmail());
                })
                .flatMap(existsEmail -> {
                    if (existsEmail) {
                        return Mono.error(new DuplicateResourceException("El correo '" + requestDto.getEmail() + "' ya está registrado"));
                    }

                    Driver driver = new Driver();
                    driver.setFullName(requestDto.getFullName());
                    driver.setLicenseNumber(requestDto.getLicenseNumber());
                    driver.setPhone(requestDto.getPhone());
                    driver.setEmail(requestDto.getEmail());
                    driver.setActive(requestDto.getActive() != null ? requestDto.getActive() : true);
                    driver.setCreatedAt(LocalDateTime.now());

                    return driverRepository.save(driver);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<DriverResponseDto> getDriverById(Long id) {
        return driverRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Conductor no encontrado con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<DriverResponseDto> getAllDrivers() {
        return driverRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<DriverResponseDto> updateDriver(Long id, DriverRequestDto requestDto) {
        return driverRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Conductor no encontrado con ID: " + id)))
                .flatMap(driver -> {
                    driver.setFullName(requestDto.getFullName());
                    driver.setLicenseNumber(requestDto.getLicenseNumber());
                    driver.setPhone(requestDto.getPhone());
                    driver.setEmail(requestDto.getEmail());
                    if (requestDto.getActive() != null) {
                        driver.setActive(requestDto.getActive());
                    }
                    return driverRepository.save(driver);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteDriver(Long id) {
        return driverRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Conductor no encontrado con ID: " + id)))
                .flatMap(driverRepository::delete);
    }

    private DriverResponseDto mapToDtoWithLinks(Driver driver) {
        DriverResponseDto dto = new DriverResponseDto(
                driver.getId(),
                driver.getFullName(),
                driver.getLicenseNumber(),
                driver.getPhone(),
                driver.getEmail(),
                driver.getActive(),
                driver.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/drivers/" + driver.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/drivers").withRel("all-drivers"));
        return dto;
    }
}
