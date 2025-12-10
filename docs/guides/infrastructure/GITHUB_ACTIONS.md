# GitHub Actions CI/CD Pipeline

**Status**: ✅ Configured
**Workflow**: `.github/workflows/ci.yml`
**Date**: November 8, 2025

---

## 🎯 Overview

Automated CI/CD pipeline that runs on every push and pull request, executing tests across multiple browsers and generating comprehensive reports.

---

## 🚀 What Gets Automated

### **Triggers**
- ✅ Push to `main` or `develop` branches
- ✅ Pull requests to `main` or `develop`
- ✅ Manual workflow dispatch (run on demand)

### **Jobs** (6 parallel jobs)

1. **Build & Compile** - Compile Java code and cache dependencies
2. **Selenium Grid Tests** - Matrix testing across Chrome & Firefox
3. **Allure Report** - Generate beautiful HTML test report
4. **Code Quality** - Dependency analysis and vulnerability checks
5. **Docker Build** - Verify Docker container builds
6. **Test Summary** - Aggregate results and post summary

---

## 📊 Pipeline Architecture

```
┌─────────────────┐
│ Build & Compile │
└────────┬────────┘
         │
    ┌────┴────┬──────────┬────────────┐
    │         │          │            │
┌───▼────┐ ┌─▼──┐   ┌───▼────┐  ┌───▼────┐
│Grid    │ │Grid│   │Code    │  │Docker  │
│Chrome  │ │FF  │   │Quality │  │Build   │
└───┬────┘ └─┬──┘   └────────┘  └────────┘
    │        │
    └────┬───┘
         │
    ┌────▼────────┐
    │Allure Report│
    └────┬────────┘
         │
    ┌────▼────────┐
    │Test Summary │
    └─────────────┘
```

---

## ✅ Job Details

### **1. Build & Compile**
- Checkout code
- Setup JDK 21
- Cache Maven dependencies
- Compile main and test sources
- Upload compiled artifacts

**Duration**: ~1-2 minutes

### **2. Selenium Grid Tests** (Matrix)
- **Chrome Browser Tests**
  - Starts Selenium Hub + Chrome Node
  - Runs SimpleGridTest (3 tests)
  - Runs EnhancedGridTests (8 tests)
  - Uploads results + screenshots

- **Firefox Browser Tests**
  - Starts Selenium Hub + Firefox Node
  - Runs same test suites
  - Uploads results + screenshots

**Duration**: ~3-4 minutes per browser

### **3. Allure Report Generation**
- Downloads all test results
- Combines Chrome + Firefox results
- Generates interactive HTML report
- Maintains history (last 20 runs)
- Deploys to GitHub Pages (on main branch)
- Uploads report artifact

**Duration**: ~30 seconds

### **4. Code Quality Analysis**
- Dependency analysis
- Vulnerability scanning
- Dependency tree visualization
- Continues even if issues found

**Duration**: ~1 minute

### **5. Docker Build Test**
- Builds test container
- Verifies Maven executable
- Ensures Docker setup works

**Duration**: ~2-3 minutes

### **6. Test Summary**
- Aggregates all test results
- Creates markdown summary
- Posts to PR (if applicable)
- Shows pass/fail counts per browser

**Duration**: ~15 seconds

---

## 📈 What You Get

### **On Every Commit**
- ✅ Automatic test execution
- ✅ Multi-browser testing (Chrome + Firefox)
- ✅ Build verification
- ✅ Code quality checks
- ✅ Docker container validation

### **Test Reports**
- ✅ Allure HTML report (downloadable)
- ✅ Test results per browser
- ✅ Screenshots on failures
- ✅ Historical trends

### **PR Integration**
- ✅ Status checks (must pass to merge)
- ✅ Test summary comment
- ✅ Allure report link
- ✅ Build status badge

### **GitHub Pages**
- ✅ Latest Allure report published
- ✅ URL: `https://<username>.github.io/<repo>/allure-report`
- ✅ Always up-to-date (on main branch)

---

## 🔧 Setup Requirements

### **1. Enable GitHub Actions**
Already enabled by default for public repos!

### **2. Enable GitHub Pages** (Optional - for Allure reports)
1. Go to repo Settings → Pages
2. Source: Deploy from a branch
3. Branch: `gh-pages` / `root`
4. Save

### **3. Repository Secrets** (if needed)
For Google Cloud Secret Manager integration:

```
Settings → Secrets and variables → Actions
Add new repository secret:
- Name: GOOGLE_CREDENTIALS
- Value: [your service account JSON]
```

---

## 📊 Pipeline Status

### **Check Status**
- Go to repository → Actions tab
- See all workflow runs
- Click any run for details
- View logs for each job

### **Status Badge**
Add to README.md:

```markdown
![CI Pipeline](https://github.com/CScharer/full-stack-testing/workflows/Selenium%20Grid%20CI%2FCD%20Pipeline/badge.svg)
```

Result: ![CI Pipeline](https://github.com/CScharer/full-stack-testing/workflows/Selenium%20Grid%20CI%2FCD%20Pipeline/badge.svg)

---

## 🎯 Usage Examples

### **Automatic Trigger**
```bash
# Just push code - tests run automatically!
git add .
git commit -m "feat: new feature"
git push origin main

# Check Actions tab in GitHub to see results
```

### **Manual Trigger**
1. Go to Actions tab
2. Select "Selenium Grid CI/CD Pipeline"
3. Click "Run workflow"
4. Select branch
5. Click "Run workflow" button

### **View Results**
1. Click on workflow run
2. See all jobs and their status
3. Click job name for detailed logs
4. Download artifacts (test results, reports, screenshots)

---

## 📂 Artifacts Generated

| Artifact | Contents | Retention |
|----------|----------|-----------|
| `compiled-classes` | Build output | 1 day |
| `test-results-chrome` | Chrome test results + Allure data | 7 days |
| `test-results-firefox` | Firefox test results + Allure data | 7 days |
| `screenshots-chrome` | Failure screenshots (Chrome) | 7 days |
| `screenshots-firefox` | Failure screenshots (Firefox) | 7 days |
| `allure-report` | Interactive HTML report | 30 days |

---

## 🔍 Monitoring & Debugging

### **View Live Logs**
While pipeline runs, click job name to see real-time logs:
- Compilation output
- Test execution
- Allure generation
- Docker builds

### **Download Artifacts**
After run completes:
1. Go to workflow run
2. Scroll to "Artifacts" section
3. Click to download
4. Extract and view locally

### **Debug Failed Tests**
```bash
# Download test-results artifact
# View Surefire reports
open test-results/surefire-reports/index.html

# Or view Allure report
cd allure-report
python3 -m http.server 8000
open http://localhost:8000
```

---

## ⚡ Performance Optimization

### **Caching Strategy**
- ✅ Maven dependencies cached
- ✅ Docker layers cached
- ✅ Reuses across runs

### **Parallel Execution**
- ✅ Matrix strategy (Chrome + Firefox in parallel)
- ✅ Independent job execution
- ✅ Faster feedback (~5 minutes total vs 10+ sequential)

### **Resource Limits**
- VM: 2 CPU cores, 7GB RAM
- Each browser node: 2GB shared memory
- Sufficient for test execution

---

## 🛡️ Security Features

### **Secrets Management**
- Never expose credentials in logs
- Use GitHub Secrets for sensitive data
- Google Cloud auth via service account

### **Isolated Execution**
- Fresh environment per run
- No state carries over
- Clean Docker containers

### **Access Control**
- Workflow runs only for authorized users
- PR from forks have limited access
- Protected branches enforceable

---

## 📋 Configuration Options

### **Adjust Test Timeout**
```yaml
- name: Run Grid Tests
  timeout-minutes: 15  # Adjust as needed
```

### **Change Browser Matrix**
```yaml
strategy:
  matrix:
    browser: [chrome, firefox, edge]  # Add more browsers
```

### **Adjust Parallel Threads**
```yaml
env:
  PARALLEL_THREADS: 3  # Reduce for limited resources
```

### **Add More Test Suites**
```yaml
- name: Run Grid Tests
  run: |
    ./mvnw test -Dtest=SimpleGridTest,EnhancedGridTests,YourNewTest
```

---

## 🎯 Example Workflow Run

```
Workflow: Selenium Grid CI/CD Pipeline
Trigger: push to main
Commit: e4def02
Branch: main

Jobs:
✅ Build & Compile (1m 23s)
✅ Grid Tests - chrome (3m 45s)
  └─ SimpleGridTest: 3/3 passed
  └─ EnhancedGridTests: 8/8 passed
✅ Grid Tests - firefox (4m 12s)
  └─ SimpleGridTest: 3/3 passed
  └─ EnhancedGridTests: 8/8 passed
✅ Allure Report (28s)
  └─ Report generated successfully
  └─ Deployed to GitHub Pages
✅ Code Quality (1m 5s)
  └─ No critical issues found
✅ Docker Build (2m 33s)
  └─ Container built successfully
✅ Test Summary (12s)
  └─ 22 tests total, 22 passed

Total Duration: 5m 18s
Status: SUCCESS ✅
```

---

## 🎁 Benefits

### **For Development**
- ✅ Catch bugs before merge
- ✅ Verify cross-browser compatibility
- ✅ Ensure Docker setup works
- ✅ Prevent regressions

### **For Team**
- ✅ Consistent test environment
- ✅ No "works on my machine"
- ✅ Visual test reports
- ✅ Historical tracking

### **For Management**
- ✅ Automated quality gates
- ✅ Professional reporting
- ✅ Audit trail
- ✅ Trend visibility

---

## 🔄 Continuous Improvement

### **After Each Run**
The pipeline:
- Generates Allure report
- Saves history
- Shows trends over time
- Identifies flaky tests
- Tracks performance

### **History Tracking**
- Last 20 runs kept
- Compare with previous
- See improvements
- Identify patterns

---

## 🆘 Troubleshooting

### **Workflow not running**
1. Check Actions tab is enabled
2. Verify workflow file syntax (YAML)
3. Check branch name in triggers

### **Tests failing in CI but passing locally**
- Check environment variables
- Verify Grid connection URL
- Check resource limits
- Review timing issues (add waits)

### **Allure report not generating**
- Verify test results are uploaded
- Check artifact download step
- Ensure allure-results directory exists

### **Docker build failing**
- Check Dockerfile syntax
- Verify base image availability
- Review build logs in job output

---

## 📞 Quick Commands

### **View Latest Run**
```bash
# Using GitHub CLI
gh run list --limit 5
gh run view <run-id>
gh run view <run-id> --log
```

### **Download Artifacts**
```bash
# Using GitHub CLI
gh run download <run-id>

# View Allure report
cd allure-report
open index.html
```

### **Re-run Failed Jobs**
```bash
# Using GitHub CLI
gh run rerun <run-id> --failed
```

---

## 🎊 Success Metrics

**Current Status:**
- ✅ Workflow configured
- ✅ Multi-browser testing
- ✅ Allure reporting integrated
- ✅ Docker validation
- ✅ Code quality checks
- ✅ Test summary generation

**On First Run:**
- ✅ 11 tests will run across 2 browsers = 22 test executions
- ✅ All should pass (100%)
- ✅ Allure report will be generated
- ✅ GitHub Pages will be updated

---

## 📚 Resources

- **GitHub Actions Docs**: https://docs.github.com/actions
- **Workflow Syntax**: https://docs.github.com/actions/reference/workflow-syntax-for-github-actions
- **Marketplace**: https://github.com/marketplace?type=actions

---

**Status**: ✅ Ready to use!
**Next Step**: Push code to trigger first workflow run

---

**Last Updated**: November 8, 2025
