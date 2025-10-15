package com.ezabenco.mcwebtool.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class DashboardWebSocketClient extends WebSocketClient {
    public DashboardWebSocketClient(String serverUri) throws URISyntaxException {
        this(serverUri, Logger.getLogger(DashboardWebSocketClient.class.getName()));
    }

    private final Logger logger;

    // Accept a logger so you use plugin logging
    public DashboardWebSocketClient(String serverUri, Logger logger) throws URISyntaxException {
        super(new URI(serverUri));
        this.logger = logger;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
    logger.info("WebSocket Connected to dashboard.");
    }

    @Override
    public void onMessage(String message) {
    logger.info("Message from server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    logger.info("WebSocket Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
    logger.severe("WebSocket Error: " + ex.getMessage());
    }

    public void sendPlayerData(String jsonData) {
        if (this.isOpen()) {
            this.send(jsonData);
        } else {
            logger.severe("WebSocket is not open. Cannot send player data.");
        }
    }

    // New method for sending chunk data
    public void sendChunkData(String jsonChunk) {
        if (this.isOpen()) {
            this.send(jsonChunk);
        } else {
            logger.severe("WebSocket is not open. Cannot send chunk data.");
        }
    }
}
