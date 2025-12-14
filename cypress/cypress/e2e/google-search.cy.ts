/// <reference types="cypress" />

describe('Google Search Tests', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('should display Google homepage', () => {
    cy.get('input[name="q"]').should('be.visible')
    cy.title().should('contain', 'Google')
  })

  it('should perform a search', () => {
    const searchQuery: string = 'Cypress testing framework'
    cy.get('input[name="q"]').type(searchQuery)
    cy.get('input[name="q"]').type('{enter}')
    cy.url().should('include', 'search')
    cy.get('#search').should('be.visible')
  })

  it('should navigate to search results', () => {
    cy.get('input[name="q"]').type('Selenium WebDriver{enter}')
    cy.get('h3').should('exist')
    cy.get('a[href*="selenium.dev"]').should('exist')
  })
})

