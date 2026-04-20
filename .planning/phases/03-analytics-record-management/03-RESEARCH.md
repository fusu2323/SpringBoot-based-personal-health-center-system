# Phase 3: Analytics & Record Management - Research

**Researched:** 2026-04-05
**Domain:** Health data visualization (trend charts, statistics) + record CRUD
**Confidence:** HIGH

## Summary

Phase 3 requires building analytics capabilities (trend charts, summary statistics, date filtering) on top of the existing health record infrastructure. The existing HealthRecord.vue already implements DM-02 (edit) and DM-03 (delete), so those requirements are satisfied. The primary work is adding: (1) backend analytics API endpoints that aggregate data over 7/30-day windows with optional date range filtering, and (2) a frontend analytics view with ECharts for all 4 metric categories (indicator, sport, diet, sleep).

**Primary recommendation:** Build a dedicated AnalyticsView.vue page that fetches from new backend `/analytics/*` endpoints. Reuse the existing ECharts v6 pattern from Home.vue. Add date range filtering via Element Plus `el-date-picker` with type="daterange".

## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| AN-01 | Display 7-day trend chart for each health metric category | Backend needs aggregation by date; frontend renders via ECharts line charts (existing pattern in Home.vue) |
| AN-02 | Display 30-day trend chart for each health metric category | Same as AN-01 with extended date window |
| AN-03 | Show summary statistics (avg, min, max) for current month | Backend calculates stats; frontend displays in stat cards |
| AN-04 | Display total counts (total workouts, total sleep hours) | Backend sums totals; frontend shows in dashboard cards |
| AN-05 | Add date range selector for custom analytics periods | Element Plus `el-date-picker` type="daterange" already available |
| DM-02 | User can view/edit existing health records | **Already implemented in HealthRecord.vue** - handleEdit() and dialog (lines 323-327) |
| DM-03 | User can delete health records | **Already implemented in HealthRecord.vue** - handleDelete() (lines 329-340) |

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| echarts | ^6.0.0 | Charting | Already installed; Home.vue uses it with `echarts.init()` and `chart.setOption()` |
| element-plus | ^2.12.0 | Date picker, UI components | Already installed; el-date-picker supports daterange mode |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| Element Plus el-date-picker | (bundled) | Date range selection | AN-05 - filter analytics by custom period |

**No new dependencies needed.** ECharts 6.x and Element Plus 2.12.x are already in package.json.

## Architecture Patterns

### Recommended Project Structure
```
frontend/src/views/
├── Analytics.vue          # NEW - Dedicated analytics page with charts and stats
├── Home.vue               # MODIFY - Add stats summary cards, integrate analytics
└── HealthRecord.vue       # ALREADY DONE - edit/delete working

backend/src/main/java/com/healthy/
├── controller/
│   └── AnalyticsController.java   # NEW - analytics API endpoints
├── service/
│   ├── AnalyticsService.java      # NEW - interface
│   └── impl/AnalyticsServiceImpl.java  # NEW - stats calculations
└── entity/
    └── AnalyticsData.java        # NEW - aggregated response DTO
```

### Pattern 1: ECharts Line Chart (from Home.vue lines 500-525)
**What:** Initialize chart with `echarts.init()`, configure with `setOption()`.
**When to use:** Trend charts for 7/30-day periods.
**Source:** Home.vue `initBpChart()` and `initSportChart()` - established pattern in codebase.

```javascript
// Source: Home.vue line 501
const chart = echarts.init(document.getElementById('bp-chart'))
chart.setOption({
  backgroundColor: 'transparent',
  grid: { top: 20, right: 20, bottom: 30, left: 50 },
  tooltip: { trigger: 'axis', ... },
  xAxis: { type: 'category', data: data.dates, ... },
  yAxis: { type: 'value', ... },
  series: [{ name: 'low', type: 'line', data: data.low, smooth: true, ... }]
})
```

### Pattern 2: Backend Aggregation by Date Range
**What:** Group records by date and compute sum/avg/min/max per day.
**When to use:** Building analytics data for charts and statistics.
**Source:** HomeServiceImpl.getHomeStats() lines 75-86 (sport grouping by date) - existing pattern.

```java
// Source: HomeServiceImpl.java lines 75-86 (adapted)
Map<LocalDate, Integer> sportMap = sports.stream()
    .filter(s -> !s.getRecordDate().isBefore(startDate) && !s.getRecordDate().isAfter(endDate))
    .collect(Collectors.groupingBy(SportRecord::getRecordDate, Collectors.summingInt(SportRecord::getDuration)));
```

### Pattern 3: Statistics Calculation
**What:** Stream records within date range, compute DoubleSummaryStatistics.
**When to use:** AN-03 (avg, min, max) and AN-04 (total counts).
**Example:**
```java
DoubleSummaryStatistics stats = indicators.stream()
    .filter(i -> /* within date range */)
    .mapToDouble(HealthIndicator::getBloodSugar)
    .summaryStatistics();
double avg = stats.getAverage();
double min = stats.getMin();
double max = stats.getMax();
```

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Date range filtering | Custom date parsing | Element Plus el-date-picker type="daterange" | Already in codebase; handles locale, formatting, validation |
| Chart rendering | Canvas manual drawing | ECharts 6.x | Already installed; Home.vue proves the pattern works |
| Statistical aggregation | Manual loops | Java Stream + Collectors.groupingBy + DoubleSummaryStatistics | Built into JDK; less error-prone |

**Key insight:** The codebase already uses ECharts for 2 charts in Home.vue and Element Plus for forms. No new charting or UI libraries are needed.

## Common Pitfalls

### Pitfall 1: Chart containers not initializing (size 0)
**What goes wrong:** ECharts init on a hidden container (e.g., inside a non-active tab) results in a chart with 0x0 size.
**Why it happens:** ECharts measures the container at init time. If the container is `display: none`, it has no dimensions.
**How to avoid:** Call `chart.resize()` after the container becomes visible, or use Element Plus tabs with `el-tab-pane` lazy loading.
**Warning signs:** Charts appear blank or only show axes with no data.

### Pitfall 2: Blood pressure parsing assumes "/" separator
**What goes wrong:** HomeServiceImpl line 51 parses `bloodPressure` as `"120/80"` using split("/"). If the string is null or malformed, it adds 0s.
**Why it happens:** The HealthIndicator entity stores BP as a string `"systolic/diastolic"` but HealthRecord.vue line 345 sets it as `form.systolic + '/' + form.diastolic`. Non-numeric input could cause NumberUtil.isNumber() to fail.
**How to avoid:** Verify BP parsing handles edge cases. The new analytics backend should handle null/empty BP gracefully.
**Warning signs:** Chart shows flat line at 0.

### Pitfall 3: Mixing LocalDate and LocalDateTime for date filtering
**What goes wrong:** SportRecord.recordDate is LocalDate, but sleepTime/wakeTime in SleepRecord are LocalDateTime. Comparing them directly can cause off-by-one errors at timezone boundaries.
**Why it happens:** Different entity fields use different date types.
**How to avoid:** Normalize to LocalDate for grouping. Use `.toLocalDate()` on LocalDateTime fields when grouping.

### Pitfall 4: Frontend date range picker sends wrong format
**What goes wrong:** Element Plus date range picker returns `Date[]` but backend expects `yyyy-MM-dd` strings.
**How to avoid:** Use `value-format="YYYY-MM-DD"` on the picker and format params before sending to backend.

## Code Examples

### Frontend: Date Range Picker (Element Plus)
```vue
<!-- Source: Element Plus docs - el-date-picker type="daterange" -->
<el-date-picker
  v-model="dateRange"
  type="daterange"
  range-separator="至"
  start-placeholder="开始日期"
  end-placeholder="结束日期"
  value-format="YYYY-MM-DD"
/>
```

### Frontend: Resize ECharts on tab switch
```javascript
// When switching to a chart tab, call resize
const onChartTabChange = () => {
  nextTick(() => {
    bpChartInstance?.resize()
    sportChartInstance?.resize()
  })
}
```

### Backend: Analytics aggregation endpoint
```java
// New endpoint in AnalyticsController.java
@GetMapping("/indicator/stats")
public Result getIndicatorStats(
    @RequestParam Long userId,
    @RequestParam(required = false) String startDate,
    @RequestParam(required = false) String endDate) {
    // Returns { dates[], systolic[], diastolic[], heartRate[], bloodSugar[], ... }
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Hardcoded 7-day only | Configurable 7/30/custom day range | This phase | Users can see longer trends |
| No statistics | Stats cards with avg/min/max | This phase | Immediate insight without calculation |
| BP chart only in Home | All metrics charted in dedicated view | This phase | Complete picture of health trends |

**Deprecated/outdated:**
- None relevant to this phase.

## Open Questions

1. **Should Analytics be a new route or integrated into Home.vue?**
   - What we know: Home.vue has 2 placeholder chart divs (#bp-chart, #sport-chart) that load 7-day data. Charts section is below the fold.
   - What's unclear: Whether the analytics view should be a separate page or a tab within Home. Home.vue already has a `charts-section`.
   - Recommendation: Create a new `/layout/analytics` route with a dedicated Analytics.vue view. Home.vue stays focused on the hero/dashboard experience. This separates concerns and keeps Home.vue from growing larger.

2. **Should statistics (AN-03, AN-04) live on Home.vue dashboard or on Analytics.vue?**
   - What we know: Home.vue already has mini-stat cards in the sidebar (todayStats sleep/sport/calories).
   - What's unclear: Whether to reuse the Home.vue sidebar pattern for stats or build a new stats section on Analytics.vue.
   - Recommendation: Put AN-03/AN-04 on Analytics.vue to keep analytics data grouped. Home.vue stays as the quick-glance dashboard.

3. **Edit record - should it open in the same dialog or a separate page?**
   - What we know: HealthRecord.vue has the dialog open on `handleEdit()` for all 4 tabs.
   - What's unclear: Whether adding records and editing records should share a dialog.
   - Recommendation: Already implemented - no change needed. DM-02 is DONE.

## Environment Availability

| Dependency | Required By | Available | Version | Fallback |
|------------|------------|-----------|---------|----------|
| echarts | Charts (AN-01, AN-02) | Yes | 6.0.0 | — |
| element-plus | Date picker (AN-05), UI components | Yes | 2.12.0 | — |
| animejs | Animations (Home.vue) | Yes | 3.2.2 | — |
| Java/Spring Boot | Backend API | Yes | (project uses Maven) | — |

**No missing dependencies.**

## Validation Architecture

### Test Framework
| Property | Value |
|----------|-------|
| Framework | None detected - no test infrastructure in project |
| Config file | — |
| Quick run command | — |
| Full suite command | — |

### Phase Requirements to Test Map
| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| AN-01 | 7-day trend chart renders for each metric category | Manual | N/A - visual inspection | N/A |
| AN-02 | 30-day trend chart renders for each metric category | Manual | N/A - visual inspection | N/A |
| AN-03 | Summary stats (avg/min/max) display for current month | Manual | N/A - visual inspection | N/A |
| AN-04 | Total counts display on dashboard | Manual | N/A - visual inspection | N/A |
| AN-05 | Date range selector filters analytics | Manual | N/A - UI interaction | N/A |
| DM-02 | Edit existing record opens dialog with populated data | Manual | N/A - HealthRecord.vue already has this | N/A |
| DM-03 | Delete record removes it after confirmation | Manual | N/A - HealthRecord.vue already has this | N/A |

**Note:** The project has no automated test infrastructure (no jest, vitest, pytest, etc.). All validation is manual. For Phase 3, manual verification against success criteria is appropriate given the primarily visual nature of charts and dialogs.

### Wave 0 Gaps
- None — project has no existing test infrastructure to extend.

## Sources

### Primary (HIGH confidence)
- Home.vue (lines 500-525) — ECharts initialization pattern, confirmed in codebase
- HealthRecord.vue (lines 323-340) — edit/delete already implemented, DM-02/DM-03 confirmed DONE
- RecordController.java — existing REST endpoints, confirmed API contract
- HomeServiceImpl.java (lines 75-86) — date-based aggregation pattern, confirmed in codebase
- package.json — echarts ^6.0.0, element-plus ^2.12.0 confirmed installed

### Secondary (MEDIUM confidence)
- Element Plus el-date-picker documentation — type="daterange" is standard component

### Tertiary (LOW confidence)
- None

## Metadata

**Confidence breakdown:**
- Standard Stack: HIGH — all dependencies already in package.json
- Architecture: HIGH — existing patterns from Home.vue and HealthRecord.vue
- Pitfalls: HIGH — issues identified from existing code review

**Research date:** 2026-04-05
**Valid until:** 2026-05-05 (30 days — no major library updates expected in this stack)
