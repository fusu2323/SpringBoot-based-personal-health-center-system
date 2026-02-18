package com.healthy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPost {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    // 扩展字段
    private String userNickname;
    private String userAvatar;
}
