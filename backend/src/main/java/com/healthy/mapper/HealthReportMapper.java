package com.healthy.mapper;

import com.healthy.entity.HealthReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

public interface HealthReportMapper {

    @Select("SELECT * FROM health_report WHERE user_id = #{userId} AND DATE(create_time) = #{date} LIMIT 1")
    HealthReport selectByUserIdAndDate(Long userId, LocalDate date);

    @Insert("INSERT INTO health_report(user_id, content, create_time) VALUES(#{userId}, #{content}, #{createTime})")
    void insert(HealthReport report);
}
