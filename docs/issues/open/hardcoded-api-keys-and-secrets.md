# 🔑 Security: Migrate Hardcoded API Keys and Secrets to Google Cloud Secret Manager

## Issue Type
**Security Enhancement / Technical Debt**

## Priority
**High** (API keys and secrets are sensitive credentials)

## Description

While passwords have been addressed, the codebase contains **hardcoded API keys and secrets** that need to be migrated to Google Cloud Secret Manager to maintain our zero-credential policy.

## Affected Files

### 1. `src/test/java/com/cjs/qa/gt/api/services/GTWebinarServiceTests.java`
**Credentials Found:**
```java
public static final String API_CONSUMER_KEY = "WGhbDnxCGUwKNABGKeymjoII4gqalCa3";
public static final String API_CONSUMER_SECRET = "DdkRQTJGLq4VF20t";
```

**Type**: GoToWebinar API OAuth credentials
**Usage**: Used in OAuth authentication for GoToWebinar API
**Action**: Migrate both to Google Cloud Secret Manager

**Referenced In:**
- `GTWebinarDataTests.java` (line 66)
- `AuthNamespace.java` (lines 32, 46)

---

### 2. `src/test/java/com/cjs/qa/ym/api/services/YMService.java`
**Credentials Found:**
```java
public static final String API_KEY = "1879E438-29E0-41C2-AFAD-3E11A84BBEF7";
public static final String API_SA_PASSCODE = "HNe6RO84P5sI";
```

**Type**: YourMembership API credentials
**Usage**: Used for YourMembership API authentication
**Action**: Migrate both to Google Cloud Secret Manager

**Referenced In:**
- `YMService.java` (line 191 - getAPIKey method)

---

### 3. `src/test/java/com/cjs/qa/gt/Notes.txt`
**Credentials Found:**
```
Consumer Key: WGhbDnxCGUwKNABGKeymjoII4gqalCa3
Consumer Secret: DdkRQTJGLq4VF20t

Consumer Key: j0OYZLREJ0E4CY8pxcI7pAClF6m26OQ6
Consumer Secret: QiTNRPL5W9VGvRri

Consumer Key: LIoKpcSQNQEoTLLb4tI17WGgE8ADPqzn
Consumer Secret: 4dAqAM8B7opvxnbA
```

**Type**: Documentation/notes file with multiple API credentials
**Action**:
- Replace with placeholders `[CONSUMER_KEY]` and `[CONSUMER_SECRET]`
- If these are active credentials, they should also be in Secret Manager
- If documentation only, use placeholders

---

## Summary

**Total Credentials Found:**
- **API Keys**: 1 (YMService.API_KEY)
- **API Secrets**: 1 (YMService.API_SA_PASSCODE)
- **Consumer Keys**: 1 active + 2 in docs (GTWebinarServiceTests.API_CONSUMER_KEY)
- **Consumer Secrets**: 1 active + 2 in docs (GTWebinarServiceTests.API_CONSUMER_SECRET)

**Active Credentials (Code):**
1. `GTWebinarServiceTests.API_CONSUMER_KEY` = "WGhbDnxCGUwKNABGKeymjoII4gqalCa3"
2. `GTWebinarServiceTests.API_CONSUMER_SECRET` = "DdkRQTJGLq4VF20t"
3. `YMService.API_KEY` = "1879E438-29E0-41C2-AFAD-3E11A84BBEF7"
4. `YMService.API_SA_PASSCODE` = "HNe6RO84P5sI"

**Documentation Only:**
- `Notes.txt` contains 3 sets of consumer keys/secrets (may be examples or additional credentials)

---

## Implementation Approach

### Option 1: Extend EPasswords Enum (Recommended)
Create a new enum `EAPIKeys` similar to `EPasswords`:

```java
public enum EAPIKeys {
  GT_WEBINAR_CONSUMER_KEY("AUTO_GT_WEBINAR_CONSUMER_KEY"),
  GT_WEBINAR_CONSUMER_SECRET("AUTO_GT_WEBINAR_CONSUMER_SECRET"),
  YM_API_KEY("AUTO_YM_API_KEY"),
  YM_API_SA_PASSCODE("AUTO_YM_API_SA_PASSCODE");

  private final String secretKey;

  EAPIKeys(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getValue() {
    return SecureConfig.getPassword(this.secretKey);
  }

  public String getSecretKey() {
    return this.secretKey;
  }
}
```

**Usage:**
```java
// Before:
public static final String API_CONSUMER_KEY = "WGhbDnxCGUwKNABGKeymjoII4gqalCa3";

// After:
public static String getApiConsumerKey() {
  return EAPIKeys.GT_WEBINER_CONSUMER_KEY.getValue();
}
```

### Option 2: Use SecureConfig Directly
```java
// Before:
public static final String API_CONSUMER_KEY = "WGhbDnxCGUwKNABGKeymjoII4gqalCa3";

// After:
public static String getApiConsumerKey() {
  return SecureConfig.getPassword("AUTO_GT_WEBINAR_CONSUMER_KEY");
}
```

---

## Migration Steps

### Step 1: Create Secrets in Google Cloud Secret Manager
```bash
# GoToWebinar credentials
gcloud secrets create AUTO_GT_WEBINAR_CONSUMER_KEY \
  --data-file=- <<< "WGhbDnxCGUwKNABGKeymjoII4gqalCa3"

gcloud secrets create AUTO_GT_WEBINAR_CONSUMER_SECRET \
  --data-file=- <<< "DdkRQTJGLq4VF20t"

# YourMembership credentials
gcloud secrets create AUTO_YM_API_KEY \
  --data-file=- <<< "1879E438-29E0-41C2-AFAD-3E11A84BBEF7"

gcloud secrets create AUTO_YM_API_SA_PASSCODE \
  --data-file=- <<< "HNe6RO84P5sI"
```

### Step 2: Create EAPIKeys Enum
- Create `src/test/java/com/cjs/qa/core/security/EAPIKeys.java`
- Follow same pattern as `EPasswords.java`

### Step 3: Update Code
- Replace hardcoded constants with enum calls
- Update all references
- Test to ensure functionality unchanged

### Step 4: Update Documentation
- Replace credentials in `Notes.txt` with placeholders
- Document which credentials are active vs examples

---

## Success Criteria

- [ ] All 4 active API keys/secrets migrated to Secret Manager
- [ ] EAPIKeys enum created and functional
- [ ] All code references updated
- [ ] Notes.txt updated with placeholders
- [ ] Pre-commit hook passes without warnings
- [ ] All tests still pass after migration
- [ ] Documentation updated

---

## Estimated Effort

**Time**: 3-4 hours
- Secret Manager setup: 30 minutes
- Enum creation: 30 minutes
- Code updates: 1-2 hours
- Testing: 1 hour

**Complexity**: Medium
- Similar to password migration (already done successfully)
- Need to handle static final constants differently (may need getter methods)

---

## Dependencies

- Google Cloud Secret Manager access (already configured ✅)
- `SecureConfig` utility (already exists ✅)
- `EPasswords` enum pattern (can replicate ✅)

---

## Testing Plan

After migration:

1. **Compile Check**:
   ```bash
   ./mvnw clean compile test-compile
   ```

2. **Run Affected Tests**:
   ```bash
   ./mvnw test -Dtest=GTWebinarDataTests
   ./mvnw test -Dtest=YMService
   ```

3. **Pre-commit Hook Verification**:
   ```bash
   git add -A
   git commit -m "test"  # Should pass without warnings
   git reset HEAD~1      # Undo test commit
   ```

4. **Smoke Tests**:
   ```bash
   ./mvnw test -DsuiteXmlFile=testng-smoke-suite.xml
   ```

---

## Related Documentation

- **Secret Manager Guide**: `INTEGRATION_COMPLETE.md` (moved to PRIVATE/ folder)
- **Password Security Rules**: `docs/process/AI_WORKFLOW_RULES.md` (Rule 2.6)
- **Previous Password Migration**: `docs/issues/open/cleanup-hardcoded-passwords.md`

---

## Notes

- API keys and secrets are as sensitive as passwords
- Should follow same security practices as password migration
- Consider creating separate enum for API keys vs passwords for clarity
- Some credentials in Notes.txt may be examples - verify before migrating

---

**Created**: 2025-11-14
**Detected By**: Manual review (user identified API_CONSUMER_KEY and API_CONSUMER_SECRET)
**Related To**: Password cleanup issue (docs/issues/open/cleanup-hardcoded-passwords.md)
**Priority**: High (security-sensitive credentials)
