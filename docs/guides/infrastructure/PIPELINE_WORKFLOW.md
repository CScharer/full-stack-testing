---
**Type**: Guide
**Purpose**: Complete reference for GitHub Actions CI/CD pipeline workflow and job dependencies
**Created**: 2025-11-13
**Last Updated**: 2025-11-13
**Maintained By**: CJS QA Team
**Status**: Active
**Related To**: GITHUB_ACTIONS.md, CI_TROUBLESHOOTING.md, MULTI_ENVIRONMENT_IMPLEMENTATION_PLAN.md
**Version**: 2.0 - Reusable Workflow Architecture
---

# CI/CD Pipeline Workflow Reference
## Complete Guide to GitHub Actions Pipeline

**Main Workflow**: `.github/workflows/ci.yml` (524 lines - orchestrator)
**Reusable Workflow**: `.github/workflows/test-environment.yml` (413 lines - per-environment testing)
**Performance Workflow**: `.github/workflows/performance.yml` (separate, not modified)
**Architecture**: Reusable Workflow Pattern (DRY principle)
**Last Pipeline Update**: 2025-11-13 (multi-environment + reusable workflow architecture)

---

## 📊 TABLE OF CONTENTS

1. [Pipeline Overview](#pipeline-overview)
2. [Workflow Triggers](#workflow-triggers)
3. [Job Flow Diagram](#job-flow-diagram)
4. [Job Descriptions](#job-descriptions)
5. [Conditional Execution Logic](#conditional-execution-logic)
6. [Service Dependencies](#service-dependencies)
7. [Artifact Management](#artifact-management)
8. [Environment Variables](#environment-variables)
9. [Troubleshooting](#troubleshooting)
10. [Optimization Features](#optimization-features)
11. [Maintenance Guide](#maintenance-guide)

---

## 🎯 PIPELINE OVERVIEW

### Main CI/CD Pipeline (ci.yml)

**Purpose**: Automated testing, quality checks, and deployment
**Trigger**: Push/PR to `main` or `develop` branches
**Architecture**: Reusable workflow pattern for clean, maintainable code
**Duration**:
- Documentation-only: ~30 seconds (optimized!)
- Single environment: ~15 minutes (shared setup + one environment)
- All environments: ~33 minutes (shared setup + 3 environments @ ~8min each)

**Total Jobs in ci.yml**: 12 main orchestrator jobs
**Jobs per Environment** (in test-environment.yml): 6 jobs (smoke, grid×2, mobile, responsive, allure, summary)

**🆕 NEW Features**:
- ✅ Multi-environment support (DEV, TEST, PROD)
- ✅ Reusable workflow architecture (59% smaller main workflow)
- ✅ Parallel test execution within each environment
- ✅ Sequential environment gating (DEV → TEST → PROD)

---

## 🌍 MULTI-ENVIRONMENT SUPPORT (Added 2025-11-13)

### **Environment Selection**

**Manual Trigger Options**:
1. **All Environments** (default): Runs DEV → TEST → PROD sequentially
2. **DEV Only**: Tests only development environment
3. **TEST Only**: Tests only test environment
4. **PROD Only**: Tests only production environment

**Test Suite Selection**:
- **smoke**: Quick smoke tests (~2 min per environment)
- **ci**: CI test suite (~5 min per environment)
- **extended**: Extended test suite (~10 min per environment)
- **all**: All test suites

### **How It Works**:

**Automatic Runs** (push/PR):
- Default: Tests all environments sequentially (dev → test → prod)
- Uses smoke test suite for speed

**Manual Runs** (workflow_dispatch):
1. Go to Actions → "Selenium Grid CI/CD Pipeline"
2. Click "Run workflow"
3. Select environment: `all`, `dev`, `test`, or `prod`
4. Select test suite: `smoke`, `ci`, `extended`, or `all`
5. Click "Run workflow"

**Environment Parameter**:
- Passed to tests via `-Dtest.environment=dev|test|prod`
- Framework reads from system property
- Overrides XML configuration

**Sequential Execution** (when `environment=all`):
```
1. DEV environment tests run first
   ↓ (waits for DEV tests to pass, not deployment)
2. TEST environment tests run
   ↓ (waits for TEST tests to pass, not deployment)
3. PROD environment tests run

Note: Deployments are optional final steps (only run on main branch)
      Test sequencing works on all branches for validation
```

**Independent Execution** (when `environment=dev|test|prod`):
```
Only selected environment runs
Other environments are skipped
```

### **Environment URLs**:

**Configured in workflow**:
```yaml
env:
  DEV_BASE_URL: 'https://dev.yourapp.com'
  TEST_BASE_URL: 'https://test.yourapp.com'
  PROD_BASE_URL: 'https://yourapp.com'
```

**Update these URLs** in `.github/workflows/ci.yml` to match your actual environment URLs.

---

## 🏗️ REUSABLE WORKFLOW ARCHITECTURE (Added 2025-11-13)

### **Why Reusable Workflows?**

**Problem**: Testing three environments meant duplicating test logic 3× in ci.yml
**Solution**: Extract environment testing into a reusable workflow

### **Architecture**:

```
ci.yml (Main Orchestrator - 524 lines)
├── Shared setup (run once)
│   ├── detect-changes
│   ├── determine-environments
│   ├── build-and-compile
│   ├── code-quality
│   └── docker-build
│
├── DEV Environment
│   ├── test-dev-environment → calls test-environment.yml
│   └── deploy-dev
│
├── TEST Environment
│   ├── test-test-environment → calls test-environment.yml
│   └── deploy-test
│
├── PROD Environment
│   ├── test-prod-environment → calls test-environment.yml
│   └── deploy-prod
│
└── Final Reporting
    ├── combined-allure-report
    └── pipeline-summary

test-environment.yml (Reusable - 413 lines)
├── Inputs: environment, test_suite, base_url
└── Jobs (all run in PARALLEL after trigger):
    ├── smoke-tests
    ├── grid-tests (matrix: chrome, firefox)
    ├── mobile-browser-tests
    ├── responsive-design-tests
    ├── environment-allure-report (waits for all tests)
    └── environment-test-summary (waits for all tests)
```

### **Benefits**:

1. **Cleaner Code**: 59% reduction in main workflow size (1285 → 524 lines)
2. **DRY Principle**: Test logic defined once, reused 3× (for each environment)
3. **Easier Maintenance**: Update test steps in one place, applies to all environments
4. **Better Organization**: Clear separation between orchestration and execution
5. **Parallel Execution**: All tests within an environment run simultaneously

### **How It Works**:

**Calling the Reusable Workflow**:
```yaml
test-dev-environment:
  uses: ./.github/workflows/test-environment.yml
  with:
    environment: 'dev'
    test_suite: 'smoke'
    base_url: 'https://dev.yourapp.com'
```

**Inside test-environment.yml**:
```yaml
on:
  workflow_call:
    inputs:
      environment:   # Which environment (dev/test/prod)
      test_suite:    # Which tests (smoke/ci/extended/all)
      base_url:      # Environment URL
```

### **Test Execution Pattern**:

**Within Each Environment** (all PARALLEL):
```
START
  ├── smoke-tests (3min)
  ├── grid-chrome (5min)
  ├── grid-firefox (5min)
  ├── mobile-tests (4min)
  └── responsive-tests (3min)
         ↓ (all complete)
     ├── allure-report
     └── test-summary
END
```

**Result**: ~8 minutes per environment (vs ~20 minutes if sequential)

---

## 🔔 WORKFLOW TRIGGERS

### **When Pipeline Runs**:

```yaml
on:
  push:
    branches: [ main, develop ]       # Every push to these branches
  pull_request:
    branches: [ main, develop ]       # Every PR to these branches
  workflow_dispatch:                  # Manual trigger via GitHub UI
```

### **Manual Trigger**:
1. Go to: https://github.com/CScharer/selenium_java_docker/actions
2. Select "Selenium Grid CI/CD Pipeline"
3. Click "Run workflow"
4. Select branch
5. **Select environment**: `all`, `dev`, `test`, or `prod`
6. **Select test suite**: `smoke`, `ci`, `extended`, or `all`
7. Click "Run workflow" button

---

## 📈 JOB FLOW DIAGRAM

### **Stage 1: Change & Environment Detection** (Always Runs)
```
┌────────────────────┐
│  detect-changes    │ ← Determines if code or docs changed
└──────────┬─────────┘
           │
      ┌────┴────┐
      │         │
   [CODE]    [DOCS]
      │         │
   [Full]   [Skip]
   Pipeline  Tests
      │
      ▼
┌─────────────────────────┐
│ determine-environments  │ ← Determines which environments to test
└───────────┬─────────────┘
            │
    ┌───────┼───────┐
    │       │       │
  [DEV]  [TEST]  [PROD]
    │       │       │
  [Run]   [Run]   [Run]  (based on selection)
```

### **Stage 2: Build & Quality** (If Code Changed)
```
┌──────────────────────────┐
│  build-and-compile       │ ← Compile + Checkstyle + PMD
└────────────┬─────────────┘
             │
      ┌──────┴──────┬──────────────┬─────────────┐
      │             │              │             │
┌─────▼─────┐ ┌────▼─────┐ ┌──────▼──────┐ ┌───▼────┐
│smoke-tests│ │grid-tests│ │mobile-tests │ │docker  │
│  (fast)   │ │(matrix)  │ │(responsive) │ │ build  │
└───────────┘ └────┬─────┘ └─────────────┘ └────────┘
                   │
              ┌────┴────┐
        [Chrome]   [Firefox]
```

### **Stage 3: Environment Testing** (Sequential or Selected)
```
┌────────────────────────┐
│ test-dev-environment   │ ← Tests DEV environment
└───────────┬────────────┘
            │ (if run_dev=true)
            ▼
┌────────────────────────┐
│ test-test-environment  │ ← Tests TEST environment
└───────────┬────────────┘  (only if DEV success)
            │ (if run_test=true)
            ▼
┌────────────────────────┐
│ test-prod-environment  │ ← Tests PROD environment
└────────────────────────┘  (only if TEST success)
   (if run_prod=true)
```

**Execution Examples**:
- `environment=all`: All three run sequentially
- `environment=dev`: Only DEV runs (TEST & PROD skipped)
- `environment=test`: Only TEST runs (DEV & PROD skipped)

### **Stage 4: Additional Testing** (Parallel Matrix)
```
┌──────────────────────────────────────┐
│   selenium-grid-tests (Matrix)       │
│   ├── Chrome   (11 tests)            │
│   └── Firefox  (11 tests)            │
└─────────────┬────────────────────────┘
              │
┌─────────────▼────────────────────────┐
│   mobile-browser-tests               │
│   (Mobile viewport testing)          │
└─────────────┬────────────────────────┘
              │
┌─────────────▼────────────────────────┐
│   responsive-design-tests            │
│   (Multiple screen sizes)            │
└─────────────┬────────────────────────┘
              │
         ┌────┴────┐
    [All Tests]
    [Complete]
```

### **Stage 5: Reporting & Quality**
```
┌────────────────┬────────────────┐
│                │                │
▼                ▼                ▼
allure-report   code-quality   test-summary
(combines all)  (analysis)     (metrics)
      │
      ▼
[GitHub Pages]
(if main branch)
```

### **Stage 6: Deployment** (If main branch)
```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ deploy-  │ ──► │ deploy-  │ ──► │ deploy-  │
│   dev    │     │  test    │     │  prod    │
└──────────┘     └──────────┘     └──────────┘
```

### **Stage 7: Summary** (Always Runs)
```
┌─────────────────────┐
│  pipeline-summary   │ ← Shows what ran/skipped
└─────────────────────┘
```

---

## 📋 JOB DESCRIPTIONS

### **Job 1: detect-changes** (Always Runs - Stage 1)

**Purpose**: Smart change detection to skip unnecessary work

**What It Does**:
1. Compares changed files (current vs previous commit)
2. Checks if any non-documentation files changed
3. Sets output: `code-changed: true/false`

**Logic**:
```bash
# Documentation-only files:
.md, .log, .txt, .rst, .adoc

# If ONLY these changed → code-changed=false (skip tests)
# If ANY other file changed → code-changed=true (run full pipeline)
```

**Outputs**:
- `code-changed`: Boolean flag used by all other jobs

**Duration**: ~10 seconds

**Depends On**: Nothing (runs first)

---

### **Job 2: determine-environments** (Conditional - Stage 1)

**Purpose**: Determine which environments to test based on workflow input

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Inputs Processed**:
- `github.event.inputs.environment` (all, dev, test, prod)
- `github.event.inputs.test_suite` (smoke, ci, extended, all)

**Logic**:
```bash
# If environment='all' (or not set):
run_dev=true, run_test=true, run_prod=true

# If environment='dev':
run_dev=true, run_test=false, run_prod=false

# If environment='test':
run_dev=false, run_test=true, run_prod=false

# If environment='prod':
run_dev=false, run_test=false, run_prod=true
```

**Outputs**:
- `run_dev`: Boolean (whether to run DEV tests)
- `run_test`: Boolean (whether to run TEST tests)
- `run_prod`: Boolean (whether to run PROD tests)
- `test_suite`: Test suite to execute (smoke, ci, extended, all)
- `selected_env`: Environment selection made (all, dev, test, prod)

**Duration**: ~10 seconds

**Depends On**: `detect-changes`

---

### **Job 3: build-and-compile** (Conditional - Stage 2)

**Purpose**: Compile Java code and run static analysis

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Steps**:
1. Checkout code
2. Set up JDK 21
3. Compile: `./mvnw clean compile test-compile`
4. Run Checkstyle: `./mvnw checkstyle:check`
5. Run PMD: `./mvnw pmd:check`
6. Upload PMD report (artifact)
7. Upload compiled classes (artifact)

**Duration**: ~3-5 minutes

**Artifacts Produced**:
- `pmd-report` (7 days retention)
- `compiled-classes` (1 day retention)

**Depends On**: `detect-changes`

---

### **Job 3: smoke-tests** (Conditional - Stage 2)

**Purpose**: Fast critical path verification

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Test Suite**: `testng-smoke-suite.xml` (5 critical tests)

**Services**:
- `selenium-hub`: Selenium Grid hub
- `chrome-node`: Chrome browser node

**Steps**:
1. Start Selenium Grid (via services)
2. Wait for Grid to be ready
3. Verify nodes registered
4. Run smoke tests: `./mvnw test -DsuiteXmlFile=testng-smoke-suite.xml`
5. Verify results (must have 0 failures)

**Duration**: ~2-3 minutes

**Timeout**: 5 minutes

**Fail-Fast**: Yes (pipeline stops if smoke tests fail)

**Depends On**: `detect-changes`, `build-and-compile`

---

### **Job 4: test-dev-environment** (Conditional - Stage 3)

**Purpose**: Test DEV environment with selected test suite

**Condition**: `if: needs.determine-environments.outputs.run_dev == 'true'`

**Environment**:
- **Name**: DEV (Development)
- **URL**: `$DEV_BASE_URL` (configured in workflow env vars)
- **Parameter**: `-Dtest.environment=dev`

**Test Suite**: Dynamic (based on `determine-environments.outputs.test_suite`)
- Could be: smoke, ci, extended, or all

**Services**:
- `selenium-hub`: Selenium Grid hub
- `chrome-node`: Chrome browser node

**Steps**:
1. Start Selenium Grid
2. Wait for Grid ready
3. Run tests with: `./mvnw test -Dtest.environment=dev -DsuiteXmlFile=testng-{suite}-suite.xml`
4. Upload test results

**Duration**: ~5-15 minutes (depending on suite)

**Timeout**: 15 minutes

**Artifacts Produced**:
- `test-results-dev-environment` (7 days)

**Depends On**: `detect-changes`, `determine-environments`, `build-and-compile`, `smoke-tests`

**Execution Logic**:
- Runs if `run_dev=true` (set by determine-environments)
- When `environment=all`: Always runs first
- When `environment=dev`: Runs (others skip)
- When `environment=test` or `prod`: Skipped

---

### **Job 5: test-test-environment** (Conditional - Stage 3)

**Purpose**: Test TEST environment with selected test suite

**Condition**: `if: needs.determine-environments.outputs.run_test == 'true' && (test-dev-environment success or skipped)`

**Environment**:
- **Name**: TEST (Testing/QA)
- **URL**: `$TEST_BASE_URL` (configured in workflow env vars)
- **Parameter**: `-Dtest.environment=test`

**Test Suite**: Dynamic (based on `determine-environments.outputs.test_suite`)

**Services**:
- `selenium-hub`: Selenium Grid hub
- `chrome-node`: Chrome browser node

**Steps**:
1. Start Selenium Grid
2. Wait for Grid ready
3. Run tests with: `./mvnw test -Dtest.environment=test -DsuiteXmlFile=testng-{suite}-suite.xml`
4. Upload test results

**Duration**: ~5-15 minutes (depending on suite)

**Timeout**: 15 minutes

**Artifacts Produced**:
- `test-results-test-environment` (7 days)

**Depends On**: `detect-changes`, `determine-environments`, `build-and-compile`, `smoke-tests`, `test-dev-environment`

**Execution Logic**:
- Runs if `run_test=true` AND (DEV success OR DEV skipped)
- When `environment=all`: Runs second (after DEV)
- When `environment=test`: Runs (others skip)
- When `environment=dev` or `prod`: Skipped

---

### **Job 6: test-prod-environment** (Conditional - Stage 3)

**Purpose**: Test PROD environment with selected test suite

**Condition**: `if: needs.determine-environments.outputs.run_prod == 'true' && (test-test-environment success or skipped)`

**Environment**:
- **Name**: PROD (Production)
- **URL**: `$PROD_BASE_URL` (configured in workflow env vars)
- **Parameter**: `-Dtest.environment=prod`

**Test Suite**: Dynamic (based on `determine-environments.outputs.test_suite`)

**Services**:
- `selenium-hub`: Selenium Grid hub
- `chrome-node`: Chrome browser node

**Steps**:
1. Start Selenium Grid
2. Wait for Grid ready
3. Run tests with: `./mvnw test -Dtest.environment=prod -DsuiteXmlFile=testng-{suite}-suite.xml`
4. Upload test results

**Duration**: ~5-15 minutes (depending on suite)

**Timeout**: 15 minutes

**Artifacts Produced**:
- `test-results-prod-environment` (7 days)

**Depends On**: `detect-changes`, `determine-environments`, `build-and-compile`, `smoke-tests`, `test-test-environment`

**Execution Logic**:
- Runs if `run_prod=true` AND (TEST success OR TEST skipped)
- When `environment=all`: Runs third (after TEST)
- When `environment=prod`: Runs (others skip)
- When `environment=dev` or `test`: Skipped

---

### **Job 7: selenium-grid-tests** (Conditional Matrix - Stage 4)

**Purpose**: Comprehensive UI testing across browsers

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Matrix Strategy**:
```yaml
matrix:
  browser: [chrome, firefox]
fail-fast: false  # Both browsers run even if one fails
```

**Test Suite**: `testng-ci-suite.xml` (11 tests per browser = 22 total)

**Services**:
- `selenium-hub`: Grid hub
- `chrome-node`: Chrome browser
- `firefox-node`: Firefox browser

**Steps**:
1. Start Grid with both browser nodes
2. Wait for Grid ready (up to 60 seconds)
3. Verify nodes available
4. Run tests: `./mvnw test -DsuiteXmlFile=testng-ci-suite.xml`
5. Verify test results
6. Upload test results + screenshots

**Duration**: ~10-15 minutes per browser

**Timeout**: 15 minutes

**Artifacts Produced**:
- `test-results-chrome` (7 days)
- `test-results-firefox` (7 days)
- `screenshots-{browser}` (7 days, only on failure)

**Depends On**: `detect-changes`, `build-and-compile`

---

### **Job 5: mobile-browser-tests** (Conditional - Stage 3)

**Purpose**: Mobile viewport testing

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Test Suite**: `testng-mobile-browser-suite.xml`

**Configuration**:
- Emulated mobile viewports
- Touch interactions
- Mobile-specific gestures

**Duration**: ~8-10 minutes

**Timeout**: 10 minutes

**Artifacts Produced**:
- `test-results-mobile-browser` (7 days)

**Depends On**: `detect-changes`, `build-and-compile`

---

### **Job 6: responsive-design-tests** (Conditional - Stage 3)

**Purpose**: Test multiple screen resolutions

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Test Suite**: `testng-responsive-suite.xml`

**Screen Sizes Tested**:
- Desktop: 1920x1080
- Laptop: 1366x768
- Tablet: 768x1024
- Mobile: 375x667

**Duration**: ~8-10 minutes

**Timeout**: 10 minutes

**Artifacts Produced**:
- `test-results-responsive-design` (7 days)

**Depends On**: `detect-changes`, `build-and-compile`

---

### **Job 7: allure-report** (Conditional - Stage 4)

**Purpose**: Generate consolidated test report from all browsers

**Condition**: `if: always() && needs.detect-changes.outputs.code-changed == 'true'`

**Note**: Uses `always()` so it runs even if some tests fail

**Steps**:
1. Download all test results (all browsers)
2. Merge Allure results
3. Install Allure CLI
4. Generate report: `allure generate allure-results --clean`
5. Upload report artifact
6. Deploy to GitHub Pages (if main branch)

**Duration**: ~2-3 minutes

**Artifacts Produced**:
- `allure-report` (30 days retention)

**GitHub Pages**: https://cscharer.github.io/selenium_java_docker/

**Depends On**: `smoke-tests`, `selenium-grid-tests`, `mobile-browser-tests`, `responsive-design-tests`

---

### **Job 8: code-quality** (Conditional - Stage 4)

**Purpose**: Static code analysis

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Checks**:
- Checkstyle: Code style compliance
- (SpotBugs and other checks can be added)

**Steps**:
1. Run Checkstyle: `./mvnw checkstyle:check`
2. Upload Checkstyle report

**Duration**: ~2-3 minutes

**Continue on Error**: Yes (warnings don't fail pipeline)

**Artifacts Produced**:
- `checkstyle-report` (7 days)

**Depends On**: `detect-changes`, `build-and-compile`

---

### **Job 9: docker-build** (Conditional - Stage 4)

**Purpose**: Verify Docker container builds successfully

**Condition**: `if: needs.detect-changes.outputs.code-changed == 'true'`

**Steps**:
1. Set up Docker Buildx
2. Build: `docker compose build tests`
3. Verify: `docker run --rm selenium_java_docker-tests:latest ./mvnw --version`

**Duration**: ~5-8 minutes (with caching)

**Depends On**: `detect-changes`

---

### **Job 10: test-summary** (Conditional - Stage 4)

**Purpose**: Aggregate test results and post summary

**Condition**: `if: always() && needs.detect-changes.outputs.code-changed == 'true'`

**Steps**:
1. Download all test results
2. Parse XML reports
3. Create summary with pass/fail counts
4. Post to GitHub Actions Summary

**Duration**: ~30 seconds

**Depends On**: `smoke-tests`, `selenium-grid-tests`, `mobile-browser-tests`, `responsive-design-tests`, `allure-report`

---

### **Job 11: deploy-dev, deploy-test, deploy-prod** (Conditional - Stage 5)

**Purpose**: Deployment to different environments

**Condition**: `if: github.ref == 'refs/heads/main' && github.event_name == 'push' && needs.detect-changes.outputs.code-changed == 'true'`

**Sequential Flow**:
```
deploy-dev (DEV environment)
    ↓ (only if success)
deploy-test (TEST environment)
    ↓ (only if success)
deploy-prod (PRODUCTION environment)
```

**Current Status**: Placeholder (validation only, actual deployment steps TBD)

**Environment Protection**: GitHub environments can require approvals

**Duration**: ~30 seconds each (placeholder)

**Depends On**:
- `deploy-dev`: All test/quality jobs
- `deploy-test`: `deploy-dev`
- `deploy-prod`: `deploy-test`

---

### **Job 12: pipeline-summary** (Always Runs - Stage 6)

**Purpose**: Final pipeline status summary

**Condition**: `if: always()`

**Shows**:
- Whether code or documentation-only change
- What was skipped (if docs-only)
- Commit and branch info

**Duration**: ~10 seconds

**Depends On**: `detect-changes`

---

## 🔄 CONDITIONAL EXECUTION LOGIC

### **Documentation-Only Optimization** (Added 2025-11-12)

**How It Works**:

**Step 1: detect-changes job analyzes files**
```bash
# Get changed files
CHANGED_FILES=$(git diff --name-only HEAD^1 HEAD)

# Check if any non-doc files changed
CODE_CHANGED=$(echo "$CHANGED_FILES" | grep -v -E '\.(md|log|txt|rst|adoc)$')

# If CODE_CHANGED is empty → documentation-only
```

**Step 2: Set output flag**
```yaml
if [ -z "$CODE_CHANGED" ]; then
  echo "code-changed=false" >> $GITHUB_OUTPUT  # Skip tests
else
  echo "code-changed=true" >> $GITHUB_OUTPUT   # Run full pipeline
fi
```

**Step 3: Conditional job execution**
```yaml
# Most jobs have this condition:
if: needs.detect-changes.outputs.code-changed == 'true'

# They are SKIPPED when only docs changed
```

**Jobs That Run on Docs-Only**:
- ✅ `detect-changes` (detects the change type)
- ✅ `pipeline-summary` (shows what was skipped)

**Jobs That Skip on Docs-Only**:
- ⏭️ `build-and-compile`
- ⏭️ `smoke-tests`
- ⏭️ `selenium-grid-tests`
- ⏭️ `mobile-browser-tests`
- ⏭️ `responsive-design-tests`
- ⏭️ `allure-report`
- ⏭️ `code-quality`
- ⏭️ `docker-build`
- ⏭️ `test-summary`
- ⏭️ `deploy-*`

**Time Saved**: ~20-25 minutes per documentation commit! 🚀

---

## 🐳 SERVICE DEPENDENCIES

### **Selenium Grid Services** (Used by Test Jobs)

**selenium-hub**:
```yaml
image: selenium/hub:4.39.0
ports:
  - 4444:4444  # Grid UI
  - 4442:4442  # Event bus publish
  - 4443:4443  # Event bus subscribe
```

**chrome-node**:
```yaml
image: selenium/node-chrome:4.39.0
env:
  SE_EVENT_BUS_HOST: selenium-hub
  SE_EVENT_BUS_PUBLISH_PORT: 4442
  SE_EVENT_BUS_SUBSCRIBE_PORT: 4443
  SE_NODE_MAX_SESSIONS: 5
options: --shm-size=2gb
```

**firefox-node**:
```yaml
image: selenium/node-firefox:4.39.0
env:
  SE_EVENT_BUS_HOST: selenium-hub
  SE_EVENT_BUS_PUBLISH_PORT: 4442
  SE_EVENT_BUS_SUBSCRIBE_PORT: 4443
  SE_NODE_MAX_SESSIONS: 5
options: --shm-size=2gb
```

### **Service Health Checks**

**Grid Readiness Verification**:
```bash
# Wait for Grid hub
timeout 60 bash -c 'until curl -sf http://localhost:4444/wd/hub/status; do sleep 2; done'

# Wait for nodes to register
for i in {1..30}; do
  NODES=$(curl -sf http://localhost:4444/wd/hub/status | jq -r '.value.nodes | length')
  if [ "$NODES" -gt "0" ]; then
    echo "✅ Grid ready!"
    break
  fi
  sleep 2
done
```

**Why This Is Important**:
- Grid services start asynchronously
- Nodes need time to register with hub
- Tests will fail if run before Grid is ready
- Typical wait: 10-30 seconds

---

## 📦 ARTIFACT MANAGEMENT

### **Artifacts Produced**:

| Artifact Name | Produced By | Retention | Size | Purpose |
|---------------|-------------|-----------|------|---------|
| `pmd-report` | build-and-compile | 7 days | ~1 MB | Code analysis |
| `compiled-classes` | build-and-compile | 1 day | ~50 MB | Compiled code |
| `test-results-dev-environment` | test-dev-environment | 7 days | ~10 MB | DEV env test results |
| `test-results-test-environment` | test-test-environment | 7 days | ~10 MB | TEST env test results |
| `test-results-prod-environment` | test-prod-environment | 7 days | ~10 MB | PROD env test results |
| `test-results-chrome` | grid-tests | 7 days | ~10 MB | Test results + screenshots |
| `test-results-firefox` | grid-tests | 7 days | ~10 MB | Test results + screenshots |
| `screenshots-{browser}` | grid-tests | 7 days | ~5 MB | Failure screenshots |
| `test-results-mobile-browser` | mobile-tests | 7 days | ~10 MB | Mobile test results |
| `test-results-responsive-design` | responsive-tests | 7 days | ~10 MB | Responsive results |
| `allure-report` | allure-report | 30 days | ~20 MB | HTML report |
| `checkstyle-report` | code-quality | 7 days | ~1 MB | Style analysis |

### **Downloading Artifacts**:

**Via GitHub UI**:
1. Go to workflow run
2. Scroll to "Artifacts" section
3. Click artifact name to download

**Via GitHub CLI**:
```bash
gh run download <run-id> --name allure-report
```

---

## 🌐 ENVIRONMENT VARIABLES

### **Global Environment Variables**:

```yaml
env:
  JAVA_VERSION: '17'                    # Java version for all jobs
  MAVEN_OPTS: -Xmx2048m                 # Maven memory allocation
```

### **Job-Specific Environment Variables**:

**Testing Jobs**:
```yaml
env:
  SELENIUM_REMOTE_URL: http://localhost:4444/wd/hub
  BROWSER: ${{ matrix.browser }}        # Chrome or Firefox
```

**Deployment Jobs**:
```yaml
environment:
  name: development|test|production
  url: https://your-{env}-url.com
```

---

## 🔧 TROUBLESHOOTING

### **Common Issues**:

#### **Issue: Grid Not Ready**
**Symptom**: Tests fail with "Could not start new session"

**Cause**: Tests started before Grid nodes registered

**Solution**: Already implemented - wait loops with 30-60 second timeout

**Check**:
```bash
# In pipeline logs, look for:
"✅ Grid is ready with X node(s)!"
```

---

#### **Issue: Documentation-Only Changes Running Full Pipeline**
**Symptom**: Pipeline runs all tests for .md file changes

**Cause**: `detect-changes` job not detecting correctly

**Solution**:
1. Check which files changed: Look for "Changed files:" in detect-changes log
2. Verify pattern match: Should exclude .md, .log, .txt, .rst, .adoc
3. Check if any code files accidentally modified

---

#### **Issue: Tests Pass Locally But Fail in Pipeline**
**Symptom**: Tests work on local machine but fail in GitHub Actions

**Common Causes**:
1. **Timing differences**: Pipeline may be slower/faster
2. **Grid initialization**: Different startup timing
3. **Dependencies**: Missing in Docker image
4. **Environment**: Different Java/Maven versions

**Solution**:
1. Check exact error in pipeline logs
2. Verify Docker image has all dependencies
3. Add explicit waits where needed
4. Check environment variable differences

---

#### **Issue: Allure Report Not Generated**
**Symptom**: No allure-report artifact or GitHub Pages not updating

**Cause**: No test results to merge OR deployment failed

**Check**:
1. Verify test jobs uploaded artifacts
2. Check allure-report job logs for errors
3. Verify GitHub Pages is enabled in repo settings
4. Check peaceiris/actions-gh-pages step for errors

---

#### **Issue: Artifacts Not Available**
**Symptom**: Can't download artifacts after pipeline runs

**Causes**:
1. Job failed before artifact upload
2. Retention period expired
3. Wrong artifact name

**Solution**:
1. Check if job succeeded (`if: always()` should still upload)
2. Check retention days in artifact upload step
3. Verify exact artifact name in upload/download steps

---

## ⚡ OPTIMIZATION FEATURES

### **1. Documentation-Only Skip** ⭐ (Added 2025-11-12)
- **Saves**: 20-25 minutes per documentation commit
- **How**: detect-changes job analyzes file types
- **Impact**: Huge time savings for docs team

### **2. Maven Dependency Caching**
```yaml
- uses: actions/setup-java@v4
  with:
    cache: 'maven'  # Caches ~/.m2/repository
```
- **Saves**: 2-3 minutes per build
- **How**: GitHub Actions caches Maven dependencies

### **3. Fail-Fast for Critical Tests**
- Smoke tests run before full suite
- If smoke tests fail, pipeline stops immediately
- **Saves**: 15-20 minutes on broken builds

### **4. Parallel Matrix Testing**
```yaml
matrix:
  browser: [chrome, firefox]
```
- Tests both browsers simultaneously
- **Saves**: 10-15 minutes vs sequential

### **5. Conditional Deployments**
- Only runs on `main` branch
- Only runs if tests pass
- Prevents bad deployments

---

## 🛠️ MAINTENANCE GUIDE

### **When to Update This Document**:

**Always update when**:
- ✅ Adding new jobs to pipeline
- ✅ Changing job dependencies
- ✅ Modifying conditional logic
- ✅ Updating service configurations
- ✅ Adding/removing test suites
- ✅ Changing artifact retention
- ✅ Modifying environment variables

**How to Update**:
1. Update the relevant section
2. Update the job flow diagram if structure changed
3. Update job count if jobs added/removed
4. Update the "Last Pipeline Update" date at top
5. Update **Last Updated** in metadata
6. Commit with message: "docs: Update pipeline workflow - [describe change]"

---

### **Keeping in Sync with ci.yml**:

**When Modifying .github/workflows/ci.yml**:

1. **Make the change** in ci.yml
2. **Update this document** with the changes
3. **Update job flow diagram** if needed
4. **Document why** the change was made
5. **Commit both files** together

**Example Commit Message**:
```
feat: Add security scanning job to CI/CD pipeline

Added new job for dependency vulnerability scanning.

Changes:
- .github/workflows/ci.yml: Added security-scan job
- docs/guides/infrastructure/PIPELINE_WORKFLOW.md: Documented new job

See pipeline workflow documentation for details.
```

---

### **Testing Pipeline Changes**:

**Before Merging Pipeline Changes**:

1. **Test on branch**:
   ```bash
   # Push to feature branch
   git checkout -b feature/improve-pipeline
   # Make changes to .github/workflows/ci.yml
   git push origin feature/improve-pipeline
   # Pipeline runs on branch
   ```

2. **Verify behavior**:
   - Check all jobs run as expected
   - Verify conditional logic works
   - Check artifacts are produced
   - Test both doc-only and code changes

3. **Merge when green**:
   - Only merge if pipeline passes
   - Update this documentation
   - Create PR with pipeline changes

---

## 📊 PERFORMANCE WORKFLOW (Separate)

**File**: `.github/workflows/performance.yml`

**Status**: ⚠️ Currently failing (missing test files)

**Purpose**: Scheduled performance testing with Locust, Gatling, JMeter

**Schedule**:
- Nightly quick check: 10 PM CST (4 AM UTC)
- Weekly comprehensive: Sunday 10 PM CST

**Issue**: Missing performance test files
- See: `docs/issues/open/missing-performance-test-files.md`

**Recommendation**: Disable scheduled runs until test files are created

---

## 🎯 PIPELINE METRICS

### **Expected Performance**:

| Scenario | Duration | Jobs Run | Artifacts |
|----------|----------|----------|-----------|
| **Documentation Only** | ~30 sec | 2/12 | 0 |
| **Code Change (Passing)** | ~25 min | 12/12 | 9 |
| **Code Change (Failing)** | ~5 min | 3/12 | 2 |

### **Test Coverage**:

| Test Type | Suite | Tests | Duration |
|-----------|-------|-------|----------|
| **Smoke** | testng-smoke-suite.xml | 5 | ~2 min |
| **CI Grid** | testng-ci-suite.xml | 11 | ~5 min |
| **Mobile** | testng-mobile-browser-suite.xml | ~10 | ~8 min |
| **Responsive** | testng-responsive-suite.xml | ~10 | ~8 min |
| **TOTAL** | | ~36 | ~23 min |

**Note**: Grid tests run in matrix (Chrome + Firefox) = 22 test executions total

---

## 📝 CHANGE LOG

### **2025-11-13: Reusable Workflow Architecture** 🔥 **MAJOR REFACTOR**
- **Created reusable workflow**: `.github/workflows/test-environment.yml` (413 lines)
- **Refactored main workflow**: Reduced from 1,285 lines to 524 lines (59% reduction)
- **Architecture change**: DRY principle - test logic defined once, reused 3×
- **Improved dependencies**: Environments now wait for previous TEST completion, not deployments
  - `test-test-environment` depends on `test-dev-environment` (not `deploy-dev`)
  - `test-prod-environment` depends on `test-test-environment` (not `deploy-test`)
  - Benefit: Sequential testing works on all branches, not just main
- **Parallel execution**: All tests within each environment run simultaneously
  - Smoke, grid (Chrome + Firefox), mobile, and responsive tests start together
  - Allure report and test summary wait for all tests to complete
  - Result: ~8 min per environment (vs ~20 min sequential)
- **Cleaner separation**: Orchestration (ci.yml) vs execution (test-environment.yml)
- **Easier maintenance**: Update test steps in one place, applies to all environments
- **Feature branch tested**: `feature/multi-environment-pipeline`

### **2025-11-13: Added Multi-Environment Support** 🆕
- Added `determine-environments` job to select which environments to test
- Added three environment-specific test jobs:
  - `test-dev-environment` - Calls reusable workflow for DEV
  - `test-test-environment` - Calls reusable workflow for TEST
  - `test-prod-environment` - Calls reusable workflow for PROD
- Added workflow_dispatch inputs:
  - `environment` selection (all, dev, test, prod)
  - `test_suite` selection (smoke, ci, extended, all)
- Updated Environment.java to read `-Dtest.environment` system property
- Sequential execution: DEV → TEST → PROD (when environment=all)
- Independent execution: Run only selected environment
- **Impact**: Can test specific environments independently, proper environment isolation

### **2025-11-12: Added Documentation-Only Detection**
- Added `detect-changes` job
- Conditional execution for all jobs
- Skips build/test for docs-only
- Added `pipeline-summary` job
- **Impact**: 20-25 min savings per doc commit

### **2025-11-10: Added Mobile & Responsive Testing**
- Added `mobile-browser-tests` job
- Added `responsive-design-tests` job
- New test suites for different viewports
- **Impact**: Better cross-device coverage

### **2025-11-08: Initial Pipeline Setup**
- Created ci.yml workflow
- Matrix testing (Chrome + Firefox)
- Allure reporting
- GitHub Pages deployment
- Code quality checks

---

## 🔗 RELATED DOCUMENTATION

- **[GITHUB_ACTIONS.md](./GITHUB_ACTIONS.md)** - General GitHub Actions guide
- **[CI_TROUBLESHOOTING.md](../troubleshooting/CI_TROUBLESHOOTING.md)** - Troubleshooting guide
- **[DOCKER.md](./DOCKER.md)** - Docker and Selenium Grid setup
- **[ALLURE_REPORTING.md](../testing/ALLURE_REPORTING.md)** - Test reporting

---

## 📚 QUICK REFERENCE

### **Key URLs**:
- **Pipeline Runs**: https://github.com/CScharer/selenium_java_docker/actions
- **Allure Reports**: https://cscharer.github.io/selenium_java_docker/
- **Workflow File**: `.github/workflows/ci.yml`
- **Performance Workflow**: `.github/workflows/performance.yml`

### **Useful Commands**:
```bash
# Check pipeline status via CLI
gh run list --limit 5

# View specific run
gh run view <run-id>

# Download artifacts
gh run download <run-id> --name allure-report

# Re-run failed jobs
gh run rerun <run-id> --failed

# Trigger manual run
gh workflow run "Selenium Grid CI/CD Pipeline"
```

### **Debug a Failed Pipeline**:
1. Click on failed job in GitHub Actions UI
2. Expand failed step
3. Read error message
4. Check logs above error for context
5. Check service logs if applicable
6. Refer to troubleshooting section above

---

## ✅ CHECKLIST: Updating This Document

When modifying the pipeline, update these sections:

- [ ] Job Descriptions (if jobs added/removed/changed)
- [ ] Job Flow Diagram (if structure changed)
- [ ] Conditional Execution Logic (if conditions changed)
- [ ] Service Dependencies (if services added/removed)
- [ ] Artifact Management (if artifacts changed)
- [ ] Environment Variables (if env vars added)
- [ ] Pipeline Metrics (if timings changed)
- [ ] Change Log section (add entry)
- [ ] Last Updated date in metadata (top of file)

---

**Last Updated**: 2025-11-13
**Pipeline Version**: ci.yml (with detect-changes optimization)
**Maintained By**: CJS QA Team
**Status**: Active - Keep current with pipeline changes!

---

*"A well-documented pipeline is a maintainable pipeline."* 🚀
