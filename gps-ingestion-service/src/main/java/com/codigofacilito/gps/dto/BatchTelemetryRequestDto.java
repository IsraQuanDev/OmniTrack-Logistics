package com.codigofacilito.gps.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO para la ingesta masiva (batch) de telemetría de flotas.
 */
public class BatchTelemetryRequestDto {

    @NotEmpty(message = "La lista de lecturas de telemetría no puede estar vacía")
    @Valid
    private List<TelemetryRequestDto> records;

    public BatchTelemetryRequestDto() {
    }

    public BatchTelemetryRequestDto(List<TelemetryRequestDto> records) {
        this.records = records;
    }

    public List<TelemetryRequestDto> getRecords() {
        return records;
    }

    public void setRecords(List<TelemetryRequestDto> records) {
        this.records = records;
    }
}
