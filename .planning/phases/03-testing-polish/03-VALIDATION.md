---
phase: 03
slug: testing-polish
status: draft
nyquist_compliant: false
wave_0_complete: false
created: 2026-04-20
---

# Phase 03 — Validation Strategy

> Per-phase validation contract for feedback sampling during execution.

---

## Test Infrastructure

| Property | Value |
|----------|-------|
| **Framework** | Manual document verification + Mermaid rendering check |
| **Config file** | N/A — document phase |
| **Quick run command** | `grep -c "```mermaid" 毕业论文初稿.md` |
| **Full suite command** | `grep -c "^## " 毕业论文初稿.md && grep -c "| " 毕业论文初稿.md` |
| **Estimated runtime** | ~30 seconds |

---

## Sampling Rate

- **After each document task commit:** Verify Mermaid blocks render, check table count
- **After each plan wave:** Full document structure audit
- **Before `/gsd-verify-work`:** All diagrams verified, all tables formatted
- **Max feedback latency:** 60 seconds

---

## Per-Task Verification Map

| Task ID | Plan | Wave | Requirement | Threat Ref | Secure Behavior | Test Type | Automated Command | File Exists | Status |
|---------|------|------|-------------|------------|-----------------|-----------|-------------------|-------------|--------|
| 03-01-01 | 01 | 1 | TH-09 | — | N/A | unit | `mvn test -Dtest=*ServiceTest 2>&1 | tail -5` | ✅ | ⬜ pending |
| 03-01-02 | 01 | 1 | TH-09 | — | N/A | unit | `mvn test -Dtest=*ServiceTest 2>&1 | tail -5` | ✅ | ⬜ pending |
| 03-02-01 | 02 | 1 | TH-10, DQ-02 | — | N/A | doc | `grep -c "| " 毕业论文初稿.md` | ✅ | ⬜ pending |
| 03-03-01 | 03 | 1 | MG-07, MG-08, TH-08 | — | N/A | doc | `grep -c "graph TD" 毕业论文初稿.md` | ✅ | ⬜ pending |
| 03-04-01 | 04 | 1 | DQ-01, DQ-03, DQ-04 | — | N/A | doc | `grep -E "(如图|如表)X-X所示" 毕业论文初稿.md | wc -l` | ✅ | ⬜ pending |

*Status: ⬜ pending · ✅ green · ❌ red · ⚠️ flaky*

---

## Wave 0 Requirements

- [ ] Unit test files created in `backend/src/test/java/com/healthy/service/`
- [ ] `mvn test` runs without compilation errors
- [ ] Thesis chapter 5 has test report tables

*If none: "Existing infrastructure covers all phase requirements."*

---

## Manual-Only Verifications

| Behavior | Requirement | Why Manual | Test Instructions |
|----------|-------------|------------|-------------------|
| Mermaid diagram rendering | DQ-01 | Requires visual preview | Open 毕业论文初稿.md in markdown viewer |
| Table alignment in thesis | DQ-02 | Visual formatting check | Open 毕业论文初稿.md and verify table borders |
| Heading hierarchy | DQ-03 | Visual hierarchy check | Verify 1.1, 1.2, 3.1.1 numbering is consistent |
| Cross-reference accuracy | DQ-04 | Must match actual figure numbers | Verify "如图3-1所示" references exist |

*If none: "All phase behaviors have automated verification."*

---

## Validation Sign-Off

- [ ] All tasks have `<automated>` verify or Wave 0 dependencies
- [ ] Sampling continuity: no 3 consecutive tasks without automated verify
- [ ] Wave 0 covers all MISSING references
- [ ] No watch-mode flags
- [ ] Feedback latency < 60s
- [ ] `nyquist_compliant: true` set in frontmatter

**Approval:** pending
