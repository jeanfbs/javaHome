# controllers/room_controller.py
import random
from datetime import datetime


months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]


class RoomController:

    def handle(self, request):
        room_type = "generic"

        if "?" in request.path:
            params = request.path.split("?")[1]
            for p in params.split("&"):
                if p.startswith("id="):
                    room_type = p.split("=")[1]

        return self.generate_room(room_type)

    def generate_room(self, room_type):
        
        return {
            "name": room_type,
            "totalArea": 9,
            "lightSwitchAmount": random.randint(1,4),
            "doorSize": 0.90,
            "light": {
                "id": f"sensor-{random.randint(1000,9999)}",
                "name": f"Luminária {room_type.capitalize()}",
                "brightness": round(random.uniform(0, 100), 1),
                "state": random.choice([True, False]),
                "lastChanged": datetime.now().isoformat(),
                "historyConsume": [{'time': 'Mar', 'value': 70.59}, {'time': 'Apr', 'value': 61.65}, {'time': 'May', 'value': 2.9}, {'time': 'Jun', 'value': 12.87}, {'time': 'Jul', 'value': 32.81}, {'time': 'Aug', 'value': 28.28}, {'time': 'Sep', 'value': 6.06}, {'time': 'Oct', 'value': 11.93}, {'time': 'Nov', 'value': 10.44}]
            },
            "outlets": self.generate_outlets(room_type),
            "temperature": {
                "id": f"sensor-{random.randint(1000,9999)}",
                "name": f"Temperatura {room_type.capitalize()}",
                "currentTemperature": round(random.uniform(18, 32), 2),
                "relativeHumidity": round(random.uniform(40, 80), 2),
                "historyConsume": [{'time': 'Jul', 'value': 24.04}, {'time': 'Aug', 'value': 25.54}, {'time': 'Sep', 'value': 24.33}, {'time': 'Oct', 'value': 21.99}, {'time': 'Nov', 'value': 27.58}]
            },
            "security" : [
                {
                    "id": "CA-1645-YD789",
                    "name": "Sensor Janela 1",
                    "type": "PRESENCE",
                    "status": True
                },
                {
                    "id": "CA-1645-YD789",
                    "name": "Sensor Janela 2",
                    "type": "MAGNETIC",
                    "status": True
                },
                {
                    "id": "CA-1010-YD777",
                    "name": "Sensor porta 1",
                    "type": "DEFAULT",
                    "status": True
                },
                {
                    "id": "CA-1010-YD777",
                    "name": "Alarme Geral",
                    "type": "DEFAULT",
                    "status": True
                }
            ]
        }

    def generate_series(self, min_size=2, max_size=9):
    
        if min_size < 1 or max_size < min_size:
            raise ValueError("min_size must be >= 1 and max_size must be >= min_size")

        start_index = random.randint(0, 11)
        length = random.randint(min_size, max_size)

        year = datetime.now().year
        month_pointer = start_index

        series = []

        for _ in range(length):

            month = months[month_pointer]
            year_suffix = str(year)[-2:]

            series.append({
                "time": f"{month}",
                "value": round(random.uniform(1, 100), 2)
            })

            month_pointer += 1
            if month_pointer == 12:
                month_pointer = 0
                year += 1

        return series

    def generate_outlets(self, room_type, min_size=1, max_size=5):

        if min_size < 1 or max_size < min_size:
            raise ValueError("min_size must be >= 1 and max_size >= min_size")

        size = random.randint(min_size, max_size)
        outlets = []

        for i in range(1, size + 1):
            inUse = random.choice([True, False])
            
            outlet = {
                "id": f"sensor-{random.randint(1000,9999)}",
                "name": f"Tomada {room_type.capitalize()} {i}",
                "inUse": random.choice([True, False]),
                "type": random.choice(["SINGLE", "DOUBLE"]),
                "powerUsage": round(random.uniform(5, 25), 2) if inUse else 0,
                "historyConsume": self.generate_series()
            }
            outlets.append(outlet)
        return outlets