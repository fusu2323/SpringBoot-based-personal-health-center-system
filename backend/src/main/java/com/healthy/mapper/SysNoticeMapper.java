package com.healthy.mapper;

import com.healthy.entity.SysNotice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SysNoticeMapper {
    @Select("SELECT * FROM sys_notice ORDER BY create_time DESC")
    List<SysNotice> selectAll();

    @Select("SELECT * FROM sys_notice ORDER BY create_time DESC LIMIT 5")
    List<SysNotice> selectTop5();

    @Insert("INSERT INTO sys_notice(title, content, admin_id, create_time) " +
            "VALUES(#{title}, #{content}, #{adminId}, #{createTime})")
    void insert(SysNotice notice);

    @Update("UPDATE sys_notice SET title=#{title}, content=#{content} WHERE id=#{id}")
    void update(SysNotice notice);

    @Delete("DELETE FROM sys_notice WHERE id = #{id}")
    void deleteById(Long id);
}
