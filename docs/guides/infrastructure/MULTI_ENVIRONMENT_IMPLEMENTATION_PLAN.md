---
**Type**: Analysis
**Purpose**: Implementation plan for multi-environment pipeline with environment selection
**Created**: 2025-11-13
**Last Updated**: 2025-11-13
**Maintained By**: AI Code Analysis System
**Status**: Active - Implementation in progress
---

# Multi-Environment Pipeline Implementation Plan
## Adding Dev/Test/Prod Environment Support with Selection

**User Request**: "The framework should have each of these environments capsulized and an environment parameter should be passed to determine what url the tests run in. We should also be able to run the pipeline and select the default run which would run all in order (i.e. dev then test then prod) or be able to select just one environment to run in."

---

## 🎯 CURRENT STATE ANALYSIS

### What Already Exists ✅:

1. **Environment Configuration** (`Environments.xml`):
   - `<ENVIRONMENT>TST</ENVIRONMENT>` setting exists
   - Framework reads this value

2. **Environment-Specific URLs** (`Companies.xml`):
   ```xml
   <Environment>
       <dev>
           <URL>http://dev-gen.company.com/app/</URL>
       </dev>
       <test>
           <URL>https://test.company.com/app/</URL>
       </test>
       <prod>
           <URL>http://prod.company.com/app/</URL>
       </prod>
   </Environment>
   ```

3. **Environment Class** (`Environment.java`):
   - Reads environment from XML
   - Has getEnvironment() and setEnvironment() methods

### What's Missing ❌:

1. **System Property Support**: Framework doesn't read `-Denvironment=dev` parameter
2. **Pipeline Selection**: Can't choose which environment(s) to run
3. **Environment Isolation**: No separate test execution per environment
4. **URL Resolution**: Need to map environment → URL from Companies.xml

---

## 🚀 IMPLEMENTATION PLAN

### Phase 1: Update Environment.java (Framework)
**Add system property override for environment selection**

### Phase 2: Update GitHub Actions Workflow
**Add workflow_dispatch inputs for environment selection**

### Phase 3: Update Pipeline Logic
**Add conditional execution for selected environments**

### Phase 4: Update Documentation
**Update PIPELINE_WORKFLOW.md with new capabilities**

---

## 📝 DETAILED IMPLEMENTATION

### Step 1: Enhance Environment.java

**File**: `src/test/java/com/cjs/qa/core/Environment.java`

**Add this method**:
```java
private static void setEnvironment(String tag, String config) {
    // First check for system property override (for CI/CD)
    String envFromSystem = System.getProperty("test.environment");
    if (envFromSystem != null && !envFromSystem.isEmpty()) {
        environment = envFromSystem.toUpperCase();
        sysOut("Environment set from system property: " + environment);
        return;
    }

    // Fall back to XML configuration
    try {
        String value = XML.getTagValue(FILE_CONFIG_XML, tag, config);
        if (value != null && !value.isEmpty()) {
            environment = value.toUpperCase();
            sysOut("Environment set from XML: " + environment);
        }
    } catch (Exception e) {
        sysOut("Using default environment: " + environment);
    }
}
```

**Benefits**:
- CI/CD can override with `-Dtest.environment=dev`
- Local runs use Environments.xml
- Clear logging of environment source

---

### Step 2: Update GitHub Actions Workflow

**File**: `.github/workflows/ci.yml`

**Add workflow_dispatch inputs**:
```yaml
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
  workflow_dispatch:
    inputs:
      environment:
        description: 'Environment to test'
        required: true
        default: 'all'
        type: choice
        options:
          - all      # Run dev → test → prod sequentially
          - dev      # Development environment only
          - test     # Test environment only
          - prod     # Production environment only

      test_suite:
        description: 'Test suite to run'
        required: false
        default: 'smoke'
        type: choice
        options:
          - smoke          # Quick smoke tests
          - ci             # CI test suite
          - extended       # Extended test suite
          - all            # All test suites
```

**Add environment URLs**:
```yaml
env:
  JAVA_VERSION: '21'
  MAVEN_OPTS: -Xmx2048m
  # Environment URLs (update these with your actual URLs)
  DEV_URL: 'https://dev.yourapp.com'
  TEST_URL: 'https://test.yourapp.com'
  PROD_URL: 'https://yourapp.com'
```

---

### Step 3: Create Environment-Specific Test Jobs

**Add new jobs for each environment**:

```yaml
  test-dev-environment:
    name: Test DEV Environment
    runs-on: ubuntu-latest
    needs: [detect-changes, build-and-compile]
    if: |
      needs.detect-changes.outputs.code-changed == 'true' &&
      (github.event.inputs.environment == 'all' ||
       github.event.inputs.environment == 'dev' ||
       github.event.inputs.environment == '')

    steps:
      - name: Run tests in DEV
        env:
          TEST_ENVIRONMENT: dev
          BASE_URL: ${{ env.DEV_URL }}
        run: |
          echo "🔧 Testing DEV environment"
          echo "URL: $BASE_URL"
          ./mvnw test -Dtest.environment=dev -DsuiteXmlFile=testng-smoke-suite.xml

  test-test-environment:
    name: Test TEST Environment
    runs-on: ubuntu-latest
    needs: [detect-changes, build-and-compile, test-dev-environment]
    if: |
      always() &&
      needs.detect-changes.outputs.code-changed == 'true' &&
      (needs.test-dev-environment.result == 'success' || needs.test-dev-environment.result == 'skipped') &&
      (github.event.inputs.environment == 'all' ||
       github.event.inputs.environment == 'test' ||
       github.event.inputs.environment == '')

    steps:
      - name: Run tests in TEST
        env:
          TEST_ENVIRONMENT: test
          BASE_URL: ${{ env.TEST_URL }}
        run: |
          echo "🧪 Testing TEST environment"
          echo "URL: $BASE_URL"
          ./mvnw test -Dtest.environment=test -DsuiteXmlFile=testng-smoke-suite.xml

  test-prod-environment:
    name: Test PROD Environment
    runs-on: ubuntu-latest
    needs: [detect-changes, build-and-compile, test-test-environment]
    if: |
      always() &&
      needs.detect-changes.outputs.code-changed == 'true' &&
      (needs.test-test-environment.result == 'success' || needs.test-test-environment.result == 'skipped') &&
      (github.event.inputs.environment == 'all' ||
       github.event.inputs.environment == 'prod')

    steps:
      - name: Run tests in PROD
        env:
          TEST_ENVIRONMENT: prod
          BASE_URL: ${{ env.PROD_URL }}
        run: |
          echo "🏭 Testing PROD environment"
          echo "URL: $BASE_URL"
          ./mvnw test -Dtest.environment=prod -DsuiteXmlFile=testng-smoke-suite.xml
```

---

## 🔄 EXECUTION LOGIC

### Scenario 1: Automatic (Push/PR)
```
environment input: (not set)
Result: Runs 'all' by default
Flow: dev → test → prod (sequential)
```

### Scenario 2: Manual - All Environments
```
User selects: environment='all'
Result: dev → test → prod (sequential)
- DEV runs first
- TEST runs only if DEV succeeds
- PROD runs only if TEST succeeds
```

### Scenario 3: Manual - Single Environment
```
User selects: environment='dev'
Result: Only DEV environment tested
- TEST and PROD jobs are skipped
```

### Scenario 4: Documentation-Only
```
Only .md files changed
Result: ALL environment tests skipped
- detect-changes sets code-changed=false
```

---

## 📋 IMPLEMENTATION CHECKLIST

### Framework Changes:
- [ ] Update Environment.java to read system property
- [ ] Test system property override locally
- [ ] Verify environment selection works

### Workflow Changes:
- [ ] Add workflow_dispatch inputs
- [ ] Add environment URL variables
- [ ] Create test-dev-environment job
- [ ] Create test-test-environment job
- [ ] Create test-prod-environment job
- [ ] Update job dependencies
- [ ] Test manual trigger with different selections

### Documentation Updates:
- [ ] Update PIPELINE_WORKFLOW.md
- [ ] Add environment selection section
- [ ] Update job flow diagrams
- [ ] Document execution scenarios
- [ ] Update troubleshooting section

### Testing:
- [ ] Test with environment='all'
- [ ] Test with environment='dev'
- [ ] Test with environment='test'
- [ ] Test with environment='prod'
- [ ] Verify sequential execution for 'all'
- [ ] Verify single environment execution

---

## 🎯 EXPECTED OUTCOMES

**Before**:
- Only runs in one environment (TST/dev)
- No environment selection
- Can't test specific environments

**After**:
- Select environment via workflow dispatch
- Run all environments sequentially
- Run single environment for quick testing
- Proper environment isolation
- Clear environment-specific results

---

**Ready to implement!** 🚀

---

*This plan will be executed and then this file will be archived.*
