package com.modnmetl.virtualrealty.util.data;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import com.modnmetl.virtualrealty.VirtualRealty;
import com.modnmetl.virtualrealty.manager.PlotManager;
import com.modnmetl.virtualrealty.model.plot.Plot;

import lombok.SneakyThrows;

@Deprecated
public class OldSchematicUtil {

	public static final String OLD_REGION_PREFIX = "plot";
	public static final String OLD_REGION_SUFFIX = ".region";

	public static void paste(int plotID, Location l) {
		long time = System.currentTimeMillis();
		String[] blocks = load(plotID);
		if (blocks == null)
			return;
		Plot plot = PlotManager.getInstance().getPlot(plotID);
		Location location = new Location(plot.getCreatedWorld(), plot.getBorderBottomLeftCorner().getBlockX(),
				plot.getBorderBottomLeftCorner().getBlockY(), plot.getBorderBottomLeftCorner().getBlockZ());
		Location location2 = new Location(plot.getCreatedWorld(), plot.getBorderTopRightCorner().getBlockX(),
				plot.getBorderTopRightCorner().getBlockY(), plot.getBorderTopRightCorner().getBlockZ());
		Block pos1Block = location.getBlock();
		Block pos2Block = location2.getBlock();
		int minX = Math.min(pos1Block.getX(), pos2Block.getX());
		int minZ = Math.min(pos1Block.getZ(), pos2Block.getZ());
		int minY = Math.min(pos1Block.getY(), pos2Block.getY());
		int maxX = Math.max(pos1Block.getX(), pos2Block.getX());
		int maxZ = Math.max(pos1Block.getZ(), pos2Block.getZ());
		int maxY = Math.max(pos1Block.getY(), pos2Block.getY());
		for (int x = minX; x <= maxX; x++) {
			for (int y = maxY; y > minY; y--) {
				for (int z = minZ; z <= maxZ; z++) {
					Block b = location.getWorld().getBlockAt(x, y, z);
					b.setType(Material.AIR);
				}
			}
		}
		if (blocks[0].isEmpty())
			return;
		for (String block : blocks) {
			String[] cords = block.split(";");
			int x = Integer.parseInt(cords[0]);
			int y = Integer.parseInt(cords[1]);
			int z = Integer.parseInt(cords[2]);
			Location displaced = l.clone();
			displaced.add(x, y, z);
			Block b = displaced.getBlock();

			BlockData blockData = Bukkit.createBlockData("minecraft:" + cords[3]);
			b.setBlockData(blockData);

			b.getState().update(true);
		}
		VirtualRealty.debug("Pasted in: " + (System.currentTimeMillis() - time) + " ms");
	}

	@SneakyThrows
	private static String[] load(int plotID) {
		long time = System.currentTimeMillis();
		File region = new File(VirtualRealty.plotsSchemaFolder, OLD_REGION_PREFIX + plotID + OLD_REGION_SUFFIX);
		if (region.exists()) {
			String loaded = new String(new DataCompressor().decompressData(region));
			if (loaded.equalsIgnoreCase("clear"))
				return new String[] { "" };
			VirtualRealty.debug("Loaded in: " + (System.currentTimeMillis() - time) + " ms");
			return loaded.split("\\|");
		}
		return null;
	}

}
