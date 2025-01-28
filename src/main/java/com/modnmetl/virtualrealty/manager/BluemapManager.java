package com.modnmetl.virtualrealty.manager;

import com.modnmetl.virtualrealty.VirtualRealty;
import com.modnmetl.virtualrealty.model.other.HighlightType;
import com.modnmetl.virtualrealty.model.plot.Plot;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class BluemapManager {

    private final VirtualRealty instance;

    @Getter
    public boolean bluemapPresent = false;
    public BluemapAPI dapi = null;
    public MarkerSet markerset = null;
    public MarkerIcon markerIcon = null;
    private static final String MARKER_STRING = "<h3>Plot #%s</h3><b>Owned By: </b>Available";
    private static final String MARKER_OWNED_STRING = "<h3>Plot #%s</h3><b>Owned By: </b>%s<br><b>Owned Until: </b>%s";
    public static Set<AreaMarker> areaMarkers = new HashSet<>();


    public BluemapManager(VirtualRealty instance) {
        this.instance = instance;
    }

    public void registerBluemap() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("bluemap");
                if (plugin == null) return;
                bluemapPresent = true;
                if (!plugin.isEnabled()) return;
                dapi = (BluemapAPI) plugin;
                if (!dapi.markerAPIInitialized()) return;
                markerset = dapi.getMarkerAPI().getMarkerSet("virtualrealty.plots");
                if (markerset == null)
                    markerset = dapi.getMarkerAPI().createMarkerSet("virutalrealty.plots", "Plots", dapi.getMarkerAPI().getMarkerIcons(), false);
                for (MarkerSet markerSet : dapi.getMarkerAPI().getMarkerSets()) {
                    if (markerSet.getMarkerSetLabel().equalsIgnoreCase("Plots")) {
                        markerset = markerSet;
                    }
                }
                try {
                    if (dapi.getMarkerAPI().getMarkerIcon("virtualrealty_main_icon") == null) {
                        InputStream in = this.getClass().getResourceAsStream("/ploticon.png");
                        if (in != null && in.available() > 0) {
                            markerIcon = dapi.getMarkerAPI().createMarkerIcon("virtualrealty_main_icon", "Plots", in);
                        }
                    } else {
                        markerIcon = dapi.getMarkerAPI().getMarkerIcon("virtualrealty_main_icon");
                    }
                } catch (IOException ignored) {}
                VirtualRealty.debug("Registering plots markers..");
                for (Plot plot : PlotManager.getInstance().getPlots()) {
                    resetPlotMarker(plot);
                }
                VirtualRealty.debug("Registered plots markers");
                this.cancel();
            }
        }.runTaskTimer(instance, 20, 20 * 5);
    }

    private static AreaMarker getAreaMarker(String areaMarkerName) {
        if (VirtualRealty.getBluemapManager() == null) return null;
        for (AreaMarker areaMarker : VirtualRealty.getBluemapManager().markerset.getAreaMarkers()) {
            if (areaMarker.getMarkerID().equalsIgnoreCase(areaMarkerName)) return areaMarker;
        }
        return null;
    }

    public static void resetPlotMarker(Plot plot) {
        if (VirtualRealty.getBluemapManager() == null || !VirtualRealty.getBluemapManager().isBluemapPresent()) return;
        LocalDateTime localDateTime = plot.getOwnedUntilDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String ownedBy;
        double opacity;
        int color;
        if (plot.getOwnedBy() == null) {
            ownedBy = "Available";
            color = VirtualRealty.getPluginConfiguration().bluemapMarkersColor.available.getHexColor();
            opacity = VirtualRealty.getPluginConfiguration().bluemapMarkersColor.available.opacity;
        } else {
            ownedBy = plot.getPlotOwner().getName();
            color = VirtualRealty.getPluginConfiguration().bluemapMarkersColor.owned.getHexColor();
            opacity = VirtualRealty.getPluginConfiguration().bluemapMarkersColor.owned.opacity;
        }
        if (VirtualRealty.getPluginConfiguration().bluemapType == HighlightType.OWNED && plot.getOwnedBy() == null) return;
        if (VirtualRealty.getPluginConfiguration().bluemapType == HighlightType.AVAILABLE && plot.getOwnedBy() != null) return;
        AreaMarker marker = getAreaMarker("virtualrealty.plots." + plot.getID());
        if (marker == null) {
            marker = VirtualRealty.getBluemapManager().markerset.createAreaMarker("virtualrealty.plots." + plot.getID(),
                    plot.getOwnedBy() == null ? String.format(MARKER_STRING, plot.getID()) : String.format(MARKER_OWNED_STRING, plot.getID(), ownedBy, dateTimeFormatter.format(localDateTime)), true,
                    plot.getCreatedWorldRaw(), new double[]{plot.getXMin(), plot.getXMax()}, new double[]{plot.getZMin(), plot.getZMax()}, true);
            areaMarkers.add(marker);
        } else {
            marker.setLabel(
                    plot.getOwnedBy() == null ? String.format(MARKER_STRING, plot.getID()) : String.format(MARKER_OWNED_STRING, plot.getID(), ownedBy, dateTimeFormatter.format(localDateTime)), true);
        }
        marker.setFillStyle(opacity, color);
        marker.setLineStyle(2, 0.8, 0x474747);
        marker.setMarkerSet(VirtualRealty.getBluemapManager().markerset);
    }

    public static void removeBlueMapMarker(Plot plot) {
        if (VirtualRealty.getBluemapManager() == null || !VirtualRealty.getBluemapManager().isBluemapPresent() || VirtualRealty.getBluemapManager().dapi == null || VirtualRealty.getBluemapManager().markerset == null)
            return;
        AreaMarker marker = VirtualRealty.getBluemapManager().markerset.findAreaMarker("virtualrealty.plots." + plot.getID());
        if (marker == null) return;
        areaMarkers.remove(marker);
        marker.deleteMarker();
    }

}
