package com.healthy.mapper;

import com.healthy.entity.SportRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface SportRecordMapper {
    @Select("SELECT * FROM sport_record WHERE user_id = #{userId} ORDER BY record_date DESC")
    List<SportRecord> selectByUserId(Long userId);

    @Select("SELECT s.*, u.username as username, u.nickname as nickname FROM sport_record s LEFT JOIN sys_user u ON s.user_id = u.id ORDER BY s.record_date DESC")
    List<Map<String, Object>> selectAllWithUser();

    @Select("SELECT * FROM sport_record ORDER BY record_date DESC")
    List<SportRecord> selectAll();

    @Insert("INSERT INTO sport_record(user_id, sport_type, duration, calorie, record_date, create_time) " +
            "VALUES(#{userId}, #{sportType}, #{duration}, #{calorie}, #{recordDate}, #{createTime})")
    void insert(SportRecord record);

    @Update("UPDATE sport_record SET sport_type=#{sportType}, duration=#{duration}, calorie=#{calorie}, record_date=#{recordDate} WHERE id=#{id}")
    void update(SportRecord record);

    @Delete("DELETE FROM sport_record WHERE id = #{id}")
    void deleteById(Long id);
}
