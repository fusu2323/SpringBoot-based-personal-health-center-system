package com.healthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SportRecord {
    private Long id;
    private Long userId;
    private String sportType;
    private Integer duration;
    private Double calorie;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
