package com.codigofacilito.gps.dto;

import java.util.List;

/**
 * DTO de confirmación tras procesar una ingesta en lote.
 */
public class BatchTelemetryResponseDto {

    private Integer totalIngested;
    private String status;
    private List<Long> recordIds;

    public BatchTelemetryResponseDto() {
    }

    public BatchTelemetryResponseDto(Integer totalIngested, String status, List<Long> recordIds) {
        this.totalIngested = totalIngested;
        this.status = status;
        this.recordIds = recordIds;
    }

    public Integer getTotalIngested() {
        return totalIngested;
    }

    public void setTotalIngested(Integer totalIngested) {
        this.totalIngested = totalIngested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getRecordIds() {
        return recordIds;
    }

    public void setRecordIds(List<Long> recordIds) {
        this.recordIds = recordIds;
    }
}
