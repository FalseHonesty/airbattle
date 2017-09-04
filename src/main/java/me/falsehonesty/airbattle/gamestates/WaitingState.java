package me.falsehonesty.airbattle.gamestates;

import me.falsehonesty.airbattle.Airbattle;
import me.falsehonesty.airbattle.game.Game;
import me.falsehonesty.airbattle.kits.Kit;
import me.falsehonesty.airbattle.kits.KitMenu;
import me.falsehonesty.airbattle.players.AirbattlePlayer;
import me.falsehonesty.airbattle.players.PlayerManager;
import me.falsehonesty.airbattle.teams.TeamMenu;
import me.falsehonesty.airbattle.util.AirbattleConfig;
import me.falsehonesty.airbattle.util.NMSHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class WaitingState extends Gamestate {

    @Getter
    private PlayerManager playerManager;
    private KitMenu kitMenu;
    private TeamMenu teamMenu;
    private Game game;
    private int countdown = -1;
    private int countdownTask;

    public void onStart(Game game) {
        this.game = game;

        kitMenu = new KitMenu(this);
        teamMenu = new TeamMenu(this);

        playerManager = game.playerManager;

        kitMenu.registerKit(Kit.SOLDIERKIT.symbol, Kit.SOLDIERKIT, "soldier");

        NMSHelper.setMOTD(ChatColor.GREEN + "Airbattle " + ChatColor.WHITE + "- " + ChatColor.YELLOW + "Waiting");

        for (Player p : Bukkit.getOnlinePlayers()) {
            initPlayer(p);
        }
    }

    public void onStop() {
        if (countdownTask >= 0) {
            Bukkit.getScheduler().cancelTask(countdownTask);
        }

        HandlerList.unregisterAll(this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
        }
    }

    @Override
    public Gamestates getGamestateType() {
        return Gamestates.WAITING;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(ChatColor.GREEN + e.getPlayer().getName() + " has joined.");

        initPlayer(e.getPlayer());
    }

    private void initPlayer(Player p) {
        p.getInventory().clear();
        playerManager.onPlayerJoin(p);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.teleport(AirbattleConfig.lobbyLoc);

        if (Bukkit.getOnlinePlayers().size() >= AirbattleConfig.minPlayers) {
            if (countdown < 0) {
                countdown = AirbattleConfig.waitTimer;
                countdownTask = Bukkit.getScheduler().runTaskTimer(Airbattle.plugin, this::countdown, 0, 20).getTaskId();
            }
        }
    }

    private void countdown() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            NMSHelper.sendActionbarMessage(ChatColor.YELLOW + "Game starting in " + ChatColor.GREEN + countdown + ChatColor.YELLOW + " seconds.", p);

            if (countdown % 15 == 0 || countdown <= 5) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            }
        }

        if (countdown == 0) {
            Bukkit.getScheduler().cancelTask(countdownTask);
            countdownTask = -1;
            game.getGamestateManager().changeGamestate(new PlayState());
        }

        countdown--;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.CONTAINER || e.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            if (e.getClickedInventory().getName().equalsIgnoreCase("Kit Menu")) {
                kitMenu.onClick(e);
            } else if (e.getClickedInventory().getName().equalsIgnoreCase("Team Menu")) {
                teamMenu.onClick(e);
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }

        if (e.getItem().getType() == Material.NETHER_STAR) {
            kitMenu.openInventory(e.getPlayer());
        } else if (e.getItem().getType() == Material.PAPER) {
            teamMenu.openInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.RED + e.getPlayer().getName() + " has left.");
        AirbattlePlayer ab = playerManager.playerMap.remove(e.getPlayer().getUniqueId());
        ab.getSelectedTeam().removePlayer(ab);

        if (Bukkit.getOnlinePlayers().size() < AirbattleConfig.maxTeamSize * 2 && countdown > 0) {
            countdown = -1;
            Bukkit.getScheduler().cancelTask(countdownTask);

            for (Player p : Bukkit.getOnlinePlayers()) {
                NMSHelper.sendActionbarMessage(ChatColor.RED + "Countdown stopped. Not enough players.", p);
            }
        }
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLoss(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
