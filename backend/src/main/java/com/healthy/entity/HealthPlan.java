package com.healthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthPlan {
    private Long id;
    private Long userId;
    private String goal; // 目标描述
    private String content; // JSON 格式的计划详情
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
