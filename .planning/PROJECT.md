# Health Dashboard

## What This Is

A health management platform for tracking body metrics, exercise, diet, sleep, and more — with modern data visualization and analytics. Targets health-conscious individuals who want to track and understand their health trends over time.

## Core Value

Help users see their health patterns clearly so they can make informed decisions about their wellness.

## Requirements

### Validated

(None yet — initial version)

### Active

- [ ] Modern UI design with contemporary visual language
- [ ] Trend charts for all health metrics (7-day, 30-day views)
- [ ] Summary statistics (averages, totals, min/max)
- [ ] Additional health metrics: blood pressure, heart rate, blood sugar, mood/stress
- [ ] Expanded data tracking with better UX

### Out of Scope

- User authentication — explicitly excluded per user request
- Admin dashboard improvements — focus is on user-facing features

## Context

- Tech stack: SpringBoot 3 + Vue 3 + Element Plus + MySQL + ECharts
- Existing features: basic CRUD for health metrics (body, exercise, diet, sleep), forum
- UI currently uses default Element Plus styling — needs modernization
- Backend has basic data structure — needs richer analytics endpoints

## Constraints

- **No Auth**: User explicitly requested no authentication
- **Tech Stack**: Must use existing SpringBoot + Vue3 stack
- **Compatibility**: Should work with existing database schema where possible

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| No auth | User finds auth annoying, not needed for MVP | — Pending |
| Modern UI | Replace basic Element Plus default with custom styling | — Pending |
| Expanded metrics | Add BP, HR, blood sugar, mood/stress | — Pending |
| Analytics focus | Trend charts + summary stats over AI insights | — Pending |

---

*Last updated: 2026-04-01 after v1.0 milestone started*

## Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `/gsd:transition`):
1. Requirements invalidated? → Move to Out of Scope with reason
2. Requirements validated? → Move to Validated with phase reference
3. New requirements emerged? → Add to Active
4. Decisions to log? → Add to Key Decisions
5. "What This Is" still accurate? → Update if drifted

**After each milestone** (via `/gsd:complete-milestone`):
1. Full review of all sections
2. Core Value check — still the right priority?
3. Audit Out of Scope — reasons still valid?
4. Update Context with current state
