// ***********************************************************
// This example support/e2e.ts is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.ts using ES2015 syntax:
import './commands'

// Handle uncaught exceptions (e.g., Google CAPTCHA challenges)
// This is common when running automated tests against Google
Cypress.on('uncaught:exception', (err, runnable) => {
  // Ignore Google CAPTCHA/sorry page errors
  if (err.message.includes('solveSimpleChallenge') || 
      err.message.includes('sorry/index') ||
      err.message.includes('uncaught exception')) {
    // Return false to prevent the error from failing the test
    // Note: The test may still fail if it can't find expected elements
    return false
  }
  // Don't prevent other errors from failing the test
  return true
})

// Alternatively you can use CommonJS syntax:
// require('./commands')

