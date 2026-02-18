package com.healthy.service;

import com.healthy.common.Result;
import com.healthy.entity.SysNotice;
import java.util.List;
import java.util.Map;

public interface HomeService {
    Map<String, Object> getHomeStats(Long userId);
    List<SysNotice> getTopNotices();
    void addNotice(SysNotice notice);
    void deleteNotice(Long id);
    List<SysNotice> getAllNotices();
}
