import { Page, Locator } from '@playwright/test'

/**
 * Page Object Model for Google Search Page.
 */
export class GoogleSearchPage {
  readonly page: Page
  readonly searchInput: Locator
  readonly searchResults: Locator

  constructor(page: Page) {
    this.page = page
    // Google now uses textarea instead of input for search box
    // Use flexible selector that works with both
    this.searchInput = page.locator("textarea[name='q'], input[name='q']")
    this.searchResults = page.locator('#search')
  }

  async navigate(baseURL?: string): Promise<void> {
    const url = baseURL || process.env.BASE_URL || 'https://www.google.com'
    await this.page.goto(url)
  }

  async search(query: string): Promise<void> {
    await this.searchInput.fill(query)
    await this.searchInput.press('Enter')
    await this.page.waitForSelector('#search', { timeout: 10000 })
  }

  async clickFirstResult(): Promise<void> {
    await this.page.locator('h3').first().click()
  }
}

