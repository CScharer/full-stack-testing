# 🔐 Cleanup: Migrate Remaining Hardcoded Passwords to Google Cloud Secret Manager

## Issue Type
**Security Enhancement / Technical Debt**

## Priority
**Medium-High** (Not blocking, but should be addressed in next sprint)

## Description

While most passwords have been successfully migrated to Google Cloud Secret Manager, the pre-commit hook has identified **5 files** that still contain hardcoded passwords. These need to be migrated to maintain our zero-password policy.

## Affected Files

### 1. `src/test/java/com/cjs/qa/pluralsight/pages/LoginPage.java`
```java
final String password = "C@Training";
```
**Type**: Training/test account password
**Action**: Migrate to Google Cloud Secret Manager or mark as dummy/example

---

### 2. `src/test/java/com/cjs/qa/gt/Notes.txt`
```bash
curl -X POST ... "password"&user_id="jill.vivit@yahoo.com"&password="vivitrules1"
```
**Type**: API credentials in curl example
**Action**:
- If active credentials: Migrate to Secret Manager
- If example/documentation: Replace with placeholder `[PASSWORD]`
- Consider moving to separate documentation file

---

### 3. `src/test/java/com/cjs/qa/gt/api/namespace/webinar/AuthNamespace.java`
```java
curlStringBuilder.append("&password=\"" + PASSWORD + "\"");
```
**Type**: String building for API call
**Action**: Verify PASSWORD constant source - ensure it retrieves from EPasswords enum

---

### 4. `src/test/java/com/cjs/qa/junit/tests/ScenariosTests.java`
```java
String password = "";
```
**Type**: Empty password string
**Action**: Low priority - verify this is intentional test case

---

### 5. `src/test/java/com/cjs/qa/atlassian/Atlassian.java`
```java
final String password = "********";
```
**Type**: Masked password placeholder
**Action**: Replace with Secret Manager retrieval or mark as dummy

---

## Success Criteria

- [ ] All 5 files reviewed and passwords migrated/documented
- [ ] Pre-commit hook passes without password warnings
- [ ] All tests still pass after migration
- [ ] Documentation updated if any examples changed
- [ ] Verify EPasswords enum has necessary entries

## Implementation Approach

### Option 1: Migrate to Secret Manager (Recommended)
```java
// Before:
final String password = "C@Training";

// After:
final String password = EPasswords.PLURALSIGHT_TRAINING.getValue();
```

**Steps**:
1. Add secret to Google Cloud Secret Manager:
   ```bash
   gcloud secrets create AUTO_PLURALSIGHT_TRAINING_PASSWORD \
     --data-file=- <<< "C@Training"
   ```

2. Add entry to `EPasswords` enum:
   ```java
   PLURALSIGHT_TRAINING("AUTO_PLURALSIGHT_TRAINING_PASSWORD")
   ```

3. Update code to retrieve from enum

### Option 2: Mark as Dummy/Example (If Not Real Credentials)
```java
// Before:
final String password = "C@Training";

// After:
// DUMMY PASSWORD - For testing only, not a real credential
final String password = "dummy_password_for_testing";
```

### Option 3: Use Environment Variable
```java
// Before:
final String password = "C@Training";

// After:
final String password = System.getenv("PLURALSIGHT_TRAINING_PASSWORD");
```

---

## Estimated Effort

**Time**: 2-3 hours
- Review: 30 minutes
- Migration: 1-2 hours
- Testing: 30-60 minutes

**Complexity**: Low-Medium
- Most changes are straightforward
- May need to verify which passwords are real vs dummy

---

## Dependencies

- Google Cloud Secret Manager access (already configured ✅)
- `EPasswords` enum (already exists ✅)
- `SecureConfig` utility (already exists ✅)

---

## Testing Plan

After migration:

1. **Compile Check**:
   ```bash
   ./mvnw clean compile test-compile
   ```

2. **Run Affected Tests**:
   ```bash
   ./mvnw test -Dtest=ScenariosTests
   # Test other affected test classes
   ```

3. **Pre-commit Hook Verification**:
   ```bash
   git add -A
   git commit -m "test"  # Should pass without warnings
   git reset HEAD~1      # Undo test commit
   ```

4. **Smoke Tests**:
   ```bash
   docker-compose run --rm tests test -Dtest=SmokeTests
   ```

---

## Related Documentation

- **Secret Manager Guide**: `INTEGRATION_COMPLETE.md` (moved to PRIVATE/ folder)
- **Password Security Rules**: `docs/process/AI_WORKFLOW_RULES.md` (v2.3, Rule 2.5)
- **Previous Migration**: `docs/analysis/previous/ANALYSIS_PS_RESULTS.md` (43 passwords migrated successfully)

---

## Context

This issue was created following the addition of **Rule 2.5** to `AI_WORKFLOW_RULES.md` which establishes a zero-tolerance policy for hardcoded passwords. The pre-commit hook now flags these remaining passwords.

**Previous Migration Success**: 43 passwords were successfully migrated to Google Cloud Secret Manager in November 2025. These 5 files were likely missed or are special cases that need review.

---

## Labels Suggested

- `security`
- `technical-debt`
- `good-first-issue` (straightforward migration)
- `documentation` (if examples need updating)

---

## Acceptance Criteria

✅ **Definition of Done**:
1. All hardcoded passwords removed from the 5 identified files
2. Active credentials migrated to Google Cloud Secret Manager
3. Dummy/example passwords clearly marked as such
4. Pre-commit hook passes without password warnings
5. All tests pass after changes
6. Documentation updated if needed
7. CHANGE.log updated with migration details

---

## Notes

- This is not a critical security issue as the main codebase already uses Secret Manager
- These appear to be edge cases or test-specific passwords
- Some may be dummy passwords used for examples/documentation
- Review before migrating to determine which are real credentials

---

**Created**: 2025-11-12
**Detected By**: Pre-commit hook (`check-secrets`)
**Related Commit**: d77311af (Added password security rules)
**Reference**: AI_WORKFLOW_RULES.md v2.3, Rule 2.5
