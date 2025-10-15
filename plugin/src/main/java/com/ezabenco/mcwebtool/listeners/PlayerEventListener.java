package com.ezabenco.mcwebtool.listeners;

import com.ezabenco.mcwebtool.utils.PlayerUtils;
import com.ezabenco.mcwebtool.websocket.DashboardWebSocketClient;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerEventListener implements Listener {

    private final DashboardWebSocketClient webSocketClient;
    private final Gson gson = new Gson();

    // Cooldown in milliseconds (default: 200ms)
    public static void setUpdateCooldownMs(long ms) {
        updateCooldownMs = ms;
    }
    private static long updateCooldownMs = 200L;
    private final Map<String, Long> lastUpdate = new ConcurrentHashMap<>();

    // Cache spawn location on enable for consistency
    private final SpawnLocation spawn;

    public PlayerEventListener(DashboardWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
        // Default to first loaded world
        World world = Bukkit.getServer().getWorlds().get(0);
        Location spawnLoc = world.getSpawnLocation();
        this.spawn = new SpawnLocation(spawnLoc.getX(), spawnLoc.getZ());
    }

    // Utility method to send player data to WebSocket
    private void sendPlayerUpdateToWebSocket(Player player) {
        long now = System.currentTimeMillis();
        String key = player.getUniqueId().toString();
        Long last = lastUpdate.get(key);

        if (last == null || (now - last) > updateCooldownMs) {
            // Only send if cooldown passed
            var playerData = new PlayerData(
                player.getName(),
                PlayerUtils.getStatsSummary(player),
                PlayerUtils.getInventoryItemStacks(player),
                PlayerUtils.getActiveEffectKeys(player),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                spawn
            );
            String json = gson.toJson(playerData);
            webSocketClient.sendPlayerData(json);
            lastUpdate.put(key, now);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome! McWebTool is active.");
        sendPlayerUpdateToWebSocket(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        sendPlayerUpdateToWebSocket(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            sendPlayerUpdateToWebSocket(player);
        }
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        sendPlayerUpdateToWebSocket(player);
    }

    public static class PlayerData {
        String name;
        String statsSummary;
        java.util.List<String> inventory;
        java.util.List<String> activeEffects;
        double x, y, z;
        SpawnLocation spawn;

        public PlayerData(String name, String statsSummary, java.util.List<String> inventory,
                          java.util.List<String> activeEffects, double x, double y, double z,
                          SpawnLocation spawn) {
            this.name = name;
            this.statsSummary = statsSummary;
            this.inventory = inventory;
            this.activeEffects = activeEffects;
            this.x = x;
            this.y = y;
            this.z = z;
            this.spawn = spawn;
        }
    }

    // Small nested class for spawn info
    public static class SpawnLocation {
        double x, z;

        public SpawnLocation(double x, double z) {
            this.x = x;
            this.z = z;
        }
    }
}
