package dev.fh.airbattle.capturepoints;

import dev.fh.airbattle.Airbattle;
import dev.fh.airbattle.teams.Team;
import dev.fh.airbattle.util.AirbattleConfig;
import dev.fh.airbattle.util.MathHelper;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CapturePoint implements ConfigurationSerializable {
    @Getter
    private final Location location;
    private final Integer capturePerTick;
    private final Integer pointsPerTick;
    private Integer redCapturePercentage;
    private Integer blueCapturePercentage;

    public CapturePoint(Location location) {
        this.location = location;
        this.capturePerTick = AirbattleConfig.capturePerTick;
        this.redCapturePercentage = 0;
        this.blueCapturePercentage = 0;
        this.pointsPerTick = AirbattleConfig.pointsPerTick;

        Bukkit.getScheduler().runTaskLater(Airbattle.plugin, () -> calculateWool(false), 20 * 3);
    }

    public boolean isPlayerInRange(Player p) {
        int range = AirbattleConfig.capturePointRange;
        Location loc1 = location.clone().add(range, range, range);
        Location loc2 = location.clone().subtract(range, range, range);

        double px = p.getLocation().getX();
        double py = p.getLocation().getY();
        double pz = p.getLocation().getZ();

        return loc1.getX() >= px && px >= loc2.getX()
                && loc1.getY() >= py && py >= loc2.getY()
                && loc1.getZ() >= pz && pz >= loc2.getZ();

    }

    public void calculatePoints() {
        if (redCapturePercentage == 100) {
            Team.RED.addPoints(pointsPerTick);
        } else if (blueCapturePercentage == 100) {
            Team.BLUE.addPoints(pointsPerTick);
        }
    }

    public void capturing(int red, int blue) {

        if (red == blue) {
            return;
        }

        if (red > blue) {
            int difference = red - blue;
            int additive = capturePerTick * difference;

            if (blueCapturePercentage > 0) {
                int pre = MathHelper.floorByNum(blueCapturePercentage, 20);
                blueCapturePercentage -= additive;

                if (blueCapturePercentage <= 0) {
                    blueCapturePercentage = 0;

                    setBlockColor(location.getBlock(), DyeColor.WHITE);
                }

                if (MathHelper.floorByNum(blueCapturePercentage, 20) != pre) {
                    calculateWool(true);
                }
            } else if (redCapturePercentage < 100) {
                int pre = MathHelper.floorByNum(redCapturePercentage, 20);
                redCapturePercentage += additive;

                if (redCapturePercentage >= 100) {
                    redCapturePercentage = 100;

                    setBlockColor(location.getBlock(), DyeColor.RED);
                }

                if (MathHelper.floorByNum(redCapturePercentage, 20) != pre) {
                    calculateWool(true);
                }
            }

        } else {
            int difference = blue - red;
            System.out.println("cpt: " + capturePerTick);
            int additive = capturePerTick * difference;

            if (redCapturePercentage > 0) {
                int pre = MathHelper.floorByNum(redCapturePercentage, 20);
                redCapturePercentage -= additive;

                if (redCapturePercentage <= 0) {
                    redCapturePercentage = 0;

                    setBlockColor(location.getBlock(), DyeColor.WHITE);
                }

                if (MathHelper.floorByNum(redCapturePercentage, 20) != pre) {
                    calculateWool(true);
                }
            } else if (blueCapturePercentage < 100) {
                int pre = MathHelper.floorByNum(blueCapturePercentage, 20);
                blueCapturePercentage = blueCapturePercentage + additive;

                if (blueCapturePercentage >= 100) {
                    blueCapturePercentage = 100;

                    setBlockColor(location.getBlock(), DyeColor.BLUE);
                }

                if (MathHelper.floorByNum(blueCapturePercentage, 20) != pre) {
                    calculateWool(true);
                }
            }
        }
    }

    public void noneCapturing() {
        if (redCapturePercentage < 100 && redCapturePercentage > 0) {
            int pre = MathHelper.floorByNum(redCapturePercentage, 20);
            redCapturePercentage -= capturePerTick;

            if (pre != MathHelper.floorByNum(redCapturePercentage, 20)) calculateWool(true);
        } else if (blueCapturePercentage < 100 && blueCapturePercentage > 0) {
            int pre = MathHelper.floorByNum(blueCapturePercentage, 20);
            blueCapturePercentage -= capturePerTick;

            if (pre != MathHelper.floorByNum(blueCapturePercentage, 20)) calculateWool(true);
        }
    }

    private void calculateWool(boolean sound) {
        DyeColor dyeColor = redCapturePercentage > 0 ? DyeColor.RED : DyeColor.BLUE;

        for (Block woolBlock : getColoredBlocks()) {
            setBlockColor(woolBlock, dyeColor);
        }

        for (Block woolBlock : getUncoloredBlocks()) {
            setBlockColor(woolBlock, DyeColor.WHITE);
        }

        if (redCapturePercentage == 100) setBlockColor(location.getBlock(), dyeColor);
        if (blueCapturePercentage == 100) setBlockColor(location.getBlock(), dyeColor);
        if (redCapturePercentage == 0 && blueCapturePercentage == 0) setBlockColor(location.getBlock(), DyeColor.WHITE);

        if (sound) {
            location.getWorld().playSound(location, Sound.BLOCK_CLOTH_BREAK, 7, 1);
        }
    }

    private ArrayList<Block> getColoredBlocks() {
        Integer range = AirbattleConfig.capturePointRange;

        Integer yToGo = getYAmount();

        if (yToGo == 0) return new ArrayList<>();

        Location min = location.clone().subtract(range, range, range);
        Location max = location.clone().add(range, yToGo - range, range);

        return MathHelper.getBlocksInBox(min, max, Material.WOOL, location);
    }

    private ArrayList<Block> getUncoloredBlocks() {
        Integer range = AirbattleConfig.capturePointRange;

        Integer yToGo = getYAmount();

        Location min = location.clone().subtract(range, range - yToGo, range);
        Location max = location.clone().add(range, 0, range);

        return MathHelper.getBlocksInBox(min, max, Material.WOOL, location);
    }

    private Integer getYAmount() {
        if (redCapturePercentage >= 20) {
            return MathHelper.floorByNum(redCapturePercentage, 20) / 20;
        } else if (blueCapturePercentage >= 20) {
            return MathHelper.floorByNum(blueCapturePercentage, 20) / 20;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    private void setBlockColor(Block block, DyeColor color) {
        if (block.getType() != Material.WOOL) {
            return;
        }

        if (color == DyeColor.RED) block.setData((byte) 14);
        else if (color == DyeColor.BLUE) block.setData((byte) 11);
        else if (color == DyeColor.WHITE) block.setData((byte) 0);
    }

    @Override
    public String toString() {
        return "CapturePoint[" + location + "," + "RedCapture[" + redCapturePercentage + "],BlueCapture[" + blueCapturePercentage + "]]";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("world", location.getWorld().getName());
        data.put("x", location.getX());
        data.put("y", location.getY());
        data.put("z", location.getZ());
        data.put("yaw", location.getYaw());
        data.put("pitch", location.getPitch());
        return data;
    }

    public static CapturePoint deserialize(Map<String, Object> args) {
        Location location = new Location(Bukkit.getWorld((String) args.get("world")), NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
        return new CapturePoint(location);
    }
}
