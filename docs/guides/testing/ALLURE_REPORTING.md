# Allure Test Reporting

**Status**: ✅ Configured
**Version**: Allure 2.31.0
**Framework**: TestNG
**Date**: November 8, 2025

---

## 🎯 Overview

Allure Framework provides beautiful, interactive HTML test reports with:
- 📊 **Visual Dashboards** - Graphs, charts, and trends
- 📸 **Screenshot Support** - Attach screenshots on failures
- ⏱️ **Performance Metrics** - Test execution times
- 📈 **Historical Trends** - Track improvements over time
- 🏷️ **Categorization** - Group by Epic, Feature, Story, Severity
- 📝 **Detailed Steps** - See exactly what each test did

---

## ✅ What's Configured

### Dependencies Added (pom.xml)
- `allure-testng:2.31.0` - TestNG integration
- `allure-java-commons:2.31.0` - Core Allure functionality
- `aspectjweaver:1.9.22` - For Allure step tracking

### Maven Plugins
- `maven-surefire-plugin` - Configured with Allure listener
- `allure-maven:2.12.0` - For report generation

### Annotated Tests
- ✅ `SimpleGridTest.java` - 3 tests with Allure annotations
- ✅ `EnhancedGridTests.java` - 8 tests with Allure annotations
- Total: **11 tests** ready for Allure reporting

---

## 🚀 Quick Start

### Method 1: Using Helper Script (Easiest)

```bash
# One command does everything:
./scripts/generate-allure-report.sh

# This will:
# 1. Start Selenium Grid
# 2. Run all tests
# 3. Stop Grid
# 4. Generate Allure report
# 5. Open in browser automatically
```

### Method 2: Manual Steps

```bash
# 1. Start Grid
docker-compose up -d selenium-hub chrome-node-1

# 2. Run tests
docker-compose run --rm tests -Dtest=SimpleGridTest,EnhancedGridTests

# 3. Generate report (requires Allure CLI)
allure serve target/allure-results

# 4. Stop Grid
docker-compose down
```

### Method 3: Using Maven Plugin

```bash
# Run tests
./mvnw clean test -Dtest=SimpleGridTest

# Generate report
./mvnw allure:serve
# Opens browser with interactive report
```

---

## 📦 Installing Allure CLI

### macOS (Homebrew)
```bash
brew install allure
```

### Linux (Manual)
```bash
wget https://github.com/allure-framework/allure2/releases/download/2.31.0/allure-2.31.0.tgz
tar -zxvf allure-2.31.0.tgz
sudo mv allure-2.31.0 /opt/allure
export PATH="/opt/allure/bin:$PATH"
```

### Windows (Scoop)
```bash
scoop install allure
```

### Verify Installation
```bash
allure --version
# Should show: 2.31.0
```

---

## 📊 Report Features

### What You'll See

**1. Overview Dashboard**
- Total tests run
- Pass/Fail rate
- Test duration
- Trend graphs

**2. Test Suites**
- Organized by package
- Each test with status
- Execution time
- Error details (if any)

**3. Categorization**
```java
@Epic("Selenium Grid Testing")     // High-level feature area
@Feature("Enhanced Grid Tests")    // Specific feature
@Story("Search Functionality")     // User story
@Severity(SeverityLevel.CRITICAL)  // Importance level
@Description("...")                // Detailed description
```

**4. Test Steps**
```java
Allure.step("Navigate to Google homepage");
Allure.step("Enter search term: Selenium");
Allure.step("Submit search");
```

**5. Attachments** (can add)
```java
// Attach screenshot on failure
Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));

// Attach logs
Allure.addAttachment("Browser Log", "text/plain", browserLog);
```

---

## 🎨 Current Test Annotations

### SimpleGridTest (3 tests)
- **Epic**: Selenium Grid Testing
- **Feature**: Basic Grid Tests
- **Stories**: Grid Connection, Basic Navigation
- **Severities**: BLOCKER (connection), CRITICAL (navigation)

### EnhancedGridTests (8 tests)
- **Epic**: Selenium Grid Testing
- **Feature**: Enhanced Grid Tests
- **Stories**:
  - Homepage Navigation
  - Search Functionality
  - Multi-Site Navigation
  - Performance Testing
  - Browser Features
  - Form Interactions
  - Responsive Design
- **Severities**: CRITICAL, NORMAL, MINOR

---

## 📈 Example Report Structure

```
Allure Report
├── Overview
│   ├── 11 tests total
│   ├── 11 passed (100%)
│   ├── 0 failed
│   └── Duration: 16.4 seconds
│
├── Suites
│   ├── SimpleGridTest (3 tests)
│   └── EnhancedGridTests (8 tests)
│
├── Graphs
│   ├── Status pie chart
│   ├── Severity distribution
│   ├── Duration graph
│   └── Timeline
│
├── Categories
│   ├── By Epic
│   ├── By Feature
│   ├── By Story
│   └── By Severity
│
└── Timeline
    └── Test execution flow
```

---

## 💡 Usage Examples

### Run Specific Test Suite
```bash
# Run only simple tests
docker-compose run --rm tests -Dtest=SimpleGridTest
allure serve target/allure-results

# Run only enhanced tests
docker-compose run --rm tests -Dtest=EnhancedGridTests
allure serve target/allure-results

# Run both
docker-compose run --rm tests -Dtest=SimpleGridTest,EnhancedGridTests
allure serve target/allure-results
```

### Run with TestNG Suite XML
```bash
# Parallel execution across browsers
docker-compose run --rm tests -DsuiteXmlFile=testng-grid-suite.xml
allure serve target/allure-results
```

### Generate Report Without Opening
```bash
# Generate static HTML report
allure generate target/allure-results -o target/allure-report --clean

# Open manually
open target/allure-report/index.html
```

---

## 🔧 Configuration Files

### allure.properties
Located: `src/test/resources/allure.properties`

```properties
allure.results.directory=target/allure-results
allure.link.issue.pattern=https://github.com/CScharer/full-stack-testing/issues/{}
allure.link.tms.pattern=https://github.com/CScharer/full-stack-testing/issues/{}
```

### Maven Surefire Configuration
```xml
<configuration>
    <properties>
        <property>
            <name>listener</name>
            <value>io.qameta.allure.testng.AllureTestNg</value>
        </property>
    </properties>
    <argLine>
        -javaagent:"${settings.localRepository}/org/aspectj/aspectjweaver/${aspectj.version}/aspectjweaver-${aspectj.version}.jar"
    </argLine>
</configuration>
```

---

## 🎯 Test Results

### Latest Test Run
```
✅ Tests run: 11
✅ Failures: 0
✅ Errors: 0
✅ Skipped: 0
✅ Time: 16.4 seconds
✅ Success Rate: 100%
```

### Test Breakdown
| Test Suite | Tests | Status |
|------------|-------|--------|
| SimpleGridTest | 3 | ✅ All Passing |
| EnhancedGridTests | 8 | ✅ All Passing |
| **Total** | **11** | **100%** |

---

## 📸 Adding Screenshots

### On Test Failure
```java
@AfterMethod
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        // Capture screenshot
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Failure Screenshot",
            new ByteArrayInputStream(screenshot));
    }
    driver.quit();
}
```

### During Test Steps
```java
@Test
public void myTest() {
    Allure.step("Navigate to page", () -> {
        driver.get("https://example.com");
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Page Screenshot", new ByteArrayInputStream(screenshot));
    });
}
```

---

## 🏷️ Annotation Reference

### Severity Levels
```java
@Severity(SeverityLevel.BLOCKER)   // Critical infrastructure
@Severity(SeverityLevel.CRITICAL)  // Core functionality
@Severity(SeverityLevel.NORMAL)    // Standard features
@Severity(SeverityLevel.MINOR)     // Nice-to-have
@Severity(SeverityLevel.TRIVIAL)   // Cosmetic issues
```

### Organization
```java
@Epic("Feature Area")              // High-level grouping
@Feature("Specific Feature")       // Feature being tested
@Story("User Story")               // User story reference
@Owner("Developer Name")           // Test owner
@Link("https://...")               // Related links
@Issue("JIRA-123")                // Issue tracking
@TmsLink("TC-456")                // Test management system
```

### Test Metadata
```java
@Description("Detailed test description")
@Severity(SeverityLevel.CRITICAL)
@Flaky                            // Mark as occasionally flaky
@Muted                            // Muted from results
```

---

## 📂 Report Locations

### Allure Results (JSON)
```
target/allure-results/
├── *-result.json          (Test results)
├── *-container.json       (Test containers)
└── *-attachment.*         (Screenshots, logs)
```

### Allure Report (HTML)
```
target/allure-report/
├── index.html            (Main dashboard)
├── data/                 (Report data)
├── widgets/              (UI components)
└── history/              (Historical data)
```

---

## 🎯 Next Steps

### 1. Install Allure CLI
```bash
brew install allure
```

### 2. Run Tests and Generate Report
```bash
./scripts/generate-allure-report.sh
```

### 3. View Report
The report will automatically open in your default browser!

---

## 🌟 Advanced Features

### Historical Trends
Keep `allure-results/history` folder to track trends over time:
```bash
# After generating report, save history
cp -r target/allure-report/history target/allure-results/

# Next run will show trends!
```

### Environment Information
Create `target/allure-results/environment.properties`:
```properties
Browser=Chrome
Browser.Version=120
Selenium.Version=4.39.0
Grid.URL=http://selenium-hub:4444
Java.Version=21
```

### Categories (Custom)
Create `target/allure-results/categories.json`:
```json
[
  {
    "name": "Product Defects",
    "matchedStatuses": ["failed"]
  },
  {
    "name": "Test Defects",
    "matchedStatuses": ["broken"]
  }
]
```

---

## 🎊 Benefits

### For Developers
- ✅ Clear test status at a glance
- ✅ Detailed failure information
- ✅ Easy to debug with screenshots
- ✅ Historical comparison

### For Management
- ✅ Professional visual reports
- ✅ Test coverage visibility
- ✅ Quality trends over time
- ✅ ROI demonstration

### For QA Team
- ✅ Test documentation
- ✅ Regression tracking
- ✅ Flaky test identification
- ✅ Performance monitoring

---

## 📞 Troubleshooting

### "allure: command not found"
Install Allure CLI:
```bash
brew install allure
```

### "No Allure results found"
Make sure tests ran successfully:
```bash
docker-compose run --rm tests -Dtest=SimpleGridTest
ls -la target/allure-results/
```

### Report doesn't open
Generate manually:
```bash
allure generate target/allure-results -o target/allure-report --clean
open target/allure-report/index.html
```

---

## 🔗 Resources

- **Allure Documentation**: https://docs.qameta.io/allure/
- **TestNG Integration**: https://docs.qameta.io/allure/#_testng
- **Allure GitHub**: https://github.com/allure-framework/allure2
- **Examples**: https://github.com/allure-examples

---

## 📝 Example Report Output

When you run `allure serve target/allure-results`, you'll see:

```
Generating report to temp directory...
Report successfully generated to /var/folders/.../allure-report
Starting web server...
Server started at <http://192.168.1.100:63342>. Press <Ctrl+C> to exit
```

Your browser will automatically open showing:
- **Overview** - Summary dashboard with graphs
- **Suites** - All test suites and their tests
- **Graphs** - Visual representation of results
- **Timeline** - Test execution timeline
- **Behaviors** - Organized by Epic/Feature/Story
- **Packages** - Organized by package structure

---

**Status**: ✅ Ready to use!
**Next Step**: Run `./scripts/generate-allure-report.sh` or install Allure CLI

---

**Last Updated**: November 8, 2025
