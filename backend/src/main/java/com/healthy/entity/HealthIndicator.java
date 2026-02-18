package com.healthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HealthIndicator {
    private Long id;
    private Long userId;
    private String bloodPressure;
    private Double bloodSugar;
    private Integer heartRate;
    private Double temperature;
    private Double weight;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
