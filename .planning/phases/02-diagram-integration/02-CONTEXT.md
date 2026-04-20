# Phase 2: Diagram Integration - Context

**Gathered:** 2026-04-20
**Status:** Ready for planning

<domain>
## Phase Boundary

Add all required Mermaid diagrams to appropriate thesis chapters. Scope: TH-05 (function module diagram), TH-06 (ER diagram), TH-07 (sequence diagrams). Flowcharts (MG-07, MG-08) belong in Phase 3.

</domain>

<decisions>
## Implementation Decisions

### Diagram Style
- **D-01:** Function module diagram — Detailed decomposition, each module shows sub-components and data flows
- **D-02:** ER diagram — All 9 entities included (sys_user, health_indicator, sport_record, diet_record, sleep_record, forum_post, forum_comment, sys_notice, health_plan)
- **D-03:** Sequence diagrams — All 3 flows: (1) 用户登录流程, (2) 健康记录创建, (3) 健康数据分析

### Diagram Format
- **D-04:** Mermaid syntax — `graph TD` for module/use case diagrams, `erDiagram` for ER, `sequenceDiagram` for sequences
- **D-05:** Figure numbering — `图X-N 系统用例图` / `图X-N 系统功能模块图` / etc. (consistent with Phase 1 use case diagram)
- **D-06:** Diagram placement — Insert before the section they illustrate, with figure caption heading

### Chapter Placement
- Function module diagram → Section 3.2 (系统功能设计)
- ER diagram → Section 3.3 (数据库设计)
- Sequence diagrams → Relevant subsections in Section 3.2 or 4.x

### Claude's Discretion
- Exact internal module decomposition details — researcher/planner decides based on existing system architecture
- Specific ER relationship cardinality notation (1:1, 1:N, N:M) — planner decides per relationship
- Sequence diagram actor/object lifelines and message arrow styles — planner uses Mermaid standard

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Thesis Structure
- `毕业论文初稿.md` — Current thesis with Phase 1 enhancements (Section 1.1 market data, Section 3.1 feasibility, use case diagram in 3.2)
- `.planning/phases/01-thesis-foundation/01-CONTEXT.md` — Phase 1 decisions (Mermaid syntax, figure format)
- `.planning/phases/01-thesis-foundation/01-RESEARCH.md` — Research on digital health platforms

### Requirements
- `.planning/REQUIREMENTS.md` — TH-05, TH-06, TH-07 requirements with specific diagram specs

### Existing Code (for diagram accuracy)
- `src/` — Frontend Vue components (for module diagram)
- `src/main.js` — Router structure
- `src/router/` — Route definitions showing module organization
- `src/api/` — API endpoint structure
- `src/views/` — Page/component organization

### Thesis Templates
- `毕业论文初稿.md` — Note figure captions use `**图X-N**` bold format
- Phase 1 use case diagram at `毕业论文初稿.md` line ~215 — reference for Mermaid style and placement

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- Phase 1 use case diagram in `毕业论文初稿.md` — template for Mermaid syntax and figure caption format

### Established Patterns
- Mermaid `graph TD` syntax — confirmed working in thesis
- Figure captions use bold `**图X-N 系统XXX图**` format before code block
- 6 functional modules identified in thesis: 用户管理, 健康记录, 健康分析, AI健康报告, 健康论坛, 系统公告

### Integration Points
- Function module diagram connects to Section 3.2 system architecture description
- ER diagram connects to Section 3.3 table definitions (9 tables already documented in thesis)
- Sequence diagrams connect to Section 4.x implementation descriptions

</codebase_context>

<specifics>
## Specific Ideas

- All 9 entities from existing database table definitions in thesis: sys_user, health_indicator, sport_record, diet_record, sleep_record, forum_post, forum_comment, sys_notice, health_plan
- 6 system modules: 用户管理, 健康记录, 健康分析, AI健康报告, 健康论坛, 系统公告
- 3 sequence flows: login (JWT auth), health record CRUD, analytics data retrieval

</specifics>

<deferred>
## Deferred Ideas

### Phase 3 (Testing & Polish)
- MG-07: Program flowchart for body indicator recording — Phase 3
- MG-08: Program flowchart for health data analysis — Phase 3

None — discussion stayed within phase scope

</deferred>
