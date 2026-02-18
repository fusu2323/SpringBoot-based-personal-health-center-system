package com.healthy.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.healthy.entity.*;
import com.healthy.mapper.HealthPlanMapper;
import com.healthy.mapper.HealthReportMapper;
import com.healthy.service.AiService;
import com.healthy.service.HealthService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Autowired
    private HealthService healthService;
    
    @Autowired
    private HealthReportMapper healthReportMapper;

    @Autowired
    private HealthPlanMapper healthPlanMapper;

    public AiServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public Flux<String> streamChat(Long userId, String message) {
        String context = buildUserHealthContext(userId);
        String systemPrompt = """
                你是一名专业的个人健康顾问助手。请根据用户的健康数据和提问，给出科学、具体且有同理心的建议。
                
                【用户健康档案】
                %s
                
                【回答要求】
                1. 如果用户数据中有异常（如血压高、睡眠少），请优先指出并给出改善建议。
                2. 回答要条理清晰，可以使用 Markdown 格式（如列表、加粗）。
                3. 语气亲切，像朋友一样交谈。
                4. 不要凭空捏造数据，只根据提供的档案分析。
                """.formatted(context);

        return chatClient.prompt()
                .system(systemPrompt)
                .user(message)
                .stream()
                .content();
    }

    @Override
    public String getDailyReport(Long userId) {
        HealthReport exist = healthReportMapper.selectByUserIdAndDate(userId, LocalDate.now());
        if (exist != null) {
            return exist.getContent();
        }

        String context = buildUserHealthContext(userId);
        String systemPrompt = """
                你是一名专业的健康分析师。请根据用户的近期健康数据，生成一份【每日健康简报】。
                
                【用户数据】
                %s
                
                【生成要求】
                1. 字数控制在 200 字左右，不要太长。
                2. 语气温暖、鼓励为主，但也需要客观指出问题。
                3. 重点分析：运动量是否达标？睡眠是否充足？饮食是否健康？
                4. 结尾给出一个今日的小建议。
                5. 不需要打招呼，直接进入正题。
                """.formatted(context);

        String reportContent = chatClient.prompt()
                .system(systemPrompt)
                .user("生成今日简报")
                .call()
                .content();

        HealthReport report = new HealthReport();
        report.setUserId(userId);
        report.setContent(reportContent);
        report.setCreateTime(LocalDateTime.now());
        healthReportMapper.insert(report);

        return reportContent;
    }

    @Override
    public String generatePlan(Long userId, String goal) {
        // 保留同步方法以兼容旧接口，或者可以直接调用流式方法并阻塞
        // 为简单起见，这里还是保留独立逻辑，或者复用
        String context = buildUserHealthContext(userId);
        String systemPrompt = buildPlanPrompt(context, goal);

        String jsonContent = chatClient.prompt()
                .system(systemPrompt)
                .user("生成计划")
                .call()
                .content();
        
        savePlan(userId, goal, jsonContent);
        return jsonContent;
    }

    @Override
    public Flux<String> generatePlanStream(Long userId, String goal) {
        String context = buildUserHealthContext(userId);
        String systemPrompt = buildPlanPrompt(context, goal);
        
        // 用于收集完整的响应内容
        StringBuilder contentBuilder = new StringBuilder();

        return chatClient.prompt()
                .system(systemPrompt)
                .user("生成计划")
                .stream()
                .content()
                .doOnNext(content -> {
                    if (content != null) {
                        contentBuilder.append(content);
                    }
                })
                .doOnComplete(() -> {
                    // 流结束时保存到数据库
                    String fullContent = contentBuilder.toString();
                    savePlan(userId, goal, fullContent);
                });
    }

    private String buildPlanPrompt(String context, String goal) {
        return """
                你是一名专业的健身教练和营养师。请根据用户的身体数据和目标，生成一份详细的 7 天健康计划。
                
                【用户档案】
                %s
                
                【用户目标】
                %s
                
                【输出格式要求】
                请务必只返回标准的 JSON 字符串，不要包含任何 Markdown 标记（如 ```json），也不要包含其他多余文字。
                JSON 结构如下：
                {
                    "dietPlan": [
                        { "day": 1, "breakfast": "...", "lunch": "...", "dinner": "..." },
                        ...
                    ],
                    "sportPlan": [
                        { "day": 1, "content": "...", "duration": "..." },
                        ...
                    ],
                    "advice": "..."
                }
                """.formatted(context, goal);
    }

    private void savePlan(Long userId, String goal, String rawContent) {
        // 清理可能的 Markdown 标记
        String jsonContent = rawContent.replace("```json", "").replace("```", "").trim();

        // 简单的 JSON 校验（防止保存完全错误的格式）
        if (!jsonContent.startsWith("{")) {
            // 如果 AI 还是输出了废话，尝试截取第一个 { 和最后一个 }
            int start = jsonContent.indexOf("{");
            int end = jsonContent.lastIndexOf("}");
            if (start >= 0 && end > start) {
                jsonContent = jsonContent.substring(start, end + 1);
            }
        }

        HealthPlan plan = new HealthPlan();
        plan.setUserId(userId);
        plan.setGoal(goal);
        plan.setContent(jsonContent);
        plan.setCreateTime(LocalDateTime.now());
        healthPlanMapper.insert(plan);
    }

    private String buildUserHealthContext(Long userId) {
        StringBuilder sb = new StringBuilder();

        // 1. 身体指标
        List<HealthIndicator> indicators = healthService.getIndicatorList(userId);
        if (CollUtil.isNotEmpty(indicators)) {
            HealthIndicator latest = indicators.get(0); 
            sb.append("- 最新身体指标 (").append(DateUtil.formatDate(DateUtil.date(latest.getRecordDate()))).append("): ")
              .append("体重 ").append(latest.getWeight()).append("kg, ")
              .append("血压 ").append(latest.getBloodPressure()).append(", ")
              .append("心率 ").append(latest.getHeartRate()).append("\n");
        } else {
            sb.append("- 暂无身体指标记录\n");
        }

        // 2. 运动记录 (最近3条)
        List<SportRecord> sports = healthService.getSportList(userId);
        if (CollUtil.isNotEmpty(sports)) {
            sb.append("- 最近运动记录: \n");
            sports.stream().limit(3).forEach(s -> 
                sb.append("  * ").append(DateUtil.formatDate(DateUtil.date(s.getRecordDate())))
                  .append(": ").append(s.getSportType()).append(" ").append(s.getDuration()).append("分钟, 消耗 ").append(s.getCalorie()).append("千卡\n")
            );
        }

        // 3. 睡眠记录 (最近1条)
        List<SleepRecord> sleeps = healthService.getSleepList(userId);
        if (CollUtil.isNotEmpty(sleeps)) {
            SleepRecord s = sleeps.get(0);
            sb.append("- 最近睡眠: ").append(DateUtil.formatDate(DateUtil.date(s.getRecordDate())))
              .append(" 时长 ").append(s.getDuration()).append("小时, 质量 ").append(s.getQuality()).append("\n");
        }

        // 4. 饮食记录 (最近3条)
        List<DietRecord> diets = healthService.getDietList(userId);
        if (CollUtil.isNotEmpty(diets)) {
             sb.append("- 最近饮食: \n");
             diets.stream().limit(3).forEach(d -> 
                sb.append("  * ").append(DateUtil.formatDate(DateUtil.date(d.getRecordDate())))
                  .append(" ").append(d.getMealType()).append(": ").append(d.getFoodName()).append("\n")
             );
        }

        return sb.toString();
    }
}
