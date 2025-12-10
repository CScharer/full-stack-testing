"""
Locust API Load Test

Tests API performance under realistic load:
- 40% of total performance testing (primary tool)
- Simulates user behavior patterns
- Measures throughput and response times
- Real-time metrics and web UI

Scenario:
- Multiple API endpoints
- CRUD operations
- Realistic wait times
- Dynamic user count

Run:
  locust -f src/test/locust/api_load_test.py

Or headless:
  locust -f src/test/locust/api_load_test.py --headless \
         --users 100 --spawn-rate 10 --run-time 2m
"""

from locust import HttpUser, task, between, events
import json
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class ApiUser(HttpUser):
    """
    Simulates a user interacting with the REST API
    """

    # Wait 1-3 seconds between tasks (realistic user behavior)
    wait_time = between(1, 3)

    # Base URL for all requests
    host = "https://jsonplaceholder.typicode.com"

    def on_start(self):
        """Called when a user starts"""
        logger.info("User started - API Load Test")

    @task(10)  # Weight: 10 (most common)
    def get_all_posts(self):
        """GET /posts - Retrieve all posts"""
        with self.client.get(
            "/posts",
            catch_response=True,
            name="GET /posts"
        ) as response:
            if response.status_code == 200:
                data = response.json()
                if len(data) == 100:
                    response.success()
                else:
                    response.failure(f"Expected 100 posts, got {len(data)}")
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(8)  # Weight: 8
    def get_single_post(self):
        """GET /posts/{id} - Retrieve specific post"""
        post_id = 1
        with self.client.get(
            f"/posts/{post_id}",
            catch_response=True,
            name="GET /posts/{id}"
        ) as response:
            if response.status_code == 200:
                data = response.json()
                if data.get('id') == post_id:
                    response.success()
                else:
                    response.failure(f"Post ID mismatch")
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(5)  # Weight: 5
    def get_comments(self):
        """GET /posts/{id}/comments - Retrieve post comments"""
        with self.client.get(
            "/posts/1/comments",
            catch_response=True,
            name="GET /posts/{id}/comments"
        ) as response:
            if response.status_code == 200:
                data = response.json()
                if len(data) > 0 and data[0].get('postId') == 1:
                    response.success()
                else:
                    response.failure("Invalid comments response")
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(3)  # Weight: 3
    def create_post(self):
        """POST /posts - Create new post"""
        payload = {
            "title": "Locust Test Post",
            "body": "This is a performance test post",
            "userId": 1
        }

        with self.client.post(
            "/posts",
            json=payload,
            catch_response=True,
            name="POST /posts"
        ) as response:
            if response.status_code == 201:
                data = response.json()
                if 'id' in data:
                    response.success()
                else:
                    response.failure("No ID in response")
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(2)  # Weight: 2
    def update_post(self):
        """PUT /posts/{id} - Update post"""
        payload = {
            "id": 1,
            "title": "Updated Title",
            "body": "Updated content",
            "userId": 1
        }

        with self.client.put(
            "/posts/1",
            json=payload,
            catch_response=True,
            name="PUT /posts/{id}"
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(1)  # Weight: 1 (least common)
    def delete_post(self):
        """DELETE /posts/{id} - Delete post"""
        with self.client.delete(
            "/posts/1",
            catch_response=True,
            name="DELETE /posts/{id}"
        ) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Status code: {response.status_code}")

    @task(6)  # Weight: 6
    def get_users(self):
        """GET /users - Retrieve all users"""
        with self.client.get(
            "/users",
            catch_response=True,
            name="GET /users"
        ) as response:
            if response.status_code == 200:
                data = response.json()
                if len(data) == 10:
                    response.success()
                else:
                    response.failure(f"Expected 10 users, got {len(data)}")
            else:
                response.failure(f"Status code: {response.status_code}")


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    """Called when test starts"""
    logger.info("=" * 60)
    logger.info("🔥 LOCUST PERFORMANCE TEST STARTING")
    logger.info("Target: JSONPlaceholder API")
    logger.info("=" * 60)


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """Called when test stops"""
    logger.info("=" * 60)
    logger.info("✅ LOCUST PERFORMANCE TEST COMPLETED")
    logger.info("=" * 60)


# Configuration for headless mode
if __name__ == "__main__":
    import os
    os.system("locust -f api_load_test.py --headless --users 100 --spawn-rate 10 --run-time 2m")
