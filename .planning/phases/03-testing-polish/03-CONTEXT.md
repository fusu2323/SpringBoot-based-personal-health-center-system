# Phase 3: Testing & Polish - Context

**Gathered:** 2026-04-20
**Status:** Ready for planning

<domain>
## Phase Boundary

Complete the testing chapter with formal white-box and black-box test reports, add program flowcharts for body indicator recording and health data analysis, and verify document quality (Mermaid rendering, table formatting, heading hierarchy, cross-references).

**Requirements covered:** TH-08, TH-09, TH-10, MG-07, MG-08, DQ-01, DQ-02, DQ-03, DQ-04

</domain>

<decisions>
## Implementation Decisions

### White-Box Testing Report (TH-09)
- **D-01:** Write actual unit tests for all 5 backend service classes
  - UserService, HealthIndicatorService, SportRecordService, DietRecordService, SleepRecordService
- **D-02:** 2-3 test cases per service
  - Happy path (normal CRUD flow)
  - 1 edge case per service (null input, invalid data, or exception handling)
- **D-03:** Use JUnit 5 + Mockito (per thesis existing mention in 5.1 测试环境)
- **D-04:** White-box report in thesis shows: test design methodology, modules tested, test case structure, pass/fail results

### Black-Box Testing Report (TH-10)
- **D-05:** Expand existing 5.2 功能测试 into formal test case tables
  - Table columns: Test Case ID, Feature, Input, Expected Output, Actual Result, Pass/Fail
- **D-06:** Cover all 8 system features:
  1. 用户管理 (user login/register)
  2. 身体指标 (body indicator recording/viewing/editing/deleting)
  3. 运动记录 (sport record management)
  4. 饮食记录 (diet record management)
  5. 睡眠记录 (sleep record management)
  6. 健康分析 (health analytics viewing)
  7. AI健康报告 (AI report generation)
  8. 论坛 (forum post/comment management)
- **D-07:** Each feature gets 2-4 formal test cases (normal flow + edge cases)

### Program Flowcharts (MG-07, MG-08)
- **D-08:** Mermaid `graph TD` syntax — consistent with Phase 1 & 2 diagram conventions
- **D-09:** Figure caption format: `**图3-N 程序流程图**` (Chapter 3 detailed design section)
- **D-10:** Placement: New section 3.4 (详细设计) — per Chinese thesis conventions, detailed design flowcharts belong in Chapter 3, not Chapter 5
- **D-11:** MG-07: Body indicator recording flowchart
  - Flow: start → select date → input metrics → validate → [pass: save to DB + confirm] / [fail: show error message]
  - Include decision branches for validation failure and empty data
- **D-12:** MG-08: Health data analysis flowchart
  - Flow: start → retrieve data → aggregate → generate chart → display → [user interaction: filter/refresh] / [end]
  - Include decision branches for insufficient data and chart rendering errors

### Document Quality (DQ-01 to DQ-04)
- **D-13:** DQ-01: Mermaid rendering — all Phase 2 diagrams already verified
- **D-14:** DQ-02: Table formatting — verify all tables have consistent formatting across all chapters (Chapter 1-6)
- **D-15:** DQ-03: Heading hierarchy — verify `1.1`, `1.2`, `3.1`, `3.1.1` numbering is consistent throughout
- **D-16:** DQ-04: Cross-references — add "如图X-X所示" / "如表X-X所示" references where content links to figures/tables; verify existing cross-refs are accurate

### Claude's Discretion
- Exact Mermaid node/edge styling (colors, shapes) for flowcharts — planner decides
- Specific edge case scenarios for each service (white-box) — planner decides based on service logic
- Table border style and formatting details — planner decides
- Specific cross-reference locations — planner decides from content analysis

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Thesis Document
- `毕业论文初稿.md` — Existing thesis with Chapter 5 (系统测试) containing 5.1 测试环境, 5.2 功能测试, 5.3 性能测试
  - Existing 5.2 功能测试 text: lines ~781-812 — expand into formal tables
  - Program flowcharts to be added as new section 3.4

### Prior Phase Context
- `.planning/phases/01-thesis-foundation/01-CONTEXT.md` — Phase 1 decisions (Mermaid syntax, figure format `**图X-N**`)
- `.planning/phases/02-diagram-integration/02-CONTEXT.md` — Phase 2 decisions (9 entities, 6 modules, sequence diagram specs)

### Requirements
- `.planning/REQUIREMENTS.md` — TH-08, TH-09, TH-10, MG-07, MG-08, DQ-01, DQ-02, DQ-03, DQ-04

### Backend Code (for unit tests)
- `backend/src/main/java/com/healthy/service/` — Service interfaces
- `backend/src/main/java/com/healthy/service/impl/` — Service implementations to test
- `backend/src/main/java/com/healthy/entity/` — Domain entities

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- Thesis existing 5.1 测试环境 mentions: Vitest (前端单元测试), JUnit 5 (后端单元测试), Postman (API测试) — tooling already documented
- Existing 5.2 功能测试 text can be converted into formal table rows

### Established Patterns
- Mermaid `graph TD` syntax — confirmed working in thesis (Phase 1 & 2 diagrams)
- Figure captions use bold `**图X-N**` format
- 6 system modules from Phase 2: 用户管理, 健康记录, 健康分析, AI健康报告, 健康论坛, 系统公告

### Integration Points
- Unit tests will run against: `backend/src/main/java/com/healthy/service/impl/`
- Flowcharts describe program logic for: HealthIndicator recording + Analytics data retrieval
- Black-box tables expand existing 5.2 content

</codebase_context>

<specifics>
## Specific Ideas

- Black-box test case naming: TC-功能编号-序号 (e.g., TC-01-01 for user login happy path)
- Flowchart Mermaid style: `graph TD` with rounded rectangles for processes, diamonds for decisions, rectangles with horizontal lines for start/end
- All 5 services to unit test: UserService, HealthIndicatorService, SportRecordService, DietRecordService, SleepRecordService

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope

---

*Phase: 03-testing-polish*
*Context gathered: 2026-04-20*
