# 🚨 Bug: Performance Testing Workflow Failing - Missing Test Files

## Issue Type
**Bug / Missing Implementation**

## Priority
**High** (CI/CD pipeline failing on schedule)

## Description

The **Performance Testing** workflow (`.github/workflows/performance.yml`) is failing because it references performance test files that don't exist in the repository. The workflow was created but the actual test implementation files were never added.

## Error Message

```
Could not find 'src/test/locust/api_load_test.py'. See --help for available options.
Error: Process completed with exit code 1.
```

## Root Cause

The performance testing infrastructure is **documented** but the actual test files are **missing**:

### Missing Locust Files (Python):
- ❌ `src/test/locust/api_load_test.py`
- ❌ `src/test/locust/web_load_test.py`
- ❌ `src/test/locust/comprehensive_load_test.py`

### Missing Gatling Files (Scala):
- ❌ `src/test/scala/` directory doesn't exist
- ❌ `ApiLoadSimulation.scala`
- ❌ `WebLoadSimulation.scala`

### Missing JMeter Files (JMX):
- ❌ `src/test/jmeter/API_Performance_Test.jmx`
- ❌ `src/test/jmeter/Web_Load_Test.jmx`

## Evidence

### 1. Only Python file in repo:
```bash
$ find . -name "*.py" -type f
./__ini__.py
```

### 2. No Scala files:
```bash
$ find . -name "*.scala" -type f
# (no results)
```

### 3. No JMeter files:
```bash
$ find . -name "*.jmx" -type f
# (no results)
```

### 4. But documentation references them:
- `README.md` mentions Gatling, JMeter, Locust
- `docs/guides/testing/PERFORMANCE_TESTING.md` has complete guide
- Scripts exist: `scripts/run-locust-tests.sh`, `scripts/run-gatling-tests.sh`, `scripts/run-jmeter-tests.sh`

## Impact

### Current Impact:
- ❌ **Performance Testing workflow fails** on every scheduled run (nightly + weekly)
- ❌ **GitHub Actions shows red status** for performance tests
- ⚠️ **Documentation promises features** that don't exist
- ⚠️ **Scripts reference missing files**

### User Experience:
- Developers may try to run performance tests and get confused
- CI/CD shows failures that aren't actually test failures
- Credibility issue: documentation doesn't match reality

## Proposed Solutions

### Option 1: Create the Performance Test Files (Recommended)
**Effort**: 8-12 hours
**Impact**: Full performance testing capability

**Tasks**:
1. Create `src/test/locust/` directory
2. Implement Locust tests (3 files)
3. Create `src/test/scala/` directory
4. Implement Gatling simulations (2 files)
5. Create `src/test/jmeter/` directory
6. Create JMeter test plans (2 files)
7. Test all three tools locally
8. Verify GitHub Actions workflow passes

**Benefits**:
- Complete performance testing capability
- Documentation matches reality
- Workflow functions as designed

---

### Option 2: Disable Performance Workflow Temporarily (Quick Fix)
**Effort**: 5 minutes
**Impact**: Stops CI/CD failures

**Tasks**:
1. Comment out or disable `.github/workflows/performance.yml`
2. Add note that it's disabled pending implementation
3. Create this GitHub issue to track implementation
4. Update documentation to note feature is planned

**Benefits**:
- Immediate fix for CI/CD failures
- Honest about current state
- Clear path forward

---

### Option 3: Hybrid Approach (Recommended for Short-term)
**Effort**: 1-2 hours
**Impact**: Partial fix, staged implementation

**Tasks**:
1. Disable failing workflow sections temporarily
2. Create stub/placeholder test files
3. Document the implementation roadmap
4. Implement tools incrementally (Locust first, then Gatling, then JMeter)

---

## Implementation Plan (Option 1 - Full Implementation)

### Phase 1: Locust Tests (40% allocation - 4 hours)

**Create**: `src/test/locust/api_load_test.py`
```python
from locust import HttpUser, task, between

class APIUser(HttpUser):
    wait_time = between(1, 3)
    host = "https://jsonplaceholder.typicode.com"

    @task(3)
    def get_posts(self):
        self.client.get("/posts")

    @task(2)
    def get_specific_post(self):
        self.client.get("/posts/1")

    @task(1)
    def create_post(self):
        self.client.post("/posts", json={
            "title": "Test",
            "body": "Test body",
            "userId": 1
        })
```

**Create**: `src/test/locust/web_load_test.py`
```python
from locust import HttpUser, task, between

class WebUser(HttpUser):
    wait_time = between(1, 5)
    host = "https://www.google.com"

    @task
    def load_homepage(self):
        self.client.get("/")

    @task(2)
    def search(self):
        self.client.get("/search?q=selenium+testing")
```

**Create**: `src/test/locust/comprehensive_load_test.py`
```python
from locust import HttpUser, TaskSet, task, between

class ComprehensiveUser(HttpUser):
    wait_time = between(2, 5)
    host = "https://jsonplaceholder.typicode.com"

    @task(4)
    def browse_posts(self):
        self.client.get("/posts")

    @task(2)
    def view_users(self):
        self.client.get("/users")

    @task(1)
    def create_comment(self):
        self.client.post("/comments", json={
            "postId": 1,
            "name": "Test",
            "email": "test@example.com",
            "body": "Test comment"
        })
```

### Phase 2: Gatling Tests (30% allocation - 4 hours)

**Create**: `src/test/scala/` directory and simulations
- `ApiLoadSimulation.scala`
- `WebLoadSimulation.scala`

### Phase 3: JMeter Tests (30% allocation - 4 hours)

**Create**: `src/test/jmeter/` directory and test plans
- `API_Performance_Test.jmx`
- `Web_Load_Test.jmx`

---

## Quick Fix for Tonight (Option 2)

To stop the workflow from failing immediately:

**Edit `.github/workflows/performance.yml`:**
```yaml
# Temporarily disable scheduled runs until test files are created
on:
  # schedule:  # DISABLED - Pending test file creation (see issue #XX)
  #   - cron: '0 4 * * 1'
  #   - cron: '0 4 * * *'

  workflow_dispatch:  # Manual trigger only
```

---

## Acceptance Criteria

✅ **Definition of Done**:
1. All performance test files created and functional
2. Locust tests run successfully locally
3. Gatling tests compile and run
4. JMeter tests execute properly
5. GitHub Actions performance workflow passes
6. Documentation accurately reflects implementation
7. Scripts work as documented

OR (for Option 2):
1. Workflow disabled/commented
2. Documentation updated to note feature is planned
3. GitHub issue created to track implementation
4. Clear roadmap for future implementation

---

## Related Files

**Documentation (claims features exist)**:
- `README.md` - Lists Locust, Gatling, JMeter
- `docs/guides/testing/PERFORMANCE_TESTING.md` - Complete guide (~880 lines)
- `pom.xml` - Has Gatling/JMeter plugin configurations

**Scripts (reference missing files)**:
- `scripts/run-locust-tests.sh`
- `scripts/run-gatling-tests.sh`
- `scripts/run-jmeter-tests.sh`
- `scripts/run-all-performance-tests.sh`

**Workflow**:
- `.github/workflows/performance.yml` - Expects all three tools

**Dependencies**:
- `requirements.txt` - Has Locust 2.20.0 ✅
- `pom.xml` - Has Gatling 3.10.3 ✅
- `pom.xml` - Has JMeter 5.6.3 ✅

---

## Recommendations

### For Tomorrow:
**Option A**: Disable the workflow temporarily (5 minutes)
- Prevents nightly failures
- Buys time to implement properly

**Option B**: Start implementation with Locust (4 hours)
- Create the 3 Locust test files
- Test locally
- Enable workflow for Locust only

### For Next Week:
- Implement remaining tools (Gatling, JMeter)
- Complete performance testing capability
- Update documentation to match implementation

---

## Estimated Effort

- **Quick Fix (Disable workflow)**: 5 minutes
- **Locust Only**: 4 hours
- **All Three Tools**: 12-16 hours
- **With documentation**: 16-20 hours

---

## Labels

- `bug` - Workflow failing
- `documentation` - Docs don't match reality
- `performance` - Performance testing
- `ci-cd` - GitHub Actions issue
- `good-first-issue` - Clear scope, good examples available

---

## Notes

- This appears to be aspirational documentation
- The infrastructure is set up correctly (dependencies, plugins, scripts)
- Just missing the actual test implementation files
- Not a security issue, just incomplete feature

---

**Created**: 2025-11-12
**Discovered During**: GitHub Actions review
**Affects**: Performance Testing workflow (nightly schedule)
**Workaround**: Disable scheduled runs until implementation complete
