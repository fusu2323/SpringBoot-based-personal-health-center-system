# Phase 2: Health Metrics & Data Foundation - Context

**Gathered:** 2026-04-05
**Status:** Ready for planning

<domain>
## Phase Boundary

Users can track new health metrics (blood pressure, heart rate, blood sugar, mood, stress, hydration) with proper form validation and date picker. Extends the existing HealthRecord.vue tabbed interface.
</domain>

<decisions>
## Implementation Decisions

### Form Layout
- **D-01:** Use Element Plus Dialog for add/edit record forms — non-blocking, keeps user in context

### Input Controls
- **D-02:** Number inputs for blood pressure (systolic/diastolic), heart rate (bpm), blood sugar (mg/dL), hydration (ml)
- **D-03:** Emoji picker or segmented button (1-5) for mood rating with visual indicators
- **D-04:** Segmented button (1-5) for stress level with color coding

### Validation Style
- **D-05:** Element Plus built-in form validation — inline error messages below fields
- **D-06:** Numeric range validation: BP (60-200 mmHg), HR (40-200 bpm), blood sugar (50-400 mg/dL), hydration (0-5000ml)

### Date Picker Style
- **D-07:** Element Plus DatePicker with calendar view — allows manual date entry and quick presets (today, yesterday, this week)

### UI Consistency
- **D-08:** Keep consistent with sage/terracotta palette from Phase 1
- **D-09:** Use same card styling (24px radius, soft shadows) for form dialogs

### Existing Codebase Notes
- HealthRecord.vue already exists with el-tabs structure (indicator/sport/diet/sleep)
- Blood pressure, blood sugar, heart rate columns already exist in indicator table
- Missing: mood, stress, hydration fields; add-record dialog; date picker integration
</decisions>

<canonical_refs>
## Canonical References

### Requirements
- `.planning/REQUIREMENTS.md` — HM-01 through HM-06, DM-01, DM-04, DM-05

### Prior Phase Context
- `.planning/phases/01-ui-modernization/01-CONTEXT.md` — UI palette and design decisions
- `.planning/phases/01-ui-modernization/01-02-SUMMARY.md` — Layout.vue implementation
- `.planning/phases/01-ui-modernization/01-03-SUMMARY.md` — Home.vue implementation

### Existing Code
- `frontend/src/views/HealthRecord.vue` — Existing health record page with tabbed interface
- `frontend/src/style.css` — Global CSS variables and design tokens

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- Element Plus `el-dialog`, `el-form`, `el-input-number`, `el-date-picker` components
- Existing tab structure in HealthRecord.vue — add new tabs or extend existing
- Sage/terracotta CSS variables from style.css

### Established Patterns
- Phase 1 established: 24px card radius, soft shadows, fade-slide transitions
- Element Plus form validation pattern already in use in other views
- Tab-based navigation for record categories

### Integration Points
- HealthRecord.vue `handleAdd()` function — needs dialog with form
- Existing API endpoints for health records (to verify)
- Home.vue dashboard cards — may need to display new metrics
</code_context>

<specifics>
## Specific Ideas

- Mood: 5-level scale with emoji faces (😢😕😐🙂😊)
- Stress: 5-level scale with color gradient (green→yellow→red)
- Hydration: ml input with quick-add buttons (+250ml, +500ml)
- BP: Two number inputs (systolic/diastolic) side by side
</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope
</deferred>
