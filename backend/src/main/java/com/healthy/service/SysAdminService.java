package com.healthy.service;

import com.healthy.entity.SysAdmin;
import java.util.List;
import java.util.Map;

public interface SysAdminService {
    Map<String, Object> login(String username, String password);
    SysAdmin getById(Long id);
    void update(SysAdmin admin);
    
    // 管理员管理
    List<SysAdmin> getAllAdmins();
    void addAdmin(SysAdmin admin);
    void deleteAdmin(Long id);
}
