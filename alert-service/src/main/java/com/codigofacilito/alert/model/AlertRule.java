package com.codigofacilito.alert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entidad de dominio AlertRule (Regla de negocio para detección de anomalías y umbrales).
 */
@Table("alert_rules")
public class AlertRule {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("rule_type")
    private String ruleType;

    @Column("threshold_value")
    private Double thresholdValue;

    @Column("severity")
    private String severity;

    @Column("active")
    private Boolean active = true;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AlertRule() {
    }

    public AlertRule(Long id, String name, String ruleType, Double thresholdValue, String severity, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.ruleType = ruleType;
        this.thresholdValue = thresholdValue;
        this.severity = severity;
        this.active = active != null ? active : true;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
