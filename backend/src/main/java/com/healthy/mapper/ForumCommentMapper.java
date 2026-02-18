package com.healthy.mapper;

import com.healthy.entity.ForumComment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ForumCommentMapper {
    @Select("SELECT c.*, u.nickname as userNickname, u.avatar as userAvatar " +
            "FROM forum_comment c LEFT JOIN sys_user u ON c.user_id = u.id " +
            "WHERE c.post_id = #{postId} ORDER BY c.create_time ASC")
    List<ForumComment> selectByPostId(Long postId);

    @Select("SELECT c.*, u.nickname as userNickname, u.avatar as userAvatar, p.title as postTitle " +
            "FROM forum_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.id " +
            "LEFT JOIN forum_post p ON c.post_id = p.id " +
            "WHERE c.user_id = #{userId} ORDER BY c.create_time DESC")
    List<Map<String, Object>> selectByUserId(Long userId);

    @Select("SELECT c.*, u.nickname as userNickname, u.avatar as userAvatar, p.title as postTitle " +
            "FROM forum_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.id " +
            "LEFT JOIN forum_post p ON c.post_id = p.id " +
            "ORDER BY c.create_time DESC")
    List<Map<String, Object>> selectAllWithUserAndPost();

    @Insert("INSERT INTO forum_comment(post_id, user_id, content, parent_id, create_time) " +
            "VALUES(#{postId}, #{userId}, #{content}, #{parentId}, #{createTime})")
    void insert(ForumComment comment);
    
    @Delete("DELETE FROM forum_comment WHERE id = #{id}")
    void deleteById(Long id);
    
    @Delete("DELETE FROM forum_comment WHERE post_id = #{postId}")
    void deleteByPostId(Long postId);
}
