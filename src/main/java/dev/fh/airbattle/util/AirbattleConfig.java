package dev.fh.airbattle.util;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.capturepoints.CapturePoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AirbattleConfig {
    public static String defaultKit;
    public static ArrayList<CapturePoint> capturePoints;
    public static Integer capturePointRange;
    public static Integer winCondition;
    public static Integer maxTeamSize;
    public static Integer minPlayers;
    public static Integer waitTimer;
    public static Location lobbyLoc;
    public static Location blueTeamLoc;
    public static Location redTeamLoc;
    public static Location spectatorLoc;
    public static Integer capturePerTick;
    public static Boolean pluginEnabled;
    public static Integer pointsPerTick;
    public static HashMap<Integer, ItemStack> soldierItems;
    public static Integer respawnTimer;

    static {
        soldierItems = new HashMap<>();
        soldierItems.put(0, new ItemBuilder(Material.STONE_HOE).setUnbreakable(true).build());
        soldierItems.put(1, new ItemBuilder(Material.STONE_SPADE).setUnbreakable(true).build());
    }

    public static HashMap<String, Object> getConfigDefaults() {
        HashMap<String, Object> configDefaults = new HashMap<>();

        configDefaults.put("defaultKit", "SOLDIERKIT");
        configDefaults.put("capturePoints", new ArrayList<>());
        configDefaults.put("capturePointRange", 5);
        configDefaults.put("winCondition", 200);
        configDefaults.put("maxTeamSize", 8);
        configDefaults.put("minPlayers", 2);
        configDefaults.put("waitTimer", 60);
        configDefaults.put("lobbyLoc", new Location(Bukkit.getWorld("world"), 0, 60, 0));
        configDefaults.put("blueTeamLoc", new Location(Bukkit.getWorld("world"), 0, 60, 0));
        configDefaults.put("redTeamLoc", new Location(Bukkit.getWorld("world"), 0, 60, 0));
        configDefaults.put("spectatorLoc", new Location(Bukkit.getWorld("world"), 0, 60, 0));
        configDefaults.put("capturePerTick", 1);
        configDefaults.put("pluginEnabled", true);
        configDefaults.put("pointsPerTick", 1);
        configDefaults.put("respawnTimer", 10);

        return configDefaults;
    }

    public static void loadConfig(FileConfiguration conf) {
        if (conf.get("defaultKit") != null) {
            defaultKit = conf.getString("defaultKit");
        } else {
            defaultKit = (String) getConfigDefaults().get("defaultKit");
        }

        if (conf.get("capturePoints") != null) {
            capturePoints = (ArrayList<CapturePoint>) conf.get("capturePoints");
        } else {
            capturePoints = (ArrayList<CapturePoint>) getConfigDefaults().get("capturePoints");
        }

        if (conf.get("capturePointRange") != null) {
            capturePointRange = conf.getInt("capturePointRange");
        } else {
            capturePointRange = (Integer) getConfigDefaults().get("capturePointRange");
        }

        if (conf.get("winCondition") != null) {
            winCondition = conf.getInt("winCondition");
        } else {
            winCondition = (Integer) getConfigDefaults().get("winCondition");
        }

        if (conf.get("maxTeamSize") != null) {
            maxTeamSize = conf.getInt("maxTeamSize");
        } else {
            maxTeamSize = (Integer) getConfigDefaults().get("maxTeamSize");
        }

        if (conf.get("minPlayers") != null) {
            minPlayers = conf.getInt("minPlayers");
        } else {
            minPlayers = (Integer) getConfigDefaults().get("minPlayers");
        }

        if (conf.get("waitTimer") != null) {
            waitTimer = conf.getInt("waitTimer");
        } else {
            waitTimer = (Integer) getConfigDefaults().get("waitTimer");
        }

        if (conf.get("lobbyLoc") != null) {
            lobbyLoc = (Location) conf.get("lobbyLoc");
        } else {
            lobbyLoc = (Location) getConfigDefaults().get("lobbyLoc");
        }

        if (conf.get("redTeamLoc") != null) {
            redTeamLoc = (Location) conf.get("redTeamLoc");
        } else {
            redTeamLoc = (Location) getConfigDefaults().get("redTeamLoc");
        }

        if (conf.get("blueTeamLoc") != null) {
            blueTeamLoc = (Location) conf.get("blueTeamLoc");
        } else {
            blueTeamLoc = (Location) getConfigDefaults().get("blueTeamLoc");
        }

        if (conf.get("spectatorLoc") != null) {
            spectatorLoc = (Location) conf.get("spectatorLoc");
        } else {
            spectatorLoc = (Location) getConfigDefaults().get("spectatorLoc");
        }

        if (conf.get("capturePerTick") != null) {
            capturePerTick = conf.getInt("capturePerTick");
        } else {
            capturePerTick = (Integer) getConfigDefaults().get("capturePerTick");
        }

        if (conf.get("pluginEnabled") != null) {
            pluginEnabled = conf.getBoolean("pluginEnabled");
        } else {
            pluginEnabled = (Boolean) getConfigDefaults().get("pluginEnabled");
        }

        if (conf.get("pointsPerTick") != null) {
            pointsPerTick = conf.getInt("pointsPerTick");
        } else {
            pointsPerTick = (Integer) getConfigDefaults().get("pointsPerTick");
        }

        if (conf.get("respawnTimer") != null) {
            respawnTimer = conf.getInt("respawnTimer");
        } else {
            respawnTimer = (Integer) getConfigDefaults().get("respawnTimer");
        }
    }

    public static void saveConfig() {
        FileConfiguration conf = Airbattle.plugin.getConfig();
        conf.set("capturePoints", capturePoints);
        Airbattle.plugin.saveConfig();
    }

    public static void setConfigOption(String key, Object value) {
        Airbattle.plugin.getConfig().set(key, value);
        Airbattle.plugin.saveConfig();
    }
}
