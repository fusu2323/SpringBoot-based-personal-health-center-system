package com.healthy.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.healthy.entity.AnalyticsData;
import com.healthy.entity.DietRecord;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SleepRecord;
import com.healthy.entity.SportRecord;
import com.healthy.mapper.DietRecordMapper;
import com.healthy.mapper.HealthIndicatorMapper;
import com.healthy.mapper.SleepRecordMapper;
import com.healthy.mapper.SportRecordMapper;
import com.healthy.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private HealthIndicatorMapper healthIndicatorMapper;
    @Autowired
    private SportRecordMapper sportRecordMapper;
    @Autowired
    private DietRecordMapper dietRecordMapper;
    @Autowired
    private SleepRecordMapper sleepRecordMapper;

    @Override
    public AnalyticsData getIndicatorStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<HealthIndicator> indicators = healthIndicatorMapper.selectByUserId(userId);
        List<HealthIndicator> filtered = indicators.stream()
                .filter(i -> !i.getRecordDate().isBefore(startDate) && !i.getRecordDate().isAfter(endDate))
                .collect(Collectors.toList());

        // Group by date, take latest record per day
        Map<LocalDate, HealthIndicator> byDate = filtered.stream()
                .collect(Collectors.groupingBy(HealthIndicator::getRecordDate,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(HealthIndicator::getCreateTime)),
                                Optional::get)));

        // Sort dates ascending
        List<LocalDate> sortedDates = byDate.keySet().stream().sorted().collect(Collectors.toList());

        List<String> dateStrings = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

        for (LocalDate date : sortedDates) {
            dateStrings.add(date.toString());
            HealthIndicator ind = byDate.get(date);
            Double val = getIndicatorValue(ind);
            values.add(val);
            if (val != null && val > 0) {
                stats.accept(val);
            }
        }

        AnalyticsData data = new AnalyticsData();
        data.setDates(dateStrings);
        data.setValues(values);
        data.setAvg(stats.getAverage());
        data.setMin(stats.getMin());
        data.setMax(stats.getMax());
        data.setTotal(stats.getSum());
        data.setCount(stats.getCount());
        return data;
    }

    private Double getIndicatorValue(HealthIndicator ind) {
        // Prefer heartRate; fall back to bloodSugar; then parse bloodPressure for systolic
        if (ind.getHeartRate() != null && ind.getHeartRate() > 0) {
            return ind.getHeartRate().doubleValue();
        }
        if (ind.getBloodSugar() != null && ind.getBloodSugar() > 0) {
            return ind.getBloodSugar();
        }
        if (ind.getBloodPressure() != null && ind.getBloodPressure().contains("/")) {
            String[] parts = ind.getBloodPressure().split("/");
            if (parts.length == 2 && NumberUtil.isNumber(parts[0])) {
                return Double.parseDouble(parts[0]);
            }
        }
        return 0.0;
    }

    @Override
    public AnalyticsData getSportStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<SportRecord> sports = sportRecordMapper.selectByUserId(userId);
        List<SportRecord> filtered = sports.stream()
                .filter(s -> !s.getRecordDate().isBefore(startDate) && !s.getRecordDate().isAfter(endDate))
                .collect(Collectors.toList());

        // Group by date, sum durations
        Map<LocalDate, Integer> byDate = filtered.stream()
                .collect(Collectors.groupingBy(SportRecord::getRecordDate,
                        Collectors.summingInt(SportRecord::getDuration)));

        List<LocalDate> sortedDates = byDate.keySet().stream().sorted().collect(Collectors.toList());

        List<String> dateStrings = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

        for (LocalDate date : sortedDates) {
            dateStrings.add(date.toString());
            double val = byDate.get(date);
            values.add(val);
            stats.accept(val);
        }

        AnalyticsData data = new AnalyticsData();
        data.setDates(dateStrings);
        data.setValues(values);
        data.setAvg(stats.getAverage());
        data.setMin(stats.getMin());
        data.setMax(stats.getMax());
        data.setTotal(stats.getSum());
        data.setCount(stats.getCount());
        return data;
    }

    @Override
    public AnalyticsData getDietStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<DietRecord> diets = dietRecordMapper.selectByUserId(userId);
        List<DietRecord> filtered = diets.stream()
                .filter(d -> !d.getRecordDate().isBefore(startDate) && !d.getRecordDate().isAfter(endDate))
                .collect(Collectors.toList());

        // Group by date, sum calories
        Map<LocalDate, Double> byDate = filtered.stream()
                .collect(Collectors.groupingBy(DietRecord::getRecordDate,
                        Collectors.summingDouble(DietRecord::getCalorie)));

        List<LocalDate> sortedDates = byDate.keySet().stream().sorted().collect(Collectors.toList());

        List<String> dateStrings = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

        for (LocalDate date : sortedDates) {
            dateStrings.add(date.toString());
            double val = byDate.get(date);
            values.add(val);
            stats.accept(val);
        }

        AnalyticsData data = new AnalyticsData();
        data.setDates(dateStrings);
        data.setValues(values);
        data.setAvg(stats.getAverage());
        data.setMin(stats.getMin());
        data.setMax(stats.getMax());
        data.setTotal(stats.getSum());
        data.setCount(stats.getCount());
        return data;
    }

    @Override
    public AnalyticsData getSleepStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<SleepRecord> sleeps = sleepRecordMapper.selectByUserId(userId);
        List<SleepRecord> filtered = sleeps.stream()
                .filter(s -> !s.getRecordDate().isBefore(startDate) && !s.getRecordDate().isAfter(endDate))
                .collect(Collectors.toList());

        // Group by date, sum durations
        Map<LocalDate, Double> byDate = filtered.stream()
                .collect(Collectors.groupingBy(SleepRecord::getRecordDate,
                        Collectors.summingDouble(SleepRecord::getDuration)));

        List<LocalDate> sortedDates = byDate.keySet().stream().sorted().collect(Collectors.toList());

        List<String> dateStrings = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

        for (LocalDate date : sortedDates) {
            dateStrings.add(date.toString());
            double val = byDate.get(date);
            values.add(val);
            stats.accept(val);
        }

        AnalyticsData data = new AnalyticsData();
        data.setDates(dateStrings);
        data.setValues(values);
        data.setAvg(stats.getAverage());
        data.setMin(stats.getMin());
        data.setMax(stats.getMax());
        data.setTotal(stats.getSum());
        data.setCount(stats.getCount());
        return data;
    }
}
