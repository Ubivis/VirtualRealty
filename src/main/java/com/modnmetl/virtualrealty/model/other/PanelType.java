package com.modnmetl.virtualrealty.model.other;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PanelType {

    PLOTS("Plots", 0),
    PLOT("Plot", 18),
    MEMBERS("Members", 36),
    PLOT_SETTINGS("Settings", 18),
    MEMBER("Member", 18),
    PLOT_PERMISSIONS("Permissions", 18),
    MANAGEMENT_PERMISSIONS("Management Permissions", 18);

    private final String inventoryName;
    private final int backwardsButtonIndex;

}
