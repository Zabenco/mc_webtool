package com.ezabenco.mcwebtool.listeners;

import com.ezabenco.mcwebtool.utils.PlayerUtils;
import com.ezabenco.mcwebtool.websocket.DashboardWebSocketClient;
import com.google.gson.Gson;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerEventListener implements Listener {

    private final DashboardWebSocketClient webSocketClient;
    private final Gson gson = new Gson();

    public PlayerEventListener(DashboardWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome! McWebTool is active.");

        var playerData = new PlayerData(
                player.getName(),
                PlayerUtils.getStatsSummary(player),
                PlayerUtils.getInventoryItemStacks(player),
                PlayerUtils.getActiveEffectKeys(player),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()
        );

        String json = gson.toJson(playerData);
        webSocketClient.sendPlayerData(json);
    }

    public static class PlayerData {
        String name;
        String statsSummary;
        java.util.List<String> inventory;
        java.util.List<String> activeEffects;
        double x, y, z;

        public PlayerData(String name, String statsSummary, java.util.List<String> inventory,
                          java.util.List<String> activeEffects, double x, double y, double z) {
            this.name = name;
            this.statsSummary = statsSummary;
            this.inventory = inventory;
            this.activeEffects = activeEffects;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
