package me.falsehonesty.airbattle.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

@UtilityClass
public class MathHelper {
    public static Integer floorByNum(Integer pre, Integer floorBy) {
        return (int) (Math.floor(pre / floorBy) * floorBy);
    }

    public static ArrayList<Block> getBlocksInBox(Location min, Location max, Material type, Location ignore) {
        ArrayList<Block> blocks = new ArrayList<>();

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location currLoc = new Location(min.getWorld(), x, y, z);

                    if (currLoc.getWorld().getBlockAt(currLoc).getType() == Material.WOOL && !currLoc.equals(ignore)) {
                        blocks.add(currLoc.getBlock());
                    }
                }
            }
        }

        return blocks;
    }
}