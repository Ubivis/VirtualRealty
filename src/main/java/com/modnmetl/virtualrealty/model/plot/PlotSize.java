package com.modnmetl.virtualrealty.model.plot;

import org.bukkit.Material;

import lombok.Getter;

@Getter
public enum PlotSize {

	SMALL(10, 10, 10, Material.matchMaterial("GRASS_BLOCK"), Material.matchMaterial("STONE_BRICK_SLAB")),
	MEDIUM(25, 25, 25, Material.matchMaterial("GRASS_BLOCK"), Material.matchMaterial("STONE_BRICK_SLAB")),
	LARGE(50, 50, 50, Material.matchMaterial("GRASS_BLOCK"), Material.matchMaterial("STONE_BRICK_SLAB")),
	CUSTOM(0, 0, 0, Material.matchMaterial("GRASS_BLOCK"), Material.matchMaterial("STONE_BRICK_SLAB")),
	AREA(0, 0, 0, Material.AIR, Material.AIR);

	private int length;
	private int height;
	private int width;

	private Material floorMaterial;
	private Material borderMaterial;

	PlotSize(int length, int height, int width, Material floorMaterial, Material borderMaterial) {
		this.length = length;
		this.height = height;
		this.width = width;
		this.floorMaterial = floorMaterial;
		this.borderMaterial = borderMaterial;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setFloorMaterial(Material floorMaterial) {
		this.floorMaterial = floorMaterial;
	}

	public void setBorderMaterial(Material borderMaterial) {
		this.borderMaterial = borderMaterial;
	}

}
