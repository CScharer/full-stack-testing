# Issues & Tracking
## GitHub Issue Templates and Work Items

**Purpose**: Track planned improvements, bugs, and technical debt
**Lifecycle**: Move from `open/` to GitHub Issues when created
**Last Updated**: November 12, 2025

---

## 📂 Folder Structure

```
issues/
├── README.md (this file)
└── open/                    Pending issues to be created on GitHub
    ├── cleanup-hardcoded-passwords.md
    └── missing-performance-test-files.md
```

---

## 🎯 Purpose

This folder contains **issue templates** ready to be created on GitHub Issues. These are well-documented work items that need to be tracked and addressed.

---

## 📋 Current Open Issues

### 1. Cleanup Hardcoded Passwords
**File**: `open/cleanup-hardcoded-passwords.md`
**Priority**: Medium-High
**Effort**: 2-3 hours
**Description**: Migrate 5 remaining hardcoded passwords to Google Cloud Secret Manager

**Files Affected**:
- `src/test/java/com/cjs/qa/pluralsight/pages/LoginPage.java`
- `src/test/java/com/cjs/qa/gt/Notes.txt`
- `src/test/java/com/cjs/qa/gt/api/namespace/webinar/AuthNamespace.java`
- `src/test/java/com/cjs/qa/junit/tests/ScenariosTests.java`
- `src/test/java/com/cjs/qa/atlassian/Atlassian.java`

---

### 2. Missing Performance Test Files
**File**: `open/missing-performance-test-files.md`
**Priority**: High (CI/CD failing)
**Effort**: 12-16 hours
**Description**: Create performance test files for Locust, Gatling, and JMeter

**Missing Files**:
- `src/test/locust/*.py` (3 files)
- `src/test/scala/*.scala` (2 files)
- `src/test/jmeter/*.jmx` (2 files)

**Quick Fix**: Disable `.github/workflows/performance.yml` scheduled runs (5 minutes)

---

## 🔄 Workflow

### Creating Issues on GitHub:

**Option 1: GitHub Web UI**
1. Go to repository Issues page
2. Click "New Issue"
3. Copy content from file in `open/`
4. Paste as issue description
5. Add appropriate labels
6. Submit issue
7. Delete or move the file from `open/` after creation

**Option 2: GitHub CLI**
```bash
# From repo root
gh issue create \
  --title "Issue Title" \
  --body-file docs/issues/open/issue-name.md \
  --label "security,bug"
```

**Option 3: Keep in docs/**
- Some teams prefer tracking issues in markdown files
- Can be converted to GitHub Issues later
- Provides version-controlled issue tracking

---

## 📁 Lifecycle Management

### States:

**open/** - Issues ready to be created
- Well-documented work items
- Complete with context, impact, effort estimates
- Ready to copy to GitHub Issues

**GitHub Issues** - Active tracking
- Create on GitHub from templates
- Track progress there
- Link back to relevant docs

**archive/** - Completed issues
- Move to `docs/archive/YYYY-MM/` when resolved
- Include resolution details
- Keep for historical reference

---

## ✅ Issue Template Requirements

Every issue template should include:

1. **Priority**: Critical/High/Medium/Low
2. **Type**: Bug/Enhancement/Technical Debt
3. **Effort Estimate**: Hours or story points
4. **Description**: Clear problem statement
5. **Impact**: What's affected
6. **Proposed Solution**: How to fix
7. **Acceptance Criteria**: Definition of done
8. **Related Files**: What needs to change
9. **Testing Plan**: How to verify fix

---

## 🎯 When to Create Issue Templates

Create templates in `open/` for:
- ✅ Technical debt that needs tracking
- ✅ Planned enhancements
- ✅ Known bugs to be fixed later
- ✅ Missing implementations
- ✅ Cleanup tasks

Don't create templates for:
- ❌ Immediate fixes (just fix them)
- ❌ Documentation updates (do them directly)
- ❌ Trivial changes (not worth tracking)

---

## 📊 Current Statistics

**Open Issue Templates**: 2
- Cleanup hardcoded passwords (2-3 hours)
- Missing performance test files (12-16 hours)

**Total Effort**: 14-19 hours of documented work

---

## 🚀 Next Actions

**Immediate**:
- [ ] Create GitHub issues from templates
- [ ] Apply appropriate labels
- [ ] Assign to team members if applicable

**This Week**:
- [ ] Address the performance test issue (disable workflow or create quick fix)
- [ ] Schedule password cleanup work

**This Month**:
- [ ] Complete both issues
- [ ] Update this folder as new issues are identified

---

**Maintained By**: CJS QA Team
**Created**: November 12, 2025
**Version**: 1.0
