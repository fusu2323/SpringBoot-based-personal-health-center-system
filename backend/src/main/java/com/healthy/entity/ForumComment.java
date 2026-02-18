package com.healthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumComment {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private Long parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    // 扩展字段
    private String userNickname;
    private String userAvatar;
}
