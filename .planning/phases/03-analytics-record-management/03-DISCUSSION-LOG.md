# Phase 3: Analytics & Record Management - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-06
**Phase:** 03-analytics-record-management
**Areas discussed:** Layout & navigation, Date filtering UX, Chart interaction, Stats presentation

---

## Layout & Navigation

| Option | Description | Selected |
|--------|-------------|----------|
| Single page with tabs (Recommended) | One /layout/analytics route with el-tabs for each category. Consistent with HealthRecord.vue pattern. | ✓ |
| Expand Home.vue | Add analytics section directly to Home.vue dashboard below the hero. Quick glance stays comprehensive. | |
| Separate routes per category | Multiple routes (/analytics/indicator, /analytics/sport, etc.). Most granular but most complex. | |

**User's choice:** Single page with tabs (Recommended)
**Notes:** Consistent with existing HealthRecord.vue pattern. Keep analytics focused and separate from Home dashboard.

---

## Date Filtering UX

| Option | Description | Selected |
|--------|-------------|----------|
| Global picker + quick toggles (Recommended) | Date range picker at top controls all charts. Quick 7/30 day pills auto-fill the picker. | ✓ |
| Picker only | Just a date range picker. User manually sets any start/end. | |
| Per-chart date controls | Each chart/tab has its own date filter. Most flexible but complex. | |

**User's choice:** Global picker + quick toggles (Recommended)
**Notes:** Quick toggles (7天/30天) for fast switching, full date range picker for custom analysis.

---

## Chart Interaction

| Option | Description | Selected |
|--------|-------------|----------|
| Enhanced tooltip only (Recommended) | Hover shows tooltip with all metric values for that date. No click action — keep it simple. | ✓ |
| Click → detail modal | Click a point to see detailed breakdown in a modal. Shows exact values + context. | |
| No interaction | Charts display only. Tooltips off, no click response. Static presentation. | |

**User's choice:** Enhanced tooltip only (Recommended)
**Notes:** Keep it simple. Date picker already gives full control over the time window.

---

## Stats Presentation

| Option | Description | Selected |
|--------|-------------|----------|
| Analytics page only (Recommended) | Stats below each chart on Analytics.vue. Home stays focused on hero + recent data. | ✓ |
| Home dashboard only | Show key stats on Home.vue dashboard. User sees them every visit. | |
| Both Home + Analytics | Stats everywhere they're relevant. Maximum visibility. | |

**User's choice:** Analytics page only (Recommended)
**Notes:** Analytics is where you go to "see your patterns." Home stays focused on the quick-glance dashboard.

---

## Claude's Discretion

No areas deferred to Claude — all decisions made by user.

## Deferred Ideas

None — discussion stayed within phase scope.
