# controllers/home_controller.py
import random


class HomeController:

    def handle(self, request):
        random_name = random.choice(["Solar", "Vila Verde", "Monte Azul", "Jardim Imperial"])

        return {
            "name": f"Casa Residencial",
            "totalArea": round(random.uniform(75, 350), 2),
            "address": "Rua das Flores, 123, Centro",
            "coordinates": "-23.5505, -46.6333",
            "status": True,
            "roomAmount": random.randint(1, 5)
        }