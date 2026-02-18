# AI 健康简报生成功能实现方案

## 1. 核心需求
在用户首页展示一份“AI 每日健康简报”，该简报由 AI 根据用户近期的健康数据生成，并持久化存储到 MySQL 数据库中（每日仅生成一次，避免重复调用 AI）。

## 2. 后端实现
### 数据库设计
- 新增表 `health_report`：
  - `id`: 主键
  - `user_id`: 用户ID
  - `content`: 简报内容 (TEXT)
  - `create_time`: 生成时间 (DATETIME)

### 代码逻辑
1.  **Entity & Mapper**: 创建 `HealthReport` 实体类和 `HealthReportMapper` 接口，实现 `selectByUserIdAndDate` 和 `insert` 方法。
2.  **Service**:
    - 在 `AiService` 中添加 `getDailyReport(Long userId)` 接口。
    - 在 `AiServiceImpl` 中实现该方法：
        - **查询**: 先检查数据库中是否已有当日简报。
        - **生成**: 若无，拉取用户近期数据，构造 Prompt。
        - **调用 AI**: 使用 `ChatClient` 的 Fluent API (`chatClient.prompt()...call().content()`) 进行同步调用。
        - **保存**: 将生成的内容存入 `health_report` 表。
        - **返回**: 返回简报内容。
3.  **Controller**: 在 `AiController` 中暴露 `/ai/report/daily` 接口。

## 3. 前端实现
- **Home.vue**:
  - 新增一个 `el-card` 用于展示简报。
  - 页面加载时调用 `/ai/report/daily` 接口。
  - 使用 `markdown-it` 渲染返回的 Markdown 文本。
  - 添加“刷新简报”按钮（可选，用于调试或强制重新生成，当前逻辑主要依赖日期缓存）。

## 4. 交付验证
- 登录后首页顶部应出现“AI 每日健康简报”卡片。
- 首次加载时会有 Loading 状态（AI 生成中），生成后显示文字。
- 刷新页面时应瞬间加载（从数据库读取）。
