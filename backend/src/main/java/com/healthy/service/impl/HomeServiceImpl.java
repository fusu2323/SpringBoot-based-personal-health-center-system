package com.healthy.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SportRecord;
import com.healthy.entity.SysNotice;
import com.healthy.mapper.*;
import com.healthy.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private HealthIndicatorMapper healthIndicatorMapper;
    @Autowired
    private SportRecordMapper sportRecordMapper;
    @Autowired
    private SysNoticeMapper sysNoticeMapper;
    @Autowired
    private DietRecordMapper dietRecordMapper;
    @Autowired
    private SleepRecordMapper sleepRecordMapper;

    @Override
    public Map<String, Object> getHomeStats(Long userId) {
        Map<String, Object> res = new HashMap<>();

        // 1. 获取近7天身体指标（血压）
        List<HealthIndicator> indicators = healthIndicatorMapper.selectByUserId(userId);
        // 取最近7条并按日期升序
        List<HealthIndicator> recent7Indicators = indicators.stream()
                .limit(7)
                .sorted(Comparator.comparing(HealthIndicator::getRecordDate))
                .collect(Collectors.toList());
        
        List<String> indicatorDates = new ArrayList<>();
        List<Integer> highPressures = new ArrayList<>();
        List<Integer> lowPressures = new ArrayList<>();
        
        for (HealthIndicator h : recent7Indicators) {
            indicatorDates.add(h.getRecordDate().toString());
            // 解析血压 "120/80"
            if (h.getBloodPressure() != null && h.getBloodPressure().contains("/")) {
                String[] parts = h.getBloodPressure().split("/");
                if (parts.length == 2 && NumberUtil.isNumber(parts[0]) && NumberUtil.isNumber(parts[1])) {
                    highPressures.add(Integer.parseInt(parts[0]));
                    lowPressures.add(Integer.parseInt(parts[1]));
                } else {
                    highPressures.add(0);
                    lowPressures.add(0);
                }
            } else {
                highPressures.add(0);
                lowPressures.add(0);
            }
        }
        
        Map<String, Object> bloodPressureData = new HashMap<>();
        bloodPressureData.put("dates", indicatorDates);
        bloodPressureData.put("high", highPressures);
        bloodPressureData.put("low", lowPressures);
        res.put("bloodPressure", bloodPressureData);

        // 2. 获取近7天运动时长
        List<SportRecord> sports = sportRecordMapper.selectByUserId(userId);
        // 按日期分组求和
        Map<LocalDate, Integer> sportMap = sports.stream()
                .collect(Collectors.groupingBy(SportRecord::getRecordDate, Collectors.summingInt(SportRecord::getDuration)));
        
        List<String> sportDates = new ArrayList<>();
        List<Integer> sportDurations = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            sportDates.add(date.toString());
            sportDurations.add(sportMap.getOrDefault(date, 0));
        }
        
        Map<String, Object> sportData = new HashMap<>();
        sportData.put("dates", sportDates);
        sportData.put("durations", sportDurations);
        res.put("sport", sportData);

        // 3. 今日健康提醒状态
        Map<String, Boolean> todayStatus = new HashMap<>();
        String todayStr = today.toString();
        
        boolean hasIndicator = indicators.stream().anyMatch(i -> i.getRecordDate().equals(today));
        boolean hasSport = sports.stream().anyMatch(s -> s.getRecordDate().equals(today));
        boolean hasDiet = dietRecordMapper.selectByUserId(userId).stream().anyMatch(d -> d.getRecordDate().equals(today));
        boolean hasSleep = sleepRecordMapper.selectByUserId(userId).stream().anyMatch(s -> s.getRecordDate().equals(today));
        
        todayStatus.put("indicator", hasIndicator);
        todayStatus.put("sport", hasSport);
        todayStatus.put("diet", hasDiet);
        todayStatus.put("sleep", hasSleep);
        
        res.put("todayStatus", todayStatus);

        return res;
    }

    @Override
    public List<SysNotice> getTopNotices() {
        return sysNoticeMapper.selectTop5();
    }

    @Override
    public void addNotice(SysNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        sysNoticeMapper.insert(notice);
    }

    @Override
    public void deleteNotice(Long id) {
        sysNoticeMapper.deleteById(id);
    }

    @Override
    public List<SysNotice> getAllNotices() {
        return sysNoticeMapper.selectAll();
    }
}
