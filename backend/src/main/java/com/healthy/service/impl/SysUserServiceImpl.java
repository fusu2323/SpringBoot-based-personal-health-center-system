package com.healthy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.healthy.common.ServiceException;
import com.healthy.entity.SysUser;
import com.healthy.mapper.SysUserMapper;
import com.healthy.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser dbUser = sysUserMapper.selectByUsername(username);
        if (dbUser == null || !dbUser.getPassword().equals(password)) {
            throw new ServiceException("401", "用户名或密码错误");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("user", dbUser);
        res.put("token", "user-token-" + dbUser.getId());
        return res;
    }

    @Override
    public void register(SysUser user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            throw new ServiceException("400", "参数错误");
        }
        SysUser dbUser = sysUserMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new ServiceException("409", "用户名已存在");
        }
        user.setCreateTime(LocalDateTime.now());
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        sysUserMapper.insert(user);
    }

    @Override
    public void addUser(SysUser user) {
        register(user); // 复用注册逻辑
    }

    @Override
    public void updateUser(SysUser user) {
        sysUserMapper.update(user);
    }

    @Override
    public void updatePassword(SysUser user) {
        sysUserMapper.updatePassword(user);
    }

    @Override
    public void deleteUser(Long id) {
        sysUserMapper.deleteById(id);
    }

    @Override
    public SysUser getUserById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public List<SysUser> getAllUsers() {
        return sysUserMapper.selectAll();
    }
}
