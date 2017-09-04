package me.falsehonesty.airbattle.players;

import me.falsehonesty.airbattle.kits.Kit;
import me.falsehonesty.airbattle.teams.Team;
import me.falsehonesty.airbattle.util.AirbattleConfig;
import me.falsehonesty.airbattle.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    public HashMap<UUID, AirbattlePlayer> playerMap;

    public PlayerManager() {
        playerMap = new HashMap<>();
    }

    public void onPlayerJoin(Player p) {

        Team teamToJoin = Bukkit.getOnlinePlayers().size() % 2 == 0 ? Team.BLUE : Team.RED;

        playerMap.put(p.getUniqueId(), new AirbattlePlayer(Kit.valueOf(AirbattleConfig.defaultKit), p, teamToJoin));
        teamToJoin.addPlayer(playerMap.get(p.getUniqueId()));
        p.getInventory().setItem(0, new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.WHITE + "Kit Selector").build());
        p.getInventory().setItem(1, new ItemBuilder(Material.PAPER).setName(ChatColor.WHITE + "Team Selector").build());
    }

    public AirbattlePlayer getABPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }
}
