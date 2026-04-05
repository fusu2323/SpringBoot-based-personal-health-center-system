# Phase 3: Analytics & Record Management - Context

**Gathered:** 2026-04-06
**Status:** Ready for planning

<domain>
## Phase Boundary

Users can view trend charts and statistics for all health metric categories on a dedicated Analytics page. Analytics is accessed via a "View Analytics" CTA from Home.vue chart cards, or directly from the navigation. The Analytics page shows 7/30-day trends, summary statistics, and date range filtering. DM-02 (edit) and DM-03 (delete) are already implemented in HealthRecord.vue — confirmed DONE, no new work needed.
</domain>

<decisions>
## Implementation Decisions

### Layout & Navigation
- **D-01:** Single `/layout/analytics` route with `Analytics.vue` as a new view
- **D-02:** Use `el-tabs` for 4 metric categories: indicator (身体指标), sport (运动), diet (饮食), sleep (睡眠)
- **D-03:** "View Analytics" CTA button on Home.vue chart cards → navigates to `/layout/analytics`
- **D-04:** No separate analytics routes per category — single page with tabs

### Date Filtering UX
- **D-05:** Date range picker at page header (above tabs) — applies globally to all charts and stats
- **D-06:** Period toggle pills: "7天" and "30天" — clicking auto-fills the date range picker
- **D-07:** Default view: last 7 days on first load
- **D-08:** Custom date range via `el-date-picker` type="daterange" — `value-format="YYYY-MM-DD"`

### Chart Interaction
- **D-09:** Enhanced tooltip on hover showing all metric values for that date point
- **D-10:** No click action on chart points — keep it simple, date picker gives control
- **D-11:** Chart resize via `chartInstance.resize()` when switching tabs (in `nextTick()`)

### Stats Presentation
- **D-12:** Summary stats (avg/min/max/total) appear on Analytics.vue below each chart
- **D-13:** Stats scoped to current date range — no separate "all time" view needed
- **D-14:** Stats NOT shown on Home.vue — keep dashboard focused on hero + recent status

### DM-02 / DM-03 Status
- **D-15:** Edit existing records — **ALREADY IMPLEMENTED** in HealthRecord.vue (`handleEdit()` lines 323-327)
- **D-16:** Delete records — **ALREADY IMPLEMENTED** in HealthRecord.vue (`handleDelete()` lines 329-340)
- No new UI work needed for DM-02/DM-03

### Design Consistency
- **D-17:** Reuse sage/terracotta palette from Phase 1 (CSS variables in style.css)
- **D-18:** Reuse card styling (24px radius, soft shadows, card-shadow) from Phase 1
- **D-19:** Reuse ECharts init/setOption pattern from Home.vue lines 500-525
- **D-20:** Reuse stat card pattern from Home.vue mini-stat (`.stat-value` 48px semibold)

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Requirements
- `.planning/REQUIREMENTS.md` — AN-01 through AN-05, DM-02, DM-03

### Prior Phase Context
- `.planning/phases/01-ui-modernization/01-CONTEXT.md` — Sage/terracotta palette, 24px card radius, soft shadows, fade-slide transitions
- `.planning/phases/02-health-metrics-data-foundation/02-CONTEXT.md` — Form/dialog patterns, el-date-picker usage
- `.planning/phases/01-ui-modernization/01-03-SUMMARY.md` — Home.vue chart implementation

### Research & Design
- `.planning/phases/03-analytics-record-management/03-RESEARCH.md` — Architecture patterns, ECharts init pattern, aggregation approach, confirmed DM-02/DM-03 done
- `.planning/phases/03-analytics-record-management/03-UI-SPEC.md` — Design tokens, chart colors, stat card pattern, layout structure

### Existing Code
- `frontend/src/views/Home.vue` — ECharts initialization (lines ~500-525), chart card structure, stat card pattern
- `frontend/src/views/HealthRecord.vue` — Edit/delete already implemented, el-table structure, dialog form layout
- `frontend/src/style.css` — CSS custom properties (--color-sage, --color-terracotta, --card-shadow, spacing tokens)
- `frontend/src/router/index.js` — Existing route structure (add analytics route here)

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- `Home.vue` ECharts init pattern (`echarts.init()` + `chart.setOption()`) — reuse for all trend charts
- Home.vue mini-stat card pattern — `.stat-value` (48px semibold sage) + `.stat-label` (13px muted)
- Home.vue chart card pattern — `.chart-card` (24px radius, shadow, 32px padding)
- Element Plus `el-date-picker` type="daterange" — already used in HealthRecord.vue
- Element Plus `el-tabs` — already used in HealthRecord.vue for categories

### Established Patterns
- Sage primary (#8B9A7D), terracotta accent (#C47D5E) — use for chart lines
- 24px card radius, `var(--card-shadow)` — apply to all analytics cards
- Fade-slide page transitions from Phase 1

### Integration Points
- New `/layout/analytics` route → add to router/index.js children array
- "View Analytics" button → Home.vue chart cards link to `/layout/analytics`
- Backend: new `AnalyticsController.java` + `AnalyticsService.java` for aggregation endpoints
- Frontend API: `request.get('/analytics/indicator', { params: { startDate, endDate } })`
</codebase_context>

<specifics>
## Specific Ideas

- Default analytics period: 7 days (most useful for quick trends)
- Charts use gradient fill under line (terracotta gradient for indicators, sage for sport)
- Stats row: 4 cards — 平均 (avg), 最低 (min), 最高 (max), 总计 (total)
- Empty state: "暂无数据" with illustration when no records in date range
</specifics>

<deferred>
## Deferred Ideas

### Reviewed Todos (not folded)
(None — no todos matched this phase scope)

None — discussion stayed within phase scope.

</deferred>

---

*Phase: 03-analytics-record-management*
*Context gathered: 2026-04-06*
