package me.falsehonesty.airbattle.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@UtilityClass
public class NMSHelper {
    private static HashMap<String, BossBattleServer> bossbars = new HashMap<>();

    public static void sendActionbarMessage(String chat, Player player) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("text", chat);
        String jsonStr = jsonObj.toJSONString();

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(jsonStr), ChatMessageType.GAME_INFO)
        );
    }

    public static void sendDragonbarMessage(String key, Player player) {
        if (bossbars.containsKey(key)) {
            BossBattleServer bossbar = bossbars.get(key);
            CraftPlayer cp = ((CraftPlayer) player);
            bossbar.addPlayer(cp.getHandle());
        }
    }

    public static void removeDragonbarMessage(String key, Player player) {
        if (bossbars.containsKey(key)) {
            BossBattleServer bossbar = bossbars.get(key);
            CraftPlayer cp = ((CraftPlayer) player);
            bossbar.removePlayer(cp.getHandle());
        }
    }

    public static void setDragonbarMessage(String key, IChatBaseComponent chat, BossBattle.BarColor color, BossBattle.BarStyle style) {
        bossbars.put(key, new BossBattleServer(chat, color, style));
    }

    public static void setServerPlayers(Integer max) {
        try {
            Field pListF = Bukkit.getServer().getClass().getDeclaredField("playerList");
            pListF.setAccessible(true);
            Field maxPlayersF = pListF.get(Bukkit.getServer()).getClass().getSuperclass().getDeclaredField("maxPlayers");
            maxPlayersF.setAccessible(true);
            maxPlayersF.setInt(pListF.get(Bukkit.getServer()), max);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setMOTD(String message) {
        try {
            Field consoleF = Bukkit.getServer().getClass().getDeclaredField("console");
            consoleF.setAccessible(true);
            MinecraftServer server = (MinecraftServer) consoleF.get(Bukkit.getServer());
            server.setMotd(message);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Entity getNearestEntityInSight(Player player, int range) {
        List<Entity> entities = player.getNearbyEntities(range, range, range);
        entities.removeIf(next -> !(next instanceof LivingEntity) || next == player);

        List<Block> sight = player.getLineOfSight(null, range);

        for (Block block : sight) {
            if (block.getType() != Material.AIR) {
                break;
            }

            Location low = block.getLocation();
            Location high = low.clone().add(1, 1, 1);

            AxisAlignedBB blockBoundingBox = new AxisAlignedBB(low.getX(), low.getY(), low.getZ(), high.getX(), high.getY(), high.getZ()); //The bounding or collision box of the block

            for (Entity entity : entities) {
                if (entity.getLocation().distance(player.getEyeLocation()) <= range && ((CraftEntity) entity).getHandle().getBoundingBox().c(blockBoundingBox)) {
                    return entity;
                }
            }
        }
        return null; //Return null/nothing if no entity was found
    }
}
