package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    /**
     * SSE 流式对话接口
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam Long userId, @RequestParam String message) {
        // 设置超时时间，0表示不过期
        SseEmitter emitter = new SseEmitter(0L);

        Disposable disposable = aiService.streamChat(userId, message)
                .subscribe(
                        content -> {
                            try {
                                // 发送数据块
                                if (content != null) {
                                    // 预处理：将换行符替换为特殊标记，避免被 SSE 协议截断
                                    String safeContent = content.replace("\n", "\\n");
                                    emitter.send(safeContent);
                                }
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            // 发生错误
                            try {
                                emitter.send(SseEmitter.event().name("error").data("AI服务暂时不可用: " + error.getMessage()));
                                emitter.completeWithError(error);
                            } catch (IOException e) {
                                // ignore
                            }
                        },
                        () -> {
                            // 完成
                            try {
                                emitter.send(SseEmitter.event().name("complete").data("[DONE]"));
                                emitter.complete();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                );

        // 客户端断开连接时取消订阅
        emitter.onCompletion(disposable::dispose);
        emitter.onTimeout(disposable::dispose);
        emitter.onError(e -> disposable.dispose());

        return emitter;
    }

    /**
     * 获取每日健康简报
     */
    @GetMapping("/report/daily")
    public Result getDailyReport(@RequestParam Long userId) {
        return Result.success(aiService.getDailyReport(userId));
    }
}
