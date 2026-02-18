package com.healthy.mapper;

import com.healthy.entity.HealthPlan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HealthPlanMapper {

    @Select("SELECT * FROM health_plan WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<HealthPlan> selectByUserId(Long userId);

    @Select("SELECT * FROM health_plan WHERE id = #{id}")
    HealthPlan selectById(Long id);

    @Insert("INSERT INTO health_plan(user_id, goal, content, create_time) VALUES(#{userId}, #{goal}, #{content}, #{createTime})")
    void insert(HealthPlan plan);
}
