/// <reference types="cypress" />

describe('Google Search Tests', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('should display Google homepage', () => {
    // Google now uses textarea instead of input for search box
    cy.get('textarea[name="q"], input[name="q"]').should('be.visible')
    cy.title().should('contain', 'Google')
  })

  it('should perform a search', () => {
    const searchQuery: string = 'Cypress testing framework'
    // Use flexible selector that works with both textarea and input
    cy.get('textarea[name="q"], input[name="q"]').type(searchQuery)
    cy.get('textarea[name="q"], input[name="q"]').type('{enter}')
    cy.url().should('include', 'search')
    cy.get('#search').should('be.visible')
  })

  it('should navigate to search results', () => {
    // Use flexible selector that works with both textarea and input
    cy.get('textarea[name="q"], input[name="q"]').type('Selenium WebDriver{enter}')
    cy.get('h3').should('exist')
    cy.get('a[href*="selenium.dev"]').should('exist')
  })
})

