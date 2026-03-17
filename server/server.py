# server.py
import asyncio
import websockets
from protocol import parse_request, HomeSocketResponse
from router import Router


router = Router()


async def handle_connection(websocket):
    print("Client connected")

    async for message in websocket:
        request = parse_request(message)

        print("Received request:", request.path)

        response_body = router.dispatch(request)

        response = HomeSocketResponse(
            method=request.method,
            path=request.path,
            version=request.version,
            headers={
                "Correlation-Id": request.headers.get("Correlation-Id"),
                "Origin": "server"
            },
            body=response_body
        )

        await websocket.send(response.to_message())


async def main():
    async with websockets.serve(handle_connection, "localhost", 8765):
        print("HOMESOCKET Server running on ws://localhost:8765")
        await asyncio.Future()


if __name__ == "__main__":
    asyncio.run(main())