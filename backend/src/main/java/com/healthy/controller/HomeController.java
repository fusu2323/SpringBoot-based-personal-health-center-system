package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.SysNotice;
import com.healthy.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/stats/{userId}")
    public Result getHomeStats(@PathVariable Long userId) {
        return Result.success(homeService.getHomeStats(userId));
    }

    @GetMapping("/notice/top")
    public Result getTopNotices() {
        return Result.success(homeService.getTopNotices());
    }

    // 管理员用接口
    @GetMapping("/notice/all")
    public Result getAllNotices() {
        return Result.success(homeService.getAllNotices());
    }

    @PostMapping("/notice/add")
    public Result addNotice(@RequestBody SysNotice notice) {
        homeService.addNotice(notice);
        return Result.success();
    }

    @DeleteMapping("/notice/{id}")
    public Result deleteNotice(@PathVariable Long id) {
        homeService.deleteNotice(id);
        return Result.success();
    }
}
