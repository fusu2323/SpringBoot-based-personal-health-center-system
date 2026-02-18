package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.DietRecord;
import com.healthy.entity.HealthIndicator;
import com.healthy.entity.SleepRecord;
import com.healthy.entity.SportRecord;
import com.healthy.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private HealthService healthService;

    // --- 身体指标 ---
    @GetMapping("/indicator/list/{userId}")
    public Result getIndicatorList(@PathVariable Long userId) {
        return Result.success(healthService.getIndicatorList(userId));
    }
    
    @PostMapping("/indicator/save")
    public Result saveIndicator(@RequestBody HealthIndicator record) {
        healthService.saveIndicator(record);
        return Result.success();
    }
    
    @DeleteMapping("/indicator/{id}")
    public Result deleteIndicator(@PathVariable Long id) {
        healthService.deleteIndicator(id);
        return Result.success();
    }

    // --- 运动记录 ---
    @GetMapping("/sport/list/{userId}")
    public Result getSportList(@PathVariable Long userId) {
        return Result.success(healthService.getSportList(userId));
    }

    @PostMapping("/sport/save")
    public Result saveSport(@RequestBody SportRecord record) {
        healthService.saveSport(record);
        return Result.success();
    }

    @DeleteMapping("/sport/{id}")
    public Result deleteSport(@PathVariable Long id) {
        healthService.deleteSport(id);
        return Result.success();
    }

    // --- 饮食记录 ---
    @GetMapping("/diet/list/{userId}")
    public Result getDietList(@PathVariable Long userId) {
        return Result.success(healthService.getDietList(userId));
    }

    @PostMapping("/diet/save")
    public Result saveDiet(@RequestBody DietRecord record) {
        healthService.saveDiet(record);
        return Result.success();
    }

    @DeleteMapping("/diet/{id}")
    public Result deleteDiet(@PathVariable Long id) {
        healthService.deleteDiet(id);
        return Result.success();
    }

    // --- 睡眠记录 ---
    @GetMapping("/sleep/list/{userId}")
    public Result getSleepList(@PathVariable Long userId) {
        return Result.success(healthService.getSleepList(userId));
    }

    @PostMapping("/sleep/save")
    public Result saveSleep(@RequestBody SleepRecord record) {
        healthService.saveSleep(record);
        return Result.success();
    }

    @DeleteMapping("/sleep/{id}")
    public Result deleteSleep(@PathVariable Long id) {
        healthService.deleteSleep(id);
        return Result.success();
    }

    // --- 管理员接口 ---
    @GetMapping("/admin/indicator/all")
    public Result getAllIndicators() {
        return Result.success(healthService.getAllIndicators());
    }

    @GetMapping("/admin/sport/all")
    public Result getAllSports() {
        return Result.success(healthService.getAllSports());
    }

    @GetMapping("/admin/diet/all")
    public Result getAllDiets() {
        return Result.success(healthService.getAllDiets());
    }

    @GetMapping("/admin/sleep/all")
    public Result getAllSleeps() {
        return Result.success(healthService.getAllSleeps());
    }
}
