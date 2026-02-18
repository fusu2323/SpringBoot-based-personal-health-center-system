package com.healthy.service;

import reactor.core.publisher.Flux;

public interface AiService {
    /**
     * 流式对话接口
     */
    Flux<String> streamChat(Long userId, String message);

    /**
     * 获取或生成每日健康简报
     */
    String getDailyReport(Long userId);

    /**
     * 生成健康计划 (同步)
     */
    String generatePlan(Long userId, String goal);

    /**
     * 生成健康计划 (流式)
     */
    Flux<String> generatePlanStream(Long userId, String goal);
}
