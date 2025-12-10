"""
Locust Web Load Test

Tests website performance under concurrent user load:
- Simulates real browser behavior
- Multiple page navigation patterns
- Measures page load times
- Tests different websites

Scenario:
- Users navigate through multiple sites
- Random wait times between pages
- Realistic browsing patterns

Run:
  locust -f src/test/locust/web_load_test.py

Or headless:
  locust -f src/test/locust/web_load_test.py --headless \
         --users 50 --spawn-rate 5 --run-time 3m
"""

from locust import HttpUser, task, between, events
import random
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class WebsiteUser(HttpUser):
    """
    Simulates a user browsing various websites
    """

    # Wait 2-5 seconds between tasks (realistic browsing)
    wait_time = between(2, 5)

    # No single host - we'll test multiple sites
    host = ""

    def on_start(self):
        """Called when a user starts"""
        logger.info("User started - Web Load Test")

    @task(3)  # Weight: 3 (most common site)
    def browse_google(self):
        """Visit Google homepage"""
        with self.client.get(
            "https://www.google.com",
            catch_response=True,
            name="Google Homepage"
        ) as response:
            if response.status_code == 200 and "Google" in response.text:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")

    @task(3)  # Weight: 3
    def browse_github(self):
        """Visit GitHub homepage"""
        with self.client.get(
            "https://github.com",
            catch_response=True,
            name="GitHub Homepage"
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")

    @task(2)  # Weight: 2
    def browse_wikipedia(self):
        """Visit Wikipedia homepage"""
        with self.client.get(
            "https://www.wikipedia.org",
            catch_response=True,
            name="Wikipedia Homepage"
        ) as response:
            if response.status_code == 200 and "Wikipedia" in response.text:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")

    @task(2)  # Weight: 2
    def browse_w3c(self):
        """Visit W3C homepage"""
        with self.client.get(
            "https://www.w3.org",
            catch_response=True,
            name="W3C Homepage"
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status: {response.status_code}")


class ApiAndWebUser(HttpUser):
    """
    Simulates mixed API and web browsing
    """

    wait_time = between(1, 4)
    host = "https://jsonplaceholder.typicode.com"

    @task(5)  # Weight: 5 - API calls
    def api_posts(self):
        """API: Get posts"""
        self.client.get("/posts", name="API: GET posts")

    @task(2)  # Weight: 2 - Web browsing
    def web_google(self):
        """Web: Visit Google"""
        self.client.get("https://www.google.com", name="Web: Google")

    @task(3)  # Weight: 3 - API calls
    def api_users(self):
        """API: Get users"""
        self.client.get("/users", name="API: GET users")


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    """Called when test starts"""
    logger.info("=" * 60)
    logger.info("🌐 LOCUST WEB LOAD TEST STARTING")
    logger.info("Target: Multiple websites")
    logger.info("=" * 60)


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """Called when test stops"""
    logger.info("=" * 60)
    logger.info("✅ LOCUST WEB LOAD TEST COMPLETED")
    logger.info("View report at: http://localhost:8089")
    logger.info("=" * 60)


if __name__ == "__main__":
    import os
    os.system("locust -f web_load_test.py --headless --users 50 --spawn-rate 5 --run-time 3m")
