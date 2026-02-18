package com.healthy.service;

import com.healthy.entity.DietRecord;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SleepRecord;
import com.healthy.entity.SportRecord;

import java.util.List;
import java.util.Map;

public interface HealthService {
    // Indicator
    List<HealthIndicator> getIndicatorList(Long userId);
    void saveIndicator(HealthIndicator record);
    void deleteIndicator(Long id);

    // Sport
    List<SportRecord> getSportList(Long userId);
    void saveSport(SportRecord record);
    void deleteSport(Long id);

    // Diet
    List<DietRecord> getDietList(Long userId);
    void saveDiet(DietRecord record);
    void deleteDiet(Long id);

    // Sleep
    List<SleepRecord> getSleepList(Long userId);
    void saveSleep(SleepRecord record);
    void deleteSleep(Long id);

    // Admin (All records with user info)
    List<Map<String, Object>> getAllIndicators();
    List<Map<String, Object>> getAllSports();
    List<Map<String, Object>> getAllDiets();
    List<Map<String, Object>> getAllSleeps();
}
