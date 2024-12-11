package com.modnmetl.virtualrealty.premiumloader;

import com.modnmetl.virtualrealty.VirtualRealty;
import com.modnmetl.virtualrealty.util.PanelUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public final class PremiumLoader {

    public PremiumLoader() {
        VirtualRealty.getInstance().getLogger().log(Level.INFO, "GUI license features have been loaded!");
    }

    public void onDisable() {
        PanelUtil.SELECTED_PANEL.forEach((uuid, panelType) -> {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            player.closeInventory();
        });
    }

}
