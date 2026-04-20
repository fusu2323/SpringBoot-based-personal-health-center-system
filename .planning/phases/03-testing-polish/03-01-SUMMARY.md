---
phase: "03-testing-polish"
plan: "01"
subsystem: testing
tags: [junit5, mockito, spring-boot, java, unit-test]

# Dependency graph
requires:
  - phase: "02-diagram-integration"
    provides: Backend service implementations (SysUserServiceImpl, HealthServiceImpl)
provides:
  - JUnit 5 + Mockito unit tests for SysUserService (12 test methods)
  - JUnit 5 + Mockito unit tests for HealthService (20 test methods)
affects: [testing, thesis-chapter-5]

# Tech tracking
tech-stack:
  added: []
  patterns:
    - "MockitoExtension for JUnit 5 mocking"
    - "@Mock/@InjectMocks dependency injection pattern"
    - "doNothing().when(...).method() for void mapper stubs"
    - "any(Entity.class) argument matchers"

key-files:
  created:
    - "backend/src/test/java/com/healthy/service/SysUserServiceTest.java"
    - "backend/src/test/java/com/healthy/service/HealthServiceTest.java"

key-decisions:
  - "Used doNothing().when(...) instead of when(...).thenReturn(...) for void mapper delete methods"
  - "Set non-blank password in testRegister_DuplicateUsername to avoid triggering blank-field validation first"

patterns-established:
  - "Pattern 1: @ExtendWith(MockitoExtension.class) as class-level JUnit 5 extension"
  - "Pattern 2: @Mock for mapper interface, @InjectMocks for service implementation"
  - "Pattern 3: verify(...).method(args) to assert interaction counts"

requirements-completed: ["TH-09"]

# Metrics
duration: 8min
completed: 2026-04-20
---

# Phase 03: Testing Polish Summary

**JUnit 5 + Mockito unit tests for SysUserService (12 tests) and HealthService (20 tests) covering all CRUD operations with happy-path and edge cases**

## Performance

- **Duration:** 8 min
- **Started:** 2026-04-20T09:23:29Z
- **Completed:** 2026-04-20T09:31:00Z
- **Tasks:** 2
- **Files modified:** 2

## Accomplishments
- SysUserServiceTest: 12 test methods covering login (success/wrong password/user not found), register (success/duplicate/blank username), addUser, updateUser, deleteUser, getUserById (found/not found), getAllUsers
- HealthServiceTest: 20 test methods covering HealthIndicator, SportRecord, DietRecord, SleepRecord CRUD (get list empty/non-empty, save insert, save update, delete) for each record type
- All 32 tests pass with BUILD SUCCESS
- Tests follow JUnit 5 + MockitoExtension pattern with proper @Mock/@InjectMocks

## Task Commits

Each task was committed atomically:

1. **Task 1: Write SysUserServiceTest.java** - `067bc1f` (test)
2. **Task 2: Write HealthServiceTest.java** - `3b9365d` (test)

## Files Created/Modified
- `backend/src/test/java/com/healthy/service/SysUserServiceTest.java` - 12 JUnit 5 tests for SysUserService
- `backend/src/test/java/com/healthy/service/HealthServiceTest.java` - 20 JUnit 5 tests for HealthService

## Decisions Made

- Used `doNothing().when(mapper).deleteById(id)` for void return type mapper methods instead of `when(...).thenReturn(1)`
- Set non-blank password in `testRegister_DuplicateUsername` because register() validates blank fields before duplicate check

## Deviations from Plan

None - plan executed exactly as written.

### Auto-fixed Issues

**1. [Rule 1 - Bug] deleteById stub corrected from thenReturn(1) to doNothing()**
- **Found during:** Task 1 (SysUserServiceTest)
- **Issue:** `sysUserMapper.deleteById` is void, not int — `when(...).thenReturn(1)` caused compilation error
- **Fix:** Changed to `doNothing().when(sysUserMapper).deleteById(1L)`
- **Files modified:** `backend/src/test/java/com/healthy/service/SysUserServiceTest.java`
- **Verification:** `mvn test -Dtest=SysUserServiceTest` — 12 tests pass, BUILD SUCCESS
- **Committed in:** `067bc1f` (part of task commit)

**2. [Rule 1 - Bug] testRegister_DuplicateUsername password set to non-blank**
- **Found during:** Task 1 (SysUserServiceTest)
- **Issue:** Test created SysUser with username "existing" but no password — register() throws "400 参数错误" before reaching duplicate check
- **Fix:** Added `newUser.setPassword("password123")`
- **Files modified:** `backend/src/test/java/com/healthy/service/SysUserServiceTest.java`
- **Verification:** `mvn test -Dtest=SysUserServiceTest` — 12 tests pass including testRegister_DuplicateUsername
- **Committed in:** `067bc1f` (part of task commit)

---

**Total deviations:** 2 auto-fixed (both Rule 1 - bug fixes)
**Impact on plan:** Both fixes necessary for tests to run. No scope creep.

## Issues Encountered

- Java 8 in PATH but project requires Java 17 — found GraalVM Java 17 at `/c/Users/11193125/.jdks/graalvm-ce-17.0.9` and used it via `JAVA_HOME` env var

## Next Phase Readiness

- Unit test foundation established for backend services
- Next plan (03-02) continues testing polish phase
- All tests pass, ready for integration with remaining plans in phase 03

---
*Phase: 03-testing-polish*
*Completed: 2026-04-20*
