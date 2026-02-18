package com.healthy.service;

import com.healthy.entity.ForumComment;
import com.healthy.entity.ForumPost;
import java.util.List;
import java.util.Map;

public interface ForumService {
    List<ForumPost> getAllPosts(String title);
    List<ForumPost> getMyPosts(Long userId);
    ForumPost getPostById(Long id);
    void savePost(ForumPost post);
    void deletePost(Long id);
    
    List<ForumComment> getComments(Long postId);
    void addComment(ForumComment comment);
    void deleteComment(Long id);
    
    // Admin & User center
    List<Map<String, Object>> getMyComments(Long userId);
    List<Map<String, Object>> getAllComments();
}
