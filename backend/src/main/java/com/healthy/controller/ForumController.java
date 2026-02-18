package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.ForumComment;
import com.healthy.entity.ForumPost;
import com.healthy.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/post/all")
    public Result getAllPosts(@RequestParam(required = false) String title) {
        return Result.success(forumService.getAllPosts(title));
    }
    
    @GetMapping("/post/my/{userId}")
    public Result getMyPosts(@PathVariable Long userId) {
        return Result.success(forumService.getMyPosts(userId));
    }

    @GetMapping("/post/{id}")
    public Result getPostById(@PathVariable Long id) {
        return Result.success(forumService.getPostById(id));
    }

    @PostMapping("/post/save")
    public Result savePost(@RequestBody ForumPost post) {
        forumService.savePost(post);
        return Result.success();
    }

    @DeleteMapping("/post/{id}")
    public Result deletePost(@PathVariable Long id) {
        forumService.deletePost(id);
        return Result.success();
    }

    @GetMapping("/comment/{postId}")
    public Result getComments(@PathVariable Long postId) {
        return Result.success(forumService.getComments(postId));
    }

    @PostMapping("/comment/add")
    public Result addComment(@RequestBody ForumComment comment) {
        forumService.addComment(comment);
        return Result.success();
    }
    
    @DeleteMapping("/comment/{id}")
    public Result deleteComment(@PathVariable Long id) {
        forumService.deleteComment(id);
        return Result.success();
    }

    // 获取我的评论
    @GetMapping("/comment/my/{userId}")
    public Result getMyComments(@PathVariable Long userId) {
        return Result.success(forumService.getMyComments(userId));
    }

    // 管理员获取所有评论
    @GetMapping("/comment/all")
    public Result getAllComments() {
        return Result.success(forumService.getAllComments());
    }
}
