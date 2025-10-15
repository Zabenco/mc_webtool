package com.ezabenco.mcwebtool;

import com.ezabenco.mcwebtool.listeners.PlayerEventListener;
import com.ezabenco.mcwebtool.commands.StrikeCommand;
import com.ezabenco.mcwebtool.commands.TeleportCommand;
import com.ezabenco.mcwebtool.commands.KillCommand;
import com.ezabenco.mcwebtool.commands.PlayerStatsCommand;
import com.ezabenco.mcwebtool.commands.BuffCommand;
import com.ezabenco.mcwebtool.websocket.DashboardWebSocketClient;
import org.bukkit.plugin.java.JavaPlugin;

public class McWebToolPlugin extends JavaPlugin {

    private DashboardWebSocketClient webSocketClient;

    @Override
    public void onEnable() {
        getLogger().info("McWebToolPlugin enabled");


        try {
            String serverUri = "ws://localhost:8080";
            webSocketClient = new DashboardWebSocketClient(serverUri);
            webSocketClient.connect();
        } catch (Exception e) {
            getLogger().severe("Failed to connect WebSocket: " + e.getMessage());
        }

        getServer().getPluginManager().registerEvents(new PlayerEventListener(webSocketClient), this);

        this.getCommand("strike").setExecutor(new StrikeCommand());
        this.getCommand("teleport").setExecutor(new TeleportCommand());
        this.getCommand("kill").setExecutor(new KillCommand());
        this.getCommand("buff").setExecutor(new BuffCommand());
        this.getCommand("playerstats").setExecutor(new PlayerStatsCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("McWebToolPlugin disabled");
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
    }
}
