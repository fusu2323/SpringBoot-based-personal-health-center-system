package com.healthy.mapper;

import com.healthy.entity.ForumPost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ForumPostMapper {

    @Select("SELECT p.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM forum_post p LEFT JOIN sys_user u ON p.user_id = u.id " +
            "ORDER BY p.create_time DESC")
    List<ForumPost> selectAll();

    @Select("SELECT p.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM forum_post p LEFT JOIN sys_user u ON p.user_id = u.id " +
            "WHERE p.user_id = #{userId} ORDER BY p.create_time DESC")
    List<ForumPost> selectByUserId(Long userId);

    @Select("SELECT p.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM forum_post p LEFT JOIN sys_user u ON p.user_id = u.id " +
            "WHERE p.id = #{id}")
    ForumPost selectById(Long id);

    @Select("SELECT p.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM forum_post p LEFT JOIN sys_user u ON p.user_id = u.id " +
            "WHERE p.title LIKE CONCAT('%', #{title}, '%') " +
            "ORDER BY p.create_time DESC")
    List<ForumPost> selectByTitleLike(String title);

    @Insert("INSERT INTO forum_post(user_id, title, content, view_count, create_time) " +
            "VALUES(#{userId}, #{title}, #{content}, 0, #{createTime})")
    void insert(ForumPost post);

    @Update("UPDATE forum_post SET title=#{title}, content=#{content} WHERE id=#{id}")
    void update(ForumPost post);

    @Update("UPDATE forum_post SET view_count = view_count + 1 WHERE id = #{id}")
    void updateViewCount(Long id);

    @Delete("DELETE FROM forum_post WHERE id = #{id}")
    void deleteById(Long id);
}
