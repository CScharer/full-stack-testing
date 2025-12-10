import { test, expect } from '@playwright/test'
import { GoogleSearchPage } from './pages/GoogleSearchPage'

test.describe('Google Search Tests', () => {
  test.beforeEach(async ({ page }) => {
    const googlePage = new GoogleSearchPage(page)
    await googlePage.navigate()
  })

  test('should display Google homepage', async ({ page }) => {
    const googlePage = new GoogleSearchPage(page)
    await expect(googlePage.searchInput).toBeVisible()
    await expect(page).toHaveTitle(/Google/)
  })

  test('should perform a search', async ({ page }) => {
    const googlePage = new GoogleSearchPage(page)
    await googlePage.search('Playwright testing framework')
    await expect(googlePage.searchResults).toBeVisible()
    await expect(page.url()).toContain('search')
  })

  test('should navigate to search results', async ({ page }) => {
    const googlePage = new GoogleSearchPage(page)
    await googlePage.search('Selenium WebDriver')
    await expect(googlePage.searchResults).toBeVisible()
    await expect(page.locator('h3')).toHaveCount(10)
    
    // Click first result
    await googlePage.clickFirstResult()
    
    // Verify navigation occurred
    await expect(page.url()).not.toContain('google.com')
  })
})

