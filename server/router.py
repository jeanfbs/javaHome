# router.py
from controllers.home_controller import HomeController
from controllers.room_controller import RoomController


class Router:

    def __init__(self):
        self.routes = {
            "/home": HomeController(),
            "/room": RoomController()
        }

    def dispatch(self, request):
        path = request.path.split("?")[0]

        controller = self.routes.get(path)
        if not controller:
            return {"error": "Endpoint not found"}

        return controller.handle(request)