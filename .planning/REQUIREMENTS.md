# Requirements — v1.0 Health Dashboard Upgrade

## Categories

### UI Modernization
**Table stakes:** Custom color scheme, improved typography, better spacing
**Differentiators:** Smooth animations, modern card designs, responsive layout overhaul

### Analytics
**Table stakes:** Trend line charts (7/30-day), summary statistics (avg, min, max, total)
**Differentiators:** Comparative views, goal tracking visualizations

### Health Metrics
**Table stakes:** Additional fields for blood pressure, heart rate
**Differentiators:** Blood sugar tracking, mood/stress ratings, hydration tracking

---

## v1.0 Requirements

### UI Modernization

- [x] **UI-01**: Apply custom color scheme with health-focused palette (greens, blues)
- [x] **UI-02**: Improve typography with modern fonts (e.g., Inter, system-ui)
- [x] **UI-03**: Redesign dashboard cards with shadows, rounded corners, hover effects
- [x] **UI-04**: Add smooth page transitions and micro-animations
- [x] **UI-05**: Improve responsive layout for mobile/tablet

### Analytics & Visualization

- [ ] **AN-01**: Display 7-day trend chart for each health metric category
- [ ] **AN-02**: Display 30-day trend chart for each health metric category
- [ ] **AN-03**: Show summary statistics (average, min, max) for current month
- [ ] **AN-04**: Display total counts (e.g., total workouts, total sleep hours)
- [ ] **AN-05**: Add date range selector for custom analytics periods

### Health Metrics Expansion

- [x] **HM-01**: Track blood pressure (systolic/diastolic mmHg)
- [x] **HM-02**: Track heart rate (bpm)
- [x] **HM-03**: Track blood sugar (mg/dL)
- [x] **HM-04**: Track mood rating (1-5 scale with emoji indicators)
- [x] **HM-05**: Track stress level (1-5 scale)
- [x] **HM-06**: Track hydration (glasses or ml)

### Data Management

- [x] **DM-01**: User can add new health records with all metric fields
- [ ] **DM-02**: User can view/edit existing health records
- [ ] **DM-03**: User can delete health records
- [ ] **DM-04**: Form validation for all numeric fields (reasonable ranges)
- [ ] **DM-05**: Date picker for record selection

---

## Future Requirements (Deferred)

- [ ] Blood oxygen (SpO2) tracking
- [ ] Exercise distance/pace tracking
- [ ] Water intake reminders
- [ ] Sleep quality breakdown (deep/light/REM)
- [ ] Export data to PDF/CSV
- [ ] Health insights/AI recommendations

## Out of Scope

- **Authentication/Authorization** — Explicitly excluded per user request
- **Admin dashboard improvements** — Focus is on user-facing features
- **Social features** — Forum deprioritized in this milestone
- **Push notifications** — No reminders in v1.0

---

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| UI-01 | Phase 1 | Complete |
| UI-02 | Phase 1 | Complete |
| UI-03 | Phase 1 | Complete |
| UI-04 | Phase 1 | Complete |
| UI-05 | Phase 1 | Complete |
| HM-01 | Phase 2 | Complete |
| HM-02 | Phase 2 | Complete |
| HM-03 | Phase 2 | Complete |
| HM-04 | Phase 2 | Complete |
| HM-05 | Phase 2 | Complete |
| HM-06 | Phase 2 | Complete |
| AN-01 | Phase 3 | Pending |
| AN-02 | Phase 3 | Pending |
| AN-03 | Phase 3 | Pending |
| AN-04 | Phase 3 | Pending |
| AN-05 | Phase 3 | Pending |
| DM-01 | Phase 2 | Complete |
| DM-02 | Phase 3 | Pending |
| DM-03 | Phase 3 | Pending |
| DM-04 | Phase 2 | Pending |
| DM-05 | Phase 2 | Pending |
