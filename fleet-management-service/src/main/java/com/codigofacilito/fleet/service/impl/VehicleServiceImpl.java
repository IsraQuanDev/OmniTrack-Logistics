package com.codigofacilito.fleet.service.impl;

import com.codigofacilito.fleet.dto.VehicleRequestDto;
import com.codigofacilito.fleet.dto.VehicleResponseDto;
import com.codigofacilito.fleet.exception.DuplicateResourceException;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.model.Vehicle;
import com.codigofacilito.fleet.repository.VehicleRepository;
import com.codigofacilito.fleet.service.VehicleService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para la flota vehicular.
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Mono<VehicleResponseDto> createVehicle(VehicleRequestDto requestDto) {
        return vehicleRepository.existsByVin(requestDto.getVin())
                .flatMap(existsVin -> {
                    if (existsVin) {
                        return Mono.error(new DuplicateResourceException("El número VIN '" + requestDto.getVin() + "' ya está registrado"));
                    }
                    return vehicleRepository.existsByLicensePlate(requestDto.getLicensePlate());
                })
                .flatMap(existsPlate -> {
                    if (existsPlate) {
                        return Mono.error(new DuplicateResourceException("La placa '" + requestDto.getLicensePlate() + "' ya está registrada"));
                    }

                    Vehicle vehicle = new Vehicle();
                    vehicle.setVin(requestDto.getVin());
                    vehicle.setLicensePlate(requestDto.getLicensePlate());
                    vehicle.setBrand(requestDto.getBrand());
                    vehicle.setModel(requestDto.getModel());
                    vehicle.setYearManufactured(requestDto.getYearManufactured());
                    vehicle.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "ACTIVE");
                    vehicle.setCapacityKg(requestDto.getCapacityKg());
                    vehicle.setAssignedDriverId(requestDto.getAssignedDriverId());
                    vehicle.setCreatedAt(LocalDateTime.now());

                    return vehicleRepository.save(vehicle);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<VehicleResponseDto> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehículo no encontrado con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<VehicleResponseDto> getAllVehicles(String status) {
        if (status != null && !status.isBlank()) {
            return vehicleRepository.findByStatus(status)
                    .map(this::mapToDtoWithLinks);
        }
        return vehicleRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<VehicleResponseDto> updateVehicle(Long id, VehicleRequestDto requestDto) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehículo no encontrado con ID: " + id)))
                .flatMap(vehicle -> {
                    vehicle.setVin(requestDto.getVin());
                    vehicle.setLicensePlate(requestDto.getLicensePlate());
                    vehicle.setBrand(requestDto.getBrand());
                    vehicle.setModel(requestDto.getModel());
                    vehicle.setYearManufactured(requestDto.getYearManufactured());
                    if (requestDto.getStatus() != null) {
                        vehicle.setStatus(requestDto.getStatus());
                    }
                    vehicle.setCapacityKg(requestDto.getCapacityKg());
                    vehicle.setAssignedDriverId(requestDto.getAssignedDriverId());
                    return vehicleRepository.save(vehicle);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<VehicleResponseDto> updateVehicleStatus(Long id, String status) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehículo no encontrado con ID: " + id)))
                .flatMap(vehicle -> {
                    vehicle.setStatus(status);
                    return vehicleRepository.save(vehicle);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteVehicle(Long id) {
        return vehicleRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Vehículo no encontrado con ID: " + id)))
                .flatMap(vehicleRepository::delete);
    }

    private VehicleResponseDto mapToDtoWithLinks(Vehicle vehicle) {
        VehicleResponseDto dto = new VehicleResponseDto(
                vehicle.getId(),
                vehicle.getVin(),
                vehicle.getLicensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYearManufactured(),
                vehicle.getStatus(),
                vehicle.getCapacityKg(),
                vehicle.getAssignedDriverId(),
                vehicle.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/vehicles/" + vehicle.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/vehicles").withRel("all-vehicles"));
        dto.add(Link.of("/api/v1/telemetry/stream/" + vehicle.getId()).withRel("telemetry-stream"));
        dto.add(Link.of("/api/v1/routes/history/" + vehicle.getId()).withRel("route-history"));
        return dto;
    }
}
