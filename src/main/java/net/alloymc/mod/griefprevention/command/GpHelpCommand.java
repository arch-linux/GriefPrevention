package net.alloymc.mod.griefprevention.command;

import net.alloymc.api.command.Command;
import net.alloymc.api.command.CommandSender;
import net.alloymc.mod.griefprevention.message.Messages;

import java.util.List;

/**
 * /gphelp — Lists all GriefPrevention commands organized by category.
 */
public class GpHelpCommand extends Command {

    public GpHelpCommand() {
        super("gphelp", "Lists all GriefPrevention commands",
                null, List.of("gpcommands", "griefpreventionhelp"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(Messages.HELP_HEADER);

        // Claims
        sender.sendMessage(String.format(Messages.HELP_CATEGORY, "Claims"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "claim [radius]", "Create a claim centered on you"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "abandonclaim", "Delete the claim you're in"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "abandontoplevelclaim", "Delete claim + all subdivisions"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "abandonallclaims confirm", "Delete ALL your claims"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "extendclaim <blocks>", "Extend claim in facing direction"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "claimslist [player]", "List your claims and blocks"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "claimexplosions", "Toggle explosions in your claim"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "claimflags [flag] [on|off]", "View/toggle per-claim flags"));

        // Trust
        sender.sendMessage(String.format(Messages.HELP_CATEGORY, "Trust"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "trust <player>", "Grant build trust"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "containertrust <player>", "Grant container access"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "accesstrust <player>", "Grant basic access (doors, buttons)"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "permissiontrust <player>", "Grant manage permission"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "untrust <player>", "Revoke all trust"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "trustlist", "Show who is trusted in this claim"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "restrictsubclaim", "Toggle subclaim permission inheritance"));

        // Admin
        if (sender.hasPermission("griefprevention.adminclaims")) {
            sender.sendMessage(String.format(Messages.HELP_CATEGORY, "Admin"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "adminclaims", "Switch to admin claim mode"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "basicclaims", "Switch to basic claim mode"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "subdivideclaims", "Switch to subdivision mode"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "ignoreclaims", "Toggle claim bypass mode"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "deleteclaim", "Delete the claim you're in"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "deleteallclaims <player>", "Delete all claims of a player"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "transferclaim <player>", "Transfer claim to a player"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "adjustbonusclaimblocks <player> <amount>", "Adjust bonus blocks"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "setaccruedclaimblocks <player> <amount>", "Set accrued blocks"));
            sender.sendMessage(String.format(Messages.HELP_ENTRY, "gpreload", "Reload configuration"));
        }

        // Utility
        sender.sendMessage(String.format(Messages.HELP_CATEGORY, "Utility"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "trapped", "Escape from someone's claim"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "unlockdrops", "Let others pick up your death drops"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "ignoreplayer <player>", "Ignore a player's chat"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "unignoreplayer <player>", "Stop ignoring a player"));
        sender.sendMessage(String.format(Messages.HELP_ENTRY, "ignoredplayerlist", "List ignored players"));

        return true;
    }
}
