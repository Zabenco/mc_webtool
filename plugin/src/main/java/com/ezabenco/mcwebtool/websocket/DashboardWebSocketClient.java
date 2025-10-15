package com.ezabenco.mcwebtool.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class DashboardWebSocketClient extends WebSocketClient {

    public DashboardWebSocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket Connected to dashboard.");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket Error:");
        ex.printStackTrace();
    }

    public void sendPlayerData(String jsonData) {
        if (this.isOpen()) {
            this.send(jsonData);
        } else {
            System.err.println("WebSocket is not open. Cannot send player data.");
        }
    }
}
