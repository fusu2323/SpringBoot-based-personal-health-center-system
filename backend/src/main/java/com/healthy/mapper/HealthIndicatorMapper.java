package com.healthy.mapper;

import com.healthy.entity.HealthIndicator;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface HealthIndicatorMapper {
    @Select("SELECT * FROM health_indicator WHERE user_id = #{userId} ORDER BY record_date DESC")
    List<HealthIndicator> selectByUserId(Long userId);

    @Select("SELECT h.*, u.username as username, u.nickname as nickname FROM health_indicator h LEFT JOIN sys_user u ON h.user_id = u.id ORDER BY h.record_date DESC")
    List<Map<String, Object>> selectAllWithUser();

    @Select("SELECT * FROM health_indicator ORDER BY record_date DESC")
    List<HealthIndicator> selectAll();

    @Insert("INSERT INTO health_indicator(user_id, blood_pressure, blood_sugar, heart_rate, temperature, weight, record_date, create_time) " +
            "VALUES(#{userId}, #{bloodPressure}, #{bloodSugar}, #{heartRate}, #{temperature}, #{weight}, #{recordDate}, #{createTime})")
    void insert(HealthIndicator record);

    @Update("UPDATE health_indicator SET blood_pressure=#{bloodPressure}, blood_sugar=#{bloodSugar}, heart_rate=#{heartRate}, " +
            "temperature=#{temperature}, weight=#{weight}, record_date=#{recordDate} WHERE id=#{id}")
    void update(HealthIndicator record);

    @Delete("DELETE FROM health_indicator WHERE id = #{id}")
    void deleteById(Long id);
}
