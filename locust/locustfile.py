from locust import HttpUser, task

class CounterUser(HttpUser):
    # @task
    # def incrementCounter(self):
    #     self.client.post("/counter")

    @task
    def getBooks(self):
        self.client.get("/books")
