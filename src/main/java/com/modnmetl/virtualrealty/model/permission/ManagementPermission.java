package com.modnmetl.virtualrealty.model.permission;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import com.modnmetl.virtualrealty.util.data.ItemBuilder;

import lombok.Getter;

@Getter
public enum ManagementPermission {

	ADD_MEMBER(0, "Add Members", new ItemBuilder(Material.DIAMOND_AXE).addItemFlag(ItemFlag.HIDE_ATTRIBUTES)),
	KICK_MEMBER(1, "Kick Members", new ItemBuilder(Material.STONE_AXE).addItemFlag(ItemFlag.HIDE_ATTRIBUTES)),
	PLOT_PERMISSIONS(2, "Plot Management", new ItemBuilder(Material.WRITABLE_BOOK));

	private final int slot;
	private final String name;
	private final ItemBuilder item;

	ManagementPermission(int slot, String name, ItemBuilder item) {
		this.slot = slot;
		this.name = name;
		this.item = item;
	}

	public static ManagementPermission getPermission(int i) {
		for (ManagementPermission value : values()) {
			if (value.getSlot() == i)
				return value;
		}
		return null;
	}

	public String getConfigName() {
		return name().replaceAll("_", " ");
	}

}
