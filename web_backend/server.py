import asyncio
import websockets
import json

connected = set()

async def handler(websocket):
    print("Client connected")
    connected.add(websocket)
    try:
        async for message in websocket:
            print("Received:", message)
            # Broadcast the received message to all clients except the sender (optional: or include the sender)
            for ws in connected:
                if ws != websocket:  # Don't send back to the sender, or remove this check to send to all
                    await ws.send(message)
    except websockets.ConnectionClosed:
        print("Client disconnected")
    finally:
        connected.remove(websocket)

async def main():
    server = await websockets.serve(handler, "localhost", 8080)
    print("WebSocket server running on ws://localhost:8080")
    await server.wait_closed()

if __name__ == "__main__":
    asyncio.run(main())
