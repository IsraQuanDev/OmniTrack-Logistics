package com.codigofacilito.fleet.service.impl;

import com.codigofacilito.fleet.dto.ShipmentRequestDto;
import com.codigofacilito.fleet.dto.ShipmentResponseDto;
import com.codigofacilito.fleet.exception.DuplicateResourceException;
import com.codigofacilito.fleet.exception.ResourceNotFoundException;
import com.codigofacilito.fleet.model.Shipment;
import com.codigofacilito.fleet.repository.ShipmentRepository;
import com.codigofacilito.fleet.repository.VehicleRepository;
import com.codigofacilito.fleet.service.ShipmentService;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para envíos y despachos logísticos.
 */
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final VehicleRepository vehicleRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, VehicleRepository vehicleRepository) {
        this.shipmentRepository = shipmentRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Mono<ShipmentResponseDto> createShipment(ShipmentRequestDto requestDto) {
        return shipmentRepository.existsByTrackingNumber(requestDto.getTrackingNumber())
                .flatMap(existsTrack -> {
                    if (existsTrack) {
                        return Mono.error(new DuplicateResourceException("El número de guía '" + requestDto.getTrackingNumber() + "' ya existe"));
                    }
                    if (requestDto.getVehicleId() != null) {
                        return vehicleRepository.findById(requestDto.getVehicleId())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("El vehículo con ID " + requestDto.getVehicleId() + " asignado al envío no existe")));
                    }
                    return Mono.empty();
                })
                .then(Mono.defer(() -> {
                    Shipment shipment = new Shipment();
                    shipment.setTrackingNumber(requestDto.getTrackingNumber());
                    shipment.setVehicleId(requestDto.getVehicleId());
                    shipment.setOriginAddress(requestDto.getOriginAddress());
                    shipment.setOriginLatitude(requestDto.getOriginLatitude());
                    shipment.setOriginLongitude(requestDto.getOriginLongitude());
                    shipment.setDestAddress(requestDto.getDestAddress());
                    shipment.setDestLatitude(requestDto.getDestLatitude());
                    shipment.setDestLongitude(requestDto.getDestLongitude());
                    shipment.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "CREATED");
                    shipment.setEstimatedDeliveryTime(requestDto.getEstimatedDeliveryTime());
                    shipment.setCreatedAt(LocalDateTime.now());
                    return shipmentRepository.save(shipment);
                }))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<ShipmentResponseDto> getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Envío no encontrado con ID: " + id)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<ShipmentResponseDto> getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Envío no encontrado con número de guía: " + trackingNumber)))
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Flux<ShipmentResponseDto> getAllShipments(String status) {
        if (status != null && !status.isBlank()) {
            return shipmentRepository.findByStatus(status)
                    .map(this::mapToDtoWithLinks);
        }
        return shipmentRepository.findAll()
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<ShipmentResponseDto> updateShipmentStatus(Long id, String status) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Envío no encontrado con ID: " + id)))
                .flatMap(shipment -> {
                    shipment.setStatus(status);
                    return shipmentRepository.save(shipment);
                })
                .map(this::mapToDtoWithLinks);
    }

    @Override
    public Mono<Void> deleteShipment(Long id) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Envío no encontrado con ID: " + id)))
                .flatMap(shipmentRepository::delete);
    }

    private ShipmentResponseDto mapToDtoWithLinks(Shipment shipment) {
        ShipmentResponseDto dto = new ShipmentResponseDto(
                shipment.getId(),
                shipment.getTrackingNumber(),
                shipment.getVehicleId(),
                shipment.getOriginAddress(),
                shipment.getOriginLatitude(),
                shipment.getOriginLongitude(),
                shipment.getDestAddress(),
                shipment.getDestLatitude(),
                shipment.getDestLongitude(),
                shipment.getStatus(),
                shipment.getEstimatedDeliveryTime(),
                shipment.getCreatedAt()
        );
        dto.add(Link.of("/api/v1/shipments/" + shipment.getId()).withSelfRel());
        dto.add(Link.of("/api/v1/shipments").withRel("all-shipments"));
        if (shipment.getVehicleId() != null) {
            dto.add(Link.of("/api/v1/vehicles/" + shipment.getVehicleId()).withRel("assigned-vehicle"));
        }
        return dto;
    }
}
