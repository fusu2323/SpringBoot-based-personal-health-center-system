package com.healthy.mapper;

import com.healthy.entity.SleepRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface SleepRecordMapper {
    @Select("SELECT * FROM sleep_record WHERE user_id = #{userId} ORDER BY record_date DESC")
    List<SleepRecord> selectByUserId(Long userId);

    @Select("SELECT s.*, u.username as username, u.nickname as nickname FROM sleep_record s LEFT JOIN sys_user u ON s.user_id = u.id ORDER BY s.record_date DESC")
    List<Map<String, Object>> selectAllWithUser();

    @Select("SELECT * FROM sleep_record ORDER BY record_date DESC")
    List<SleepRecord> selectAll();

    @Insert("INSERT INTO sleep_record(user_id, sleep_time, wake_time, duration, quality, record_date, create_time) " +
            "VALUES(#{userId}, #{sleepTime}, #{wakeTime}, #{duration}, #{quality}, #{recordDate}, #{createTime})")
    void insert(SleepRecord record);

    @Update("UPDATE sleep_record SET sleep_time=#{sleepTime}, wake_time=#{wakeTime}, duration=#{duration}, quality=#{quality}, record_date=#{recordDate} WHERE id=#{id}")
    void update(SleepRecord record);

    @Delete("DELETE FROM sleep_record WHERE id = #{id}")
    void deleteById(Long id);
}
