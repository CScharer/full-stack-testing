# Helper Scripts

This directory contains helper scripts for common development tasks.

## Available Scripts

### `run-tests.sh`
Run test suite with optional parameters.

```bash
# Run default test suite (Scenarios) with chrome
./scripts/run-tests.sh

# Run specific suite with specific browser
./scripts/run-tests.sh Scenarios firefox

# Run Google tests
./scripts/run-tests.sh Scenarios chrome
```

### `run-specific-test.sh`
Run a specific test method.

```bash
# Run a specific test
./scripts/run-specific-test.sh Scenarios Google

# Run Microsoft test
./scripts/run-specific-test.sh Scenarios Microsoft
```

### `compile.sh`
Compile the project without running tests.

```bash
./scripts/compile.sh
```

## Making Scripts Executable

If you need to make scripts executable:

```bash
chmod +x scripts/*.sh
```

## Using Maven Wrapper

All scripts use `./mvnw` (Maven wrapper) instead of `mvn`. This ensures everyone uses the same Maven version without needing to install Maven separately.
