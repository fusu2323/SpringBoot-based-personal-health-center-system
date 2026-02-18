package com.healthy.service;

import com.healthy.entity.SysUser;
import java.util.List;
import java.util.Map;

public interface SysUserService {
    Map<String, Object> login(String username, String password);
    void register(SysUser user);
    void addUser(SysUser user);
    void updateUser(SysUser user);
    void updatePassword(SysUser user);
    void deleteUser(Long id);
    SysUser getUserById(Long id);
    List<SysUser> getAllUsers();
}
