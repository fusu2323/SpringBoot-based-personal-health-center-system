# ROADMAP — v1.0 Health Dashboard Upgrade

## Phases

- [ ] **Phase 1: UI Modernization** — Apply custom color scheme, typography, modern card designs, animations, responsive layout
- [x] **Phase 2: Health Metrics & Data Foundation** — Add blood pressure, heart rate, blood sugar, mood, stress, hydration tracking with form validation and date picker (completed 2026-04-05)
- [ ] **Phase 3: Analytics & Record Management** — Add trend charts, summary statistics, date range selector, edit/delete records

---

## Phase Details

### Phase 1: UI Modernization

**Goal:** Dashboard has modern, health-focused visual design with smooth interactions

**Depends on:** None (first phase)

**Requirements:** UI-01, UI-02, UI-03, UI-04, UI-05

**Success Criteria** (what must be TRUE):
1. Custom health-focused color scheme (greens, blues) visible throughout the dashboard
2. Modern fonts (Inter, system-ui) applied to all text elements
3. Dashboard cards display with shadows, rounded corners, and hover effects
4. Smooth page transitions and micro-animations when navigating between views
5. Layout adapts properly on mobile and tablet viewports

**Plans:** 3/4 plans executed

**Plan list:**
- [x] 01-01-PLAN.md -- Global CSS foundation with color palette and typography
- [x] 01-02-PLAN.md -- Layout.vue navigation modernization
- [x] 01-03-PLAN.md -- Home.vue dashboard card modernization
- [ ] 01-04-PLAN.md -- Human verification checkpoint

**UI hint:** yes

---

### Phase 2: Health Metrics & Data Foundation

**Goal:** Users can track new health metrics (blood pressure, heart rate, blood sugar, mood, stress, hydration) with proper validation

**Depends on:** Phase 1

**Requirements:** HM-01, HM-02, HM-03, HM-04, HM-05, HM-06, DM-01, DM-04, DM-05

**Success Criteria** (what must be TRUE):
1. Blood pressure fields (systolic/diastolic mmHg) appear in the add-record form
2. Heart rate field (bpm) appears in the add-record form
3. Blood sugar field (mg/dL) appears in the add-record form
4. Mood rating selector (1-5 scale with emoji indicators) appears in the add-record form
5. Stress level selector (1-5 scale) appears in the add-record form
6. Hydration input (glasses or ml) appears in the add-record form
7. Form validation rejects out-of-range numeric values with clear error messages
8. Date picker enables calendar-based date selection when adding records

**Plans:** 3/3 plans complete

**Plan list:**
- [x] 02-01-PLAN.md -- Add health metric fields (BP systolic/diastolic, mood, stress, hydration) to indicator tab dialog
- [x] 02-02-PLAN.md -- Add Element Plus validation rules and date picker presets
- [x] 02-03-PLAN.md -- Human verification checkpoint

**UI hint:** yes

---

### Phase 3: Analytics & Record Management

**Goal:** Users can view trend charts, statistics, and manage their existing health records

**Depends on:** Phase 2

**Requirements:** AN-01, AN-02, AN-03, AN-04, AN-05, DM-02, DM-03

**Success Criteria** (what must be TRUE):
1. 7-day trend chart displays for each health metric category
2. 30-day trend chart displays for each health metric category
3. Summary statistics (average, min, max) display for the current month
4. Total counts (e.g., total workouts, total sleep hours) display on the dashboard
5. Date range selector allows filtering analytics to custom periods
6. User can view and edit existing health records (ALREADY DONE in HealthRecord.vue)
7. User can delete health records from the system (ALREADY DONE in HealthRecord.vue)

**Plans:** 1/4 plans executed

**Plan list:**
- [ ] 03-01-PLAN.md -- Backend analytics aggregation (DTO, service, controller)
- [x] 03-02-PLAN.md -- Frontend Analytics.vue with charts, tabs, date picker
- [ ] 03-03-PLAN.md -- Router integration and Home.vue CTA button
- [ ] 03-04-PLAN.md -- Human verification checkpoint

---

## Progress Table

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. UI Modernization | 3/4 | In Progress|  |
| 2. Health Metrics & Data Foundation | 3/3 | Complete   | 2026-04-05 |
| 3. Analytics & Record Management | 1/4 | In Progress|  |

---

## Coverage

**Total v1.0 requirements:** 21

| Phase | Requirements |
|-------|--------------|
| 1. UI Modernization | UI-01, UI-02, UI-03, UI-04, UI-05 |
| 2. Health Metrics & Data Foundation | HM-01, HM-02, HM-03, HM-04, HM-05, HM-06, DM-01, DM-04, DM-05 |
| 3. Analytics & Record Management | AN-01, AN-02, AN-03, AN-04, AN-05, DM-02, DM-03 |

**Coverage:** 21/21 requirements mapped

**Verification:**
- UI-01 to UI-05 -> Phase 1 (5 requirements)
- HM-01 to HM-06 -> Phase 2 (6 requirements)
- AN-01 to AN-05 -> Phase 3 (5 requirements)
- DM-01, DM-04, DM-05 -> Phase 2 (3 requirements)
- DM-02, DM-03 -> Phase 3 (2 requirements) - ALREADY DONE in HealthRecord.vue
- Total: 5 + 6 + 5 + 3 + 2 = 21 requirements
