package com.healthy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.healthy.common.ServiceException;
import com.healthy.entity.SysAdmin;
import com.healthy.mapper.SysAdminMapper;
import com.healthy.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysAdminServiceImpl implements SysAdminService {

    @Autowired
    private SysAdminMapper sysAdminMapper;

    @Override
    public Map<String, Object> login(String username, String password) {
        SysAdmin dbAdmin = sysAdminMapper.selectByUsername(username);
        if (dbAdmin == null || !dbAdmin.getPassword().equals(password)) {
             throw new ServiceException("401", "用户名或密码错误");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("user", dbAdmin);
        res.put("token", "admin-token-" + dbAdmin.getId());
        return res;
    }

    @Override
    public SysAdmin getById(Long id) {
        return sysAdminMapper.selectById(id);
    }

    @Override
    public void update(SysAdmin admin) {
        sysAdminMapper.update(admin);
    }

    @Override
    public List<SysAdmin> getAllAdmins() {
        return sysAdminMapper.selectAll();
    }

    @Override
    public void addAdmin(SysAdmin admin) {
        SysAdmin dbAdmin = sysAdminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new ServiceException("400", "用户名已存在");
        }
        if (StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("123456"); // 默认密码
        }
        sysAdminMapper.insert(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        sysAdminMapper.delete(id);
    }
}
