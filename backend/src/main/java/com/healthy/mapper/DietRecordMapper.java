package com.healthy.mapper;

import com.healthy.entity.DietRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface DietRecordMapper {
    @Select("SELECT * FROM diet_record WHERE user_id = #{userId} ORDER BY record_date DESC")
    List<DietRecord> selectByUserId(Long userId);

    @Select("SELECT d.*, u.username as username, u.nickname as nickname FROM diet_record d LEFT JOIN sys_user u ON d.user_id = u.id ORDER BY d.record_date DESC")
    List<Map<String, Object>> selectAllWithUser();

    @Select("SELECT * FROM diet_record ORDER BY record_date DESC")
    List<DietRecord> selectAll();

    @Insert("INSERT INTO diet_record(user_id, food_name, calorie, meal_type, record_date, create_time) " +
            "VALUES(#{userId}, #{foodName}, #{calorie}, #{mealType}, #{recordDate}, #{createTime})")
    void insert(DietRecord record);

    @Update("UPDATE diet_record SET food_name=#{foodName}, calorie=#{calorie}, meal_type=#{mealType}, record_date=#{recordDate} WHERE id=#{id}")
    void update(DietRecord record);

    @Delete("DELETE FROM diet_record WHERE id = #{id}")
    void deleteById(Long id);
}
