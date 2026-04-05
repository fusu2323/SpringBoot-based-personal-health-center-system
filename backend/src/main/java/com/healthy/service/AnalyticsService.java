package com.healthy.service;

import com.healthy.entity.AnalyticsData;
import java.time.LocalDate;

public interface AnalyticsService {
    AnalyticsData getIndicatorStats(Long userId, LocalDate startDate, LocalDate endDate);
    AnalyticsData getSportStats(Long userId, LocalDate startDate, LocalDate endDate);
    AnalyticsData getDietStats(Long userId, LocalDate startDate, LocalDate endDate);
    AnalyticsData getSleepStats(Long userId, LocalDate startDate, LocalDate endDate);
}
