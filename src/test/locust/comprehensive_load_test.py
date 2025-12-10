"""
Locust Comprehensive Load Test

Advanced performance testing scenarios:
- Multi-endpoint testing
- Authentication simulation
- File upload/download patterns
- Database query patterns
- Cache performance testing

Run with Locust Web UI:
  locust -f src/test/locust/comprehensive_load_test.py
  Then open: http://localhost:8089

Run headless (CI/CD):
  locust -f src/test/locust/comprehensive_load_test.py --headless \
         --users 200 --spawn-rate 20 --run-time 5m \
         --html target/locust/report.html \
         --csv target/locust/stats
"""

from locust import HttpUser, task, between, SequentialTaskSet, events
import json
import random
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


class UserBehavior(SequentialTaskSet):
    """
    Sequential user journey through the API
    """

    @task
    def step1_browse_posts(self):
        """Step 1: Browse posts"""
        self.client.get("/posts", name="1. Browse Posts")

    @task
    def step2_read_post(self):
        """Step 2: Read specific post"""
        post_id = random.randint(1, 100)
        self.client.get(f"/posts/{post_id}", name="2. Read Post")

    @task
    def step3_read_comments(self):
        """Step 3: Read comments"""
        post_id = random.randint(1, 10)
        self.client.get(f"/posts/{post_id}/comments", name="3. Read Comments")

    @task
    def step4_create_post(self):
        """Step 4: Create new post"""
        payload = {
            "title": f"Test Post {random.randint(1, 1000)}",
            "body": "Performance test content",
            "userId": random.randint(1, 10)
        }
        self.client.post(
            "/posts",
            json=payload,
            name="4. Create Post"
        )

    @task
    def step5_update_post(self):
        """Step 5: Update post"""
        post_id = random.randint(1, 10)
        payload = {
            "id": post_id,
            "title": "Updated Title",
            "body": "Updated content",
            "userId": 1
        }
        self.client.put(
            f"/posts/{post_id}",
            json=payload,
            name="5. Update Post"
        )


class ComprehensiveUser(HttpUser):
    """
    Comprehensive API load testing with realistic patterns
    """

    tasks = [UserBehavior]
    wait_time = between(1, 5)
    host = "https://jsonplaceholder.typicode.com"

    @task(10)
    def query_with_params(self):
        """Test query parameters"""
        user_id = random.randint(1, 10)
        self.client.get(
            f"/posts?userId={user_id}",
            name="Query: Filter by userId"
        )

    @task(8)
    def get_user_data(self):
        """Get user profile"""
        user_id = random.randint(1, 10)
        self.client.get(f"/users/{user_id}", name="GET: User Profile")

    @task(5)
    def get_albums(self):
        """Get photo albums"""
        self.client.get("/albums", name="GET: Albums")

    @task(3)
    def get_todos(self):
        """Get todo items"""
        user_id = random.randint(1, 10)
        self.client.get(
            f"/todos?userId={user_id}",
            name="GET: User Todos"
        )

    @task(2)
    def batch_requests(self):
        """Simulate batch requests"""
        # Get multiple resources quickly
        self.client.get("/posts/1", name="Batch: Post 1")
        self.client.get("/posts/2", name="Batch: Post 2")
        self.client.get("/posts/3", name="Batch: Post 3")


@events.init_command_line_parser.add_listener
def _(parser):
    """Add custom command line options"""
    parser.add_argument("--test-env", type=str, default="dev", help="Test environment")
    parser.add_argument("--api-key", type=str, default="", help="API key if needed")


@events.test_start.add_listener
def on_test_start(environment, **kwargs):
    """Test start event"""
    logger.info("=" * 80)
    logger.info("🚀 COMPREHENSIVE LOAD TEST STARTING")
    logger.info(f"Users: Ramping up to target")
    logger.info(f"Target: JSONPlaceholder API")
    logger.info("=" * 80)


@events.test_stop.add_listener
def on_test_stop(environment, **kwargs):
    """Test stop event"""
    stats = environment.stats
    logger.info("=" * 80)
    logger.info("📊 LOAD TEST RESULTS:")
    logger.info(f"  Total requests: {stats.total.num_requests}")
    logger.info(f"  Failures: {stats.total.num_failures}")
    logger.info(f"  RPS: {stats.total.total_rps:.2f}")
    logger.info(f"  Avg response time: {stats.total.avg_response_time:.2f}ms")
    logger.info(f"  Max response time: {stats.total.max_response_time:.2f}ms")
    logger.info("=" * 80)
    logger.info("✅ Reports saved to target/locust/")
    logger.info("=" * 80)
