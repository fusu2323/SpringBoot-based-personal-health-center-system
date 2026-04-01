# Phase 1: UI Modernization - Context

**Gathered:** 2026-04-01
**Status:** Ready for planning

<domain>
## Phase Boundary

Dashboard has modern, health-focused visual design with smooth interactions. Applies custom color scheme, typography, card redesigns, animations, and responsive layout overhaul. Phase 2 (Health Metrics) depends on this — new forms should use the modernized UI system.

</domain>

<decisions>
## Implementation Decisions

### Color Palette
- **D-01:** Primary color: Soft teal/emerald (#10B981) — health-focused, calming
- **D-02:** Accent color: Blue (#3B82F6) — for interactive elements and highlights
- **D-03:** Background: Light gray (#F8FAFC) for app shell, white (#FFFFFF) for cards
- **D-04:** Text: Dark slate (#1E293B) primary, muted gray (#64748B) secondary
- **D-05:** Default to light mode; include dark mode toggle in header for future

### Typography
- **D-06:** Font family: Inter (Google Fonts) with system-ui fallback
- **D-07:** Base font size: 16px, line-height 1.6 for readability
- **D-08:** Heading scale: 32px (h1), 24px (h2), 20px (h3) with semi-bold weight

### Card Design
- **D-09:** Card background: White (#FFFFFF) on light gray app background
- **D-10:** Border radius: 12px for cards, 8px for buttons/inputs
- **D-11:** Shadow: Subtle soft shadow `0 4px 6px rgba(0,0,0,0.07), 0 2px 4px rgba(0,0,0,0.05)`
- **D-12:** Hover effect: Slight lift (translateY(-2px)) + shadow increase
- **D-13:** Card padding: 24px consistent

### Animations & Transitions
- **D-14:** Page transitions: Vue `<Transition>` with fade-slide effect (200ms)
- **D-15:** Micro-interactions: CSS transitions 200-300ms ease for hover states
- **D-16:** Card hover: Scale 1.01 + shadow lift on mouse enter
- **D-17:** Button hover: Background darken + slight scale (0.98 press effect)

### Responsive Layout
- **D-18:** Breakpoints: 1200px (desktop), 768px (tablet), 480px (phone)
- **D-19:** Dashboard grid: Responsive card layout adapting from 4-col to 2-col to 1-col
- **D-20:** Use Element Plus `el-row`/`el-col` grid system for layout

### Specific Ideas
- Cards should feel premium but not heavy — light and airy, not Material Design heavy shadows
- Health metrics should use subtle color coding (green = good, amber = warning, red = concern)

### Claude's Discretion
- Exact color hex values within the health-focused palette
- Specific transition timing values (fine-tuning 200ms vs 300ms)
- Icon library choice (Lucide Vue recommended, consistent with modern feel)
- Loading skeleton design approach
- Empty state visual treatment

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

- `.planning/ROADMAP.md` §Phase 1 — Phase 1 goal, success criteria, requirements (UI-01 to UI-05)
- `.planning/REQUIREMENTS.md` §UI Modernization — Requirements UI-01 to UI-05
- `.planning/PROJECT.md` — Core value and vision for health dashboard

### Frontend Code
- `frontend/src/style.css` — Existing base styling to replace
- `frontend/src/views/Home.vue` — Main dashboard view to modernize
- `frontend/src/components/` — Existing components to update
- `frontend/src/App.vue` — Root component
- `frontend/package.json` — Check installed dependencies

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- `frontend/src/views/Layout.vue` — Navigation layout structure
- `frontend/src/views/Home.vue` — Dashboard entry point (primary target for Phase 1)
- Element Plus components: `el-card`, `el-button`, `el-form`, `el-input` — already used
- ECharts: Already integrated for charts (via vue-echarts)

### Established Patterns
- Element Plus component library in use
- Vue Router for page navigation
- CSS custom properties (CSS variables) for theming — should extend this approach
- Dark/light color-scheme already defined in style.css

### Integration Points
- New global styles will apply to all views (App.vue, Layout.vue)
- Cards in Home.vue, HealthRecord.vue, and other views need modernization
- CSS variables should be defined in :root to cascade through all components

</codebase_context>

<specifics>
## Specific Ideas

- User wants "modern" look — use current best practices (Inter, soft shadows, generous whitespace)
- No specific reference designs provided — open to standard modern dashboard aesthetics
- Health dashboard should feel calm and professional, not clinical

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope

</deferred>

---

*Phase: 01-ui-modernization*
*Context gathered: 2026-04-01*
