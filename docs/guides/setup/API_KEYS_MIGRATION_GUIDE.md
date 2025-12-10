# 🔑 API Keys Migration Guide - Google Cloud Secret Manager

## Overview

This guide explains how to migrate hardcoded API keys and secrets to Google Cloud Secret Manager. **You will need to add the secrets to Google Cloud** - I cannot do this for you as it requires:
- Google Cloud CLI (`gcloud`) access
- Authentication to your Google Cloud project
- IAM permissions to create secrets
- Access to the actual secret values

---

## Prerequisites

### 1. Verify Google Cloud Access
```bash
# Check if gcloud is installed
gcloud --version

# Verify you're authenticated
gcloud auth list

# Verify project is set
gcloud config get-value project
# Should show: cscharer
```

### 2. Verify Permissions
You need the `Secret Manager Admin` or `Secret Manager Secret Accessor` role:
```bash
# Check your permissions
gcloud projects get-iam-policy cscharer --flatten="bindings[].members" --filter="bindings.members:user:YOUR_EMAIL"
```

---

## Migration Steps

### Step 1: Create Secrets in Google Cloud Secret Manager

**You must run these commands** - I cannot execute them for you.

#### GoToWebinar API Credentials:
```bash
# User ID
echo -n "jill.vivit@yahoo.com" | gcloud secrets create AUTO_VIVIT_GT_WEBINAR_USER_ID \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"

# Password
echo -n "vivitrules1" | gcloud secrets create AUTO_VIVIT_GT_WEBINAR_PASSWORD \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"

# Consumer Key
echo -n "WGhbDnxCGUwKNABGKeymjoII4gqalCa3" | gcloud secrets create AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"

# Consumer Secret
echo -n "DdkRQTJGLq4VF20t" | gcloud secrets create AUTO_VIVIT_GT_WEBINAR_CONSUMER_SECRET \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"
```

#### YourMembership API Credentials:
```bash
# API Key
echo -n "1879E438-29E0-41C2-AFAD-3E11A84BBEF7" | gcloud secrets create AUTO_VIVIT_YM_API_KEY \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"

# SA Passcode
echo -n "HNe6RO84P5sI" | gcloud secrets create AUTO_VIVIT_YM_API_SA_PASSCODE \
  --data-file=- \
  --project=cscharer \
  --replication-policy="automatic"
```

### Step 2: Verify Secrets Were Created

```bash
# List all secrets (should see the 6 new ones)
gcloud secrets list --project=cscharer | grep AUTO_VIVIT

# Verify a secret value (optional - for testing)
gcloud secrets versions access latest --secret="AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY" --project=cscharer
```

**Expected Output:**
```
AUTO_VIVIT_GT_WEBINAR_USER_ID
AUTO_VIVIT_GT_WEBINAR_PASSWORD
AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY
AUTO_VIVIT_GT_WEBINAR_CONSUMER_SECRET
AUTO_VIVIT_YM_API_KEY
AUTO_VIVIT_YM_API_SA_PASSCODE
```

### Step 3: Grant Access (If Needed)

If your application service account needs access:
```bash
# Get your service account email (from credentials file)
# Usually looks like: YOUR_PROJECT@appspot.gserviceaccount.com

# Grant access to all 6 secrets
gcloud secrets add-iam-policy-binding AUTO_VIVIT_GT_WEBINAR_USER_ID \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer

gcloud secrets add-iam-policy-binding AUTO_VIVIT_GT_WEBINAR_PASSWORD \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer

gcloud secrets add-iam-policy-binding AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer

gcloud secrets add-iam-policy-binding AUTO_VIVIT_GT_WEBINAR_CONSUMER_SECRET \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer

gcloud secrets add-iam-policy-binding AUTO_VIVIT_YM_API_KEY \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer

gcloud secrets add-iam-policy-binding AUTO_VIVIT_YM_API_SA_PASSCODE \
  --member="serviceAccount:YOUR_SERVICE_ACCOUNT@cscharer.iam.gserviceaccount.com" \
  --role="roles/secretmanager.secretAccessor" \
  --project=cscharer
```

---

## Code Changes (Already Done ✅)

The following code changes have been made:

1. **Created `EAPIKeys.java` enum** with VIVIT_ prefix:
   - `VIVIT_GT_WEBINAR_USER_ID`
   - `VIVIT_GT_WEBINAR_CONSUMER_KEY`
   - `VIVIT_GT_WEBINAR_CONSUMER_SECRET`
   - `VIVIT_YM_API_KEY`
   - `VIVIT_YM_API_SA_PASSCODE`

2. **Updated `EPasswords.java` enum**:
   - Added `VIVIT_GT_WEBINAR_PASSWORD`

3. **Updated `GTWebinarServiceTests.java`**:
   - Added `getUserId()` method (uses `EAPIKeys.VIVIT_GT_WEBINAR_USER_ID`)
   - Added `getPassword()` method (uses `EPasswords.VIVIT_GT_WEBINAR_PASSWORD`)
   - Added `getApiConsumerKey()` and `getApiConsumerSecret()` methods
   - Marked old constants (`USER_ID`, `PASSWORD`, `API_CONSUMER_KEY`, `API_CONSUMER_SECRET`) as `@Deprecated`
   - Updated `getAPIKey()` to use new getter

4. **Updated `YMService.java`**:
   - Added `getApiKeyValue()` and `getApiSaPasscodeValue()` methods
   - Marked old constants as `@Deprecated`
   - Updated `getAPIKey()` and `getSAPasscode()` to use new getters

5. **Updated references**:
   - `AuthNamespace.java` - Uses `getUserId()`, `getPassword()`, and `getApiConsumerKey()`
   - `GTWebinarDataTests.java` - Uses new getters

---

## Testing After Migration

### Step 1: Test Secret Retrieval
```bash
# Run a simple test to verify secrets can be retrieved
./mvnw test -Dtest=SecureConfigTest
```

### Step 2: Test API Functionality
```bash
# Test GoToWebinar API
./mvnw test -Dtest=GTWebinarDataTests#testAuth

# Test YourMembership API (if you have tests)
./mvnw test -Dtest=YMService
```

### Step 3: Verify No Hardcoded Values
```bash
# Pre-commit hook should pass
git add -A
git commit -m "test"  # Should pass without warnings
git reset HEAD~1      # Undo test commit
```

---

## Troubleshooting

### Error: "Permission denied"
**Solution**: Verify you have Secret Manager Admin role:
```bash
gcloud projects add-iam-policy-binding cscharer \
  --member="user:YOUR_EMAIL" \
  --role="roles/secretmanager.admin"
```

### Error: "Secret already exists"
**Solution**: The secret was already created. Verify it exists:
```bash
gcloud secrets describe AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY --project=cscharer
```

### Error: "Failed to fetch secret from Google Cloud Secret Manager"
**Solution**: 
1. Verify secret exists: `gcloud secrets list --project=cscharer`
2. Verify service account has access (see Step 3 above)
3. Verify credentials file is in correct location: `~/.config/gcloud/`

### Code Compiles But Tests Fail
**Solution**: Secrets may not be created yet. Create them first (Step 1), then test.

---

## What I Can Do vs What You Must Do

### ✅ I Can Do:
- Create the `EAPIKeys.java` enum
- Update all code references
- Provide exact commands to run
- Test code compilation
- Document the process

### ❌ I Cannot Do:
- Execute `gcloud` commands (requires your authentication)
- Create secrets in Google Cloud (requires your IAM permissions)
- Access your Google Cloud project
- Test secret retrieval (requires secrets to exist first)

---

## Next Steps

1. **You**: Run the `gcloud secrets create` commands (Step 1)
2. **You**: Verify secrets were created (Step 2)
3. **You**: Grant service account access if needed (Step 3)
4. **Both**: Test the migration (Testing section)
5. **Me**: Help troubleshoot any issues

---

## Quick Reference

**Secret Names:**
- `AUTO_VIVIT_GT_WEBINAR_USER_ID`
- `AUTO_VIVIT_GT_WEBINAR_PASSWORD`
- `AUTO_VIVIT_GT_WEBINAR_CONSUMER_KEY`
- `AUTO_VIVIT_GT_WEBINAR_CONSUMER_SECRET`
- `AUTO_VIVIT_YM_API_KEY`
- `AUTO_VIVIT_YM_API_SA_PASSCODE`

**Project ID:** `cscharer`

**Enum Values:**
- `EAPIKeys.VIVIT_GT_WEBINAR_USER_ID.getValue()`
- `EPasswords.VIVIT_GT_WEBINAR_PASSWORD.getValue()`
- `EAPIKeys.VIVIT_GT_WEBINAR_CONSUMER_KEY.getValue()`
- `EAPIKeys.VIVIT_GT_WEBINAR_CONSUMER_SECRET.getValue()`
- `EAPIKeys.VIVIT_YM_API_KEY.getValue()`
- `EAPIKeys.VIVIT_YM_API_SA_PASSCODE.getValue()`

---

**Created**: 2025-11-14
**Related To**: `docs/issues/open/hardcoded-api-keys-and-secrets.md`
**Status**: Code changes complete, awaiting Google Cloud secret creation

