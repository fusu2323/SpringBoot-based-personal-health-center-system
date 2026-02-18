package com.healthy.service.impl;

import com.healthy.entity.DietRecord;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SleepRecord;
import com.healthy.entity.SportRecord;
import com.healthy.mapper.DietRecordMapper;
import com.healthy.mapper.HealthIndicatorMapper;
import com.healthy.mapper.SleepRecordMapper;
import com.healthy.mapper.SportRecordMapper;
import com.healthy.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HealthServiceImpl implements HealthService {

    @Autowired
    private HealthIndicatorMapper healthIndicatorMapper;
    @Autowired
    private SportRecordMapper sportRecordMapper;
    @Autowired
    private DietRecordMapper dietRecordMapper;
    @Autowired
    private SleepRecordMapper sleepRecordMapper;

    @Override
    public List<HealthIndicator> getIndicatorList(Long userId) {
        return healthIndicatorMapper.selectByUserId(userId);
    }

    @Override
    public void saveIndicator(HealthIndicator record) {
        if (record.getId() == null) {
            record.setCreateTime(LocalDateTime.now());
            healthIndicatorMapper.insert(record);
        } else {
            healthIndicatorMapper.update(record);
        }
    }

    @Override
    public void deleteIndicator(Long id) {
        healthIndicatorMapper.deleteById(id);
    }

    @Override
    public List<SportRecord> getSportList(Long userId) {
        return sportRecordMapper.selectByUserId(userId);
    }

    @Override
    public void saveSport(SportRecord record) {
        if (record.getId() == null) {
            record.setCreateTime(LocalDateTime.now());
            sportRecordMapper.insert(record);
        } else {
            sportRecordMapper.update(record);
        }
    }

    @Override
    public void deleteSport(Long id) {
        sportRecordMapper.deleteById(id);
    }

    @Override
    public List<DietRecord> getDietList(Long userId) {
        return dietRecordMapper.selectByUserId(userId);
    }

    @Override
    public void saveDiet(DietRecord record) {
        if (record.getId() == null) {
            record.setCreateTime(LocalDateTime.now());
            dietRecordMapper.insert(record);
        } else {
            dietRecordMapper.update(record);
        }
    }

    @Override
    public void deleteDiet(Long id) {
        dietRecordMapper.deleteById(id);
    }

    @Override
    public List<SleepRecord> getSleepList(Long userId) {
        return sleepRecordMapper.selectByUserId(userId);
    }

    @Override
    public void saveSleep(SleepRecord record) {
        if (record.getId() == null) {
            record.setCreateTime(LocalDateTime.now());
            sleepRecordMapper.insert(record);
        } else {
            sleepRecordMapper.update(record);
        }
    }

    @Override
    public void deleteSleep(Long id) {
        sleepRecordMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getAllIndicators() {
        return healthIndicatorMapper.selectAllWithUser();
    }

    @Override
    public List<Map<String, Object>> getAllSports() {
        return sportRecordMapper.selectAllWithUser();
    }

    @Override
    public List<Map<String, Object>> getAllDiets() {
        return dietRecordMapper.selectAllWithUser();
    }

    @Override
    public List<Map<String, Object>> getAllSleeps() {
        return sleepRecordMapper.selectAllWithUser();
    }
}
