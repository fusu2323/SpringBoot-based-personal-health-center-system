package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.SysUser;
import com.healthy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/all")
    public Result getAllUsers() {
        return Result.success(sysUserService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Long id) {
        return Result.success(sysUserService.getUserById(id));
    }

    @PostMapping("/add")
    public Result addUser(@RequestBody SysUser user) {
        sysUserService.addUser(user);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateUser(@RequestBody SysUser user) {
        sysUserService.updateUser(user);
        return Result.success();
    }
    
    @PutMapping("/password")
    public Result updatePassword(@RequestBody SysUser user) {
        sysUserService.updatePassword(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        sysUserService.deleteUser(id);
        return Result.success();
    }
}
