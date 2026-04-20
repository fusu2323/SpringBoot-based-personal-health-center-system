# Phase 3: Testing & Polish - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-20
**Phase:** 03-testing-polish
**Areas discussed:** White-box report structure, Black-box report structure, Flowchart format, Doc quality checks

---

## White-box report structure

| Option | Description | Selected |
|--------|-------------|----------|
| Write actual unit tests | Write unit tests for 3-4 key backend classes (UserController, HealthIndicatorService, SportRecordService, DietRecordService, SleepRecordService) using JUnit 5 + Mockito. White-box report shows actual test results with pass/fail and coverage. | ✓ |
| Describe methodology only | Describe the white-box testing approach: test design methodology, modules tested, test case structure, coverage strategy. No actual test code needed — just methodology documentation. | |

**User's choice:** Write actual unit tests
**Notes:** User wants actual unit tests written, not just methodology description.

---

### Module coverage

| Option | Description | Selected |
|--------|-------------|----------|
| All 5 service classes | Test UserService, HealthIndicatorService, SportRecordService, DietRecordService, SleepRecordService. Comprehensive coverage. | ✓ |
| Health recording services only | Test 2 core services: HealthIndicatorService + SportRecordService. | |
| HealthIndicatorService only | Test single focused service covering MG-07/MG-08 flowchart scope. | |

**User's choice:** All 5 service classes

---

### Test depth per service

| Option | Description | Selected |
|--------|-------------|----------|
| 2-3 cases per service (Recommended) | 2-3 test cases per service (happy path + 1 edge case). Balanced effort-to-coverage ratio. | ✓ |
| 1 case per service | 1 test case per service — just the happy path CRUD flow. | |
| 4-5 cases per service | 4-5 test cases per service covering: happy path, null input, invalid data, edge cases, exception handling. | |

**User's choice:** 2-3 cases per service

---

## Black-box report structure

| Option | Description | Selected |
|--------|-------------|----------|
| Formal tables (Recommended) | Convert existing text into formal tables: Test Case ID, Feature, Input, Expected Output, Actual Result, Pass/Fail. Aligned with academic thesis standards. | ✓ |
| Enhanced descriptions | Keep existing descriptive paragraphs, add section intro explaining test methodology. Less formal. | |

**User's choice:** Formal tables

---

### Black-box feature scope

| Option | Description | Selected |
|--------|-------------|----------|
| All 8 features | Test all 8 features: 用户管理, 身体指标, 运动记录, 饮食记录, 睡眠记录, 健康分析, AI报告, 论坛. | ✓ |
| Body indicator + analytics only | Test only features matching MG-07/MG-08 flowchart scope. | |

**User's choice:** All 8 features

---

## Flowchart format

| Option | Description | Selected |
|--------|-------------|----------|
| Chapter 3 (详细设计) | Add new section 3.4 详细设计 before 4 系统实现. Flowcharts describe program logic — part of detailed design chapter per Chinese thesis conventions. | |
| Chapter 5 (系统测试) | Place in Chapter 5 alongside test reports. Flowcharts document logic being tested. | |
| You decide | Claude decides based on existing thesis structure and Chinese academic conventions. | ✓ |

**User's choice:** You decide
**Notes:** Claude recommended Chapter 3 (详细设计) per Chinese thesis conventions — program flowcharts describing system logic belong in detailed design chapter, not testing chapter.

---

### Flowchart detail level

| Option | Description | Selected |
|--------|-------------|----------|
| Include decision branches | Show: start → select feature → input data → validate → [pass: save + confirm] / [fail: error message]. Shows both success and error paths. | ✓ |
| Happy path only | Show only the happy path: start → input data → save → confirm. | |

**User's choice:** Include decision branches

---

## Doc quality checks

| Option | Description | Selected |
|--------|-------------|----------|
| All remaining checks | Verify table formatting consistency, heading hierarchy, and cross-references across all chapters. | ✓ |
| Chapter 5 new content only | Focus only on new content added in Phase 3 (Chapter 5). | |

**User's choice:** All remaining checks (table formatting, heading hierarchy, cross-references across all chapters)

---

## Claude's Discretion

- Flowchart placement: Chapter 3.4 (详细设计) — per Chinese thesis conventions
- Exact Mermaid node/edge styling for flowcharts — planner decides
- Specific edge case scenarios per service — planner decides based on service logic
- Table border and formatting details — planner decides
- Specific cross-reference locations — planner decides from content analysis

## Deferred Ideas

None — discussion stayed within phase scope.
