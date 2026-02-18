package com.healthy.controller;

import cn.hutool.core.util.StrUtil;
import com.healthy.common.Result;
import com.healthy.entity.SysUser;
import com.healthy.service.SysAdminService;
import com.healthy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WebController {

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private SysAdminService sysAdminService;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        String role = user.get("role");

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error("400", "参数错误");
        }

        if ("ADMIN".equals(role)) {
            return Result.success(sysAdminService.login(username, password));
        } else {
            return Result.success(sysUserService.login(username, password));
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody SysUser user) {
        sysUserService.register(user);
        return Result.success(user);
    }
}
