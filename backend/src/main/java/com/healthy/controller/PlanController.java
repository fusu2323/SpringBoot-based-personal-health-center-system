package com.healthy.controller;

import com.healthy.common.Result;
import com.healthy.entity.HealthPlan;
import com.healthy.mapper.HealthPlanMapper;
import com.healthy.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private AiService aiService;

    @Autowired
    private HealthPlanMapper healthPlanMapper;

    @PostMapping("/generate")
    public Result generatePlan(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String goal = (String) params.get("goal");
        return Result.success(aiService.generatePlan(userId, goal));
    }

    /**
     * 流式生成计划接口
     * 由于需要接收参数，这里使用 GET + QueryParam 比较方便用于 SSE。
     * 如果参数很长，可以考虑 POST，但前端 EventSource 不支持 POST，需要 fetch 处理。
     * 这里为了方便前端 fetch 流式处理，我们提供一个 GET 接口。
     */
    @GetMapping(value = "/generate-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generatePlanStream(@RequestParam Long userId, @RequestParam String goal) {
        SseEmitter emitter = new SseEmitter(0L); // 0L 表示不过期

        Disposable disposable = aiService.generatePlanStream(userId, goal)
                .subscribe(
                        content -> {
                            try {
                                if (content != null) {
                                    // 替换换行符，防止 SSE 截断
                                    String safeContent = content.replace("\n", "\\n");
                                    emitter.send(safeContent);
                                }
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            try {
                                emitter.send(SseEmitter.event().name("error").data("AI生成失败: " + error.getMessage()));
                                emitter.completeWithError(error);
                            } catch (IOException e) {
                                // ignore
                            }
                        },
                        () -> {
                            try {
                                emitter.send(SseEmitter.event().name("complete").data("[DONE]"));
                                emitter.complete();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                );
        
        emitter.onCompletion(disposable::dispose);
        emitter.onTimeout(disposable::dispose);
        emitter.onError(e -> disposable.dispose());

        return emitter;
    }

    @GetMapping("/list/{userId}")
    public Result getPlans(@PathVariable Long userId) {
        List<HealthPlan> plans = healthPlanMapper.selectByUserId(userId);
        return Result.success(plans);
    }
}
