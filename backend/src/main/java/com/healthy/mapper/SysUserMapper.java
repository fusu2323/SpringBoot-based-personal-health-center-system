package com.healthy.mapper;

import com.healthy.entity.SysUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SysUserMapper {
    
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(String username);

    @Insert("INSERT INTO sys_user(username, password, nickname, gender, age, height, weight, phone, avatar, create_time) " +
            "VALUES(#{username}, #{password}, #{nickname}, #{gender}, #{age}, #{height}, #{weight}, #{phone}, #{avatar}, #{createTime})")
    void insert(SysUser user);

    @Update("UPDATE sys_user SET nickname=#{nickname}, gender=#{gender}, age=#{age}, height=#{height}, weight=#{weight}, " +
            "phone=#{phone}, avatar=#{avatar} WHERE id=#{id}")
    void update(SysUser user);

    @Update("UPDATE sys_user SET password=#{password} WHERE id=#{id}")
    void updatePassword(SysUser user);

    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Long id);

    @Select("SELECT * FROM sys_user")
    List<SysUser> selectAll();
}
