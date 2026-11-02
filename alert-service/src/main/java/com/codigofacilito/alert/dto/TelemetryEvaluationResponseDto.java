package com.codigofacilito.alert.dto;

import java.util.List;

/**
 * DTO con el resumen de la evaluación de telemetría y alertas disparadas.
 */
public class TelemetryEvaluationResponseDto {

    private Long vehicleId;
    private String status; // NORMAL, ANOMALY_DETECTED
    private Integer alertsTriggeredCount;
    private List<AlertResponseDto> triggeredAlerts;

    public TelemetryEvaluationResponseDto() {
    }

    public TelemetryEvaluationResponseDto(Long vehicleId, String status, Integer alertsTriggeredCount, List<AlertResponseDto> triggeredAlerts) {
        this.vehicleId = vehicleId;
        this.status = status;
        this.alertsTriggeredCount = alertsTriggeredCount;
        this.triggeredAlerts = triggeredAlerts;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAlertsTriggeredCount() {
        return alertsTriggeredCount;
    }

    public void setAlertsTriggeredCount(Integer alertsTriggeredCount) {
        this.alertsTriggeredCount = alertsTriggeredCount;
    }

    public List<AlertResponseDto> getTriggeredAlerts() {
        return triggeredAlerts;
    }

    public void setTriggeredAlerts(List<AlertResponseDto> triggeredAlerts) {
        this.triggeredAlerts = triggeredAlerts;
    }
}
