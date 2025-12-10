# 🔐 Decision Guide: 3 Remaining Password Files

## What You Need to Decide

For each of the 3 files below, you need to decide:

**Option A: Keep as-is (Dummy/Placeholder)**
- The password is a test/dummy value, not a real credential
- It's safe to leave in the code
- No migration needed

**Option B: Migrate to Secret Manager**
- The password is a real credential
- It should be moved to Google Cloud Secret Manager
- Code should be updated to use enum getters

---

## File #1: `src/test/java/com/cjs/qa/pluralsight/pages/LoginPage.java`

### Current State:
```java
// DUMMY PASSWORD - For testing/training only, not a real credential
// TODO: Migrate to Google Cloud Secret Manager if this becomes a production credential
final String password = "C@Training";
```

### Questions to Ask Yourself:
1. **Is "C@Training" a real password for a real account?**
   - ✅ **If NO** → Keep as-is (Option A)
   - ❌ **If YES** → Migrate to Secret Manager (Option B)

2. **Is this a test/training account that doesn't matter if exposed?**
   - ✅ **If YES** → Keep as-is (Option A)
   - ❌ **If NO** → Migrate to Secret Manager (Option B)

3. **Does this account have access to real data?**
   - ✅ **If NO** → Keep as-is (Option A)
   - ❌ **If YES** → Migrate to Secret Manager (Option B)

### Recommendation:
**If it's truly a dummy/test account** → **Option A (Keep as-is)**
- The comment already says "DUMMY PASSWORD - For testing/training only"
- If it's not a real credential, it's acceptable to leave it

**If it's a real account** → **Option B (Migrate)**
- Add to `EPasswords` enum
- Update code to use `EPasswords.PLURALSIGHT_TRAINING.getValue()`

---

## File #2: `src/test/java/com/cjs/qa/gt/Notes.txt`

### Current State:
```
Consumer Key: WGhbDnxCGUwKNABGKeymjoII4gqalCa3
Consumer Secret: DdkRQTJGLq4VF20t

Consumer Key: j0OYZLREJ0E4CY8pxcI7pAClF6m26OQ6
Consumer Secret: QiTNRPL5W9VGvRri

Consumer Key: LIoKpcSQNQEoTLLb4tI17WGgE8ADPqzn
Consumer Secret: 4dAqAM8B7opvxnbA
```

### Questions to Ask Yourself:
1. **Are these active credentials you're using?**
   - ✅ **If YES** → They should be in Secret Manager (Option B)
   - ❌ **If NO** → Replace with placeholders (Option A)

2. **Are these old/expired credentials?**
   - ✅ **If YES** → Replace with placeholders (Option A)
   - ❌ **If NO** → Check if they're active

3. **Are these example/documentation credentials?**
   - ✅ **If YES** → Replace with placeholders like `[CONSUMER_KEY]` (Option A)
   - ❌ **If NO** → Migrate if active (Option B)

### Note:
- The first set (WGhbDnxCGUwKNABGKeymjoII4gqalCa3 / DdkRQTJGLq4VF20t) **has already been migrated** to Secret Manager
- The other two sets (j0OYZLREJ0E4CY8pxcI7pAClF6m26OQ6 and LIoKpcSQNQEoTLLb4tI17WGgE8ADPqzn) need a decision

### Recommendation:
**Option A: Replace with placeholders** (if they're examples/old)
```txt
Consumer Key: [CONSUMER_KEY]
Consumer Secret: [CONSUMER_SECRET]
```

**Option B: Migrate to Secret Manager** (if they're active)
- Add to `EAPIKeys` enum
- Create secrets in Google Cloud
- Update Notes.txt to reference the enum

---

## File #3: `src/test/java/com/cjs/qa/atlassian/Atlassian.java`

### Current State:
```java
// TODO: Replace with EPasswords enum or Google Cloud Secret Manager
// This is a placeholder - actual password should be retrieved from Secret Manager
final String password = "********";
```

### Questions to Ask Yourself:
1. **Is "********" actually used, or is it a placeholder that doesn't work?**
   - ✅ **If it's a real password** → Migrate to Secret Manager (Option B)
   - ❌ **If it's just a placeholder** → Keep as-is or fix the code (Option A)

2. **Does this code actually run successfully?**
   - ✅ **If YES** → "********" might be a real password (Option B - Migrate)
   - ❌ **If NO** → It's a placeholder, code needs to be fixed (Option A)

3. **Is there a real Atlassian account this should use?**
   - ✅ **If YES** → Migrate real password to Secret Manager (Option B)
   - ❌ **If NO** → Keep placeholder or remove the code (Option A)

### Recommendation:
**Option A: Fix the code** (if it's just a placeholder)
- The code won't work with "********" as a password
- Either remove this functionality or implement it properly with Secret Manager

**Option B: Migrate to Secret Manager** (if there's a real password)
- Add to `EPasswords` enum
- Update code to use `EPasswords.ATLASSIAN.getValue()`

---

## Summary Table

| File | Current Value | Decision Needed | Recommendation |
|------|--------------|----------------|----------------|
| **LoginPage.java** | `"C@Training"` | Is this a real password? | **Option A** if dummy, **Option B** if real |
| **Notes.txt** | Multiple Consumer Keys/Secrets | Are these active? | **Option A** if examples, **Option B** if active |
| **Atlassian.java** | `"********"` | Is this a real password? | **Option A** if placeholder, **Option B** if real |

---

## How to Decide

### Quick Test:
1. **Try to use the password/credential**
2. **Does it work?**
   - ✅ **If YES** → It's real, migrate it (Option B)
   - ❌ **If NO** → It's dummy/placeholder, keep as-is (Option A)

### Security Rule:
- **If it's a real credential that works** → Must migrate to Secret Manager
- **If it's a dummy/test value** → Can stay in code (but document it clearly)

---

## Next Steps After Decision

### If You Choose Option A (Keep as-is):
1. Ensure comments clearly state it's a dummy/placeholder
2. Consider adding to `.gitignore` if it's sensitive
3. Document in code why it's acceptable

### If You Choose Option B (Migrate):
1. Add to `EPasswords` or `EAPIKeys` enum
2. Create secret in Google Cloud Secret Manager
3. Update code to use enum getter
4. Remove hardcoded value
5. Test to ensure it works

---

**Questions?** Review each file and decide based on whether the credentials are real or dummy/test values.

