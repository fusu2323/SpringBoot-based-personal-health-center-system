package com.healthy.service.impl;

import com.healthy.entity.ForumComment;
import com.healthy.entity.ForumPost;
import com.healthy.mapper.ForumCommentMapper;
import com.healthy.mapper.ForumPostMapper;
import com.healthy.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumPostMapper forumPostMapper;
    @Autowired
    private ForumCommentMapper forumCommentMapper;

    @Override
    public List<ForumPost> getAllPosts(String title) {
        if (title != null && !title.isEmpty()) {
            return forumPostMapper.selectByTitleLike(title);
        }
        return forumPostMapper.selectAll();
    }

    @Override
    public List<ForumPost> getMyPosts(Long userId) {
        return forumPostMapper.selectByUserId(userId);
    }

    @Override
    public ForumPost getPostById(Long id) {
        forumPostMapper.updateViewCount(id);
        return forumPostMapper.selectById(id);
    }

    @Override
    public void savePost(ForumPost post) {
        if (post.getId() == null) {
            post.setCreateTime(LocalDateTime.now());
            forumPostMapper.insert(post);
        } else {
            forumPostMapper.update(post);
        }
    }

    @Override
    public void deletePost(Long id) {
        forumPostMapper.deleteById(id);
        forumCommentMapper.deleteByPostId(id);
    }

    @Override
    public List<ForumComment> getComments(Long postId) {
        return forumCommentMapper.selectByPostId(postId);
    }

    @Override
    public void addComment(ForumComment comment) {
        comment.setCreateTime(LocalDateTime.now());
        forumCommentMapper.insert(comment);
    }

    @Override
    public void deleteComment(Long id) {
        forumCommentMapper.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getMyComments(Long userId) {
        return forumCommentMapper.selectByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> getAllComments() {
        return forumCommentMapper.selectAllWithUserAndPost();
    }
}
