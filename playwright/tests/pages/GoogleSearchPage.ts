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
    this.searchInput = page.locator("input[name='q']")
    this.searchResults = page.locator('#search')
  }

  async navigate(): Promise<void> {
    await this.page.goto('https://www.google.com')
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

