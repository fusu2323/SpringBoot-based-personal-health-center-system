package com.healthy.mapper;

import com.healthy.entity.SysAdmin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SysAdminMapper {

    @Select("SELECT * FROM sys_admin WHERE username = #{username}")
    SysAdmin selectByUsername(String username);

    @Select("SELECT * FROM sys_admin WHERE id = #{id}")
    SysAdmin selectById(Long id);

    @Select("SELECT * FROM sys_admin")
    List<SysAdmin> selectAll();
    
    @Update("UPDATE sys_admin SET nickname=#{nickname}, phone=#{phone}, avatar=#{avatar}, password=#{password} WHERE id=#{id}")
    void update(SysAdmin admin);

    @Insert("INSERT INTO sys_admin(username, password, nickname, phone, avatar) VALUES(#{username}, #{password}, #{nickname}, #{phone}, #{avatar})")
    void insert(SysAdmin admin);

    @Delete("DELETE FROM sys_admin WHERE id = #{id}")
    void delete(Long id);
}
