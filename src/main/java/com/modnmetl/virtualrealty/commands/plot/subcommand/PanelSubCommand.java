package com.modnmetl.virtualrealty.commands.plot.subcommand;

import com.modnmetl.virtualrealty.VirtualRealty;
import com.modnmetl.virtualrealty.commands.SubCommand;
import com.modnmetl.virtualrealty.exception.FailedCommandException;
import com.modnmetl.virtualrealty.manager.PlotManager;
import com.modnmetl.virtualrealty.util.PanelUtil;
import com.modnmetl.virtualrealty.model.other.ChatMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class PanelSubCommand extends SubCommand {

    public PanelSubCommand() {}

    public PanelSubCommand(CommandSender sender, Command command, String label, String[] args) throws FailedCommandException {
        super(sender, command, label, args, new LinkedList<>());
    }

    @Override
    public void exec(CommandSender sender, Command command, String label, String[] args) throws FailedCommandException {
        assertPlayer();
        Player player = ((Player) sender);
        if (PlotManager.getInstance().getAccessPlots(player.getUniqueId()).isEmpty()) {
            ChatMessage.of(VirtualRealty.getMessages().noPlayerPlotsFound).sendWithPrefix(sender);
            return;
        }
        PanelUtil.openPlotsPage(player, 1);
    }

}
