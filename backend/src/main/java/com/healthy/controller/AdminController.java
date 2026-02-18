package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.SysAdmin;
import com.healthy.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SysAdminService sysAdminService;

    @GetMapping("/all")
    public Result getAllAdmins() {
        return Result.success(sysAdminService.getAllAdmins());
    }

    @PostMapping("/add")
    public Result addAdmin(@RequestBody SysAdmin admin) {
        sysAdminService.addAdmin(admin);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateAdmin(@RequestBody SysAdmin admin) {
        sysAdminService.update(admin);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Long id) {
        sysAdminService.deleteAdmin(id);
        return Result.success();
    }
}
