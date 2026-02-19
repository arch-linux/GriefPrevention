package net.alloymc.mod.griefprevention.command;

import net.alloymc.api.command.Command;
import net.alloymc.api.command.CommandSender;
import net.alloymc.api.entity.Player;
import net.alloymc.mod.griefprevention.GriefPreventionMod;
import net.alloymc.mod.griefprevention.claim.Claim;
import net.alloymc.mod.griefprevention.claim.ClaimFlag;
import net.alloymc.mod.griefprevention.claim.ClaimPermission;
import net.alloymc.mod.griefprevention.message.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * /claimflags — view and toggle per-claim flags.
 *
 * Usage:
 *   /claimflags              — show all flags for the claim you're in
 *   /claimflags <flag> on|off — toggle a flag
 */
public class ClaimFlagsCommand extends Command {

    private final GriefPreventionMod mod;

    public ClaimFlagsCommand(GriefPreventionMod mod) {
        super("claimflags", "View or toggle per-claim flags for public interactions",
                "griefprevention.claims", List.of("cf", "claimflag", "flags"));
        this.mod = mod;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!sender.isPlayer()) { sender.sendMessage(Messages.PLAYER_ONLY); return true; }
        Player player = (Player) sender;

        Claim claim = mod.claimManager().getClaimAt(player.location());
        if (claim == null) {
            player.sendMessage(Messages.NO_CLAIM_HERE, Player.MessageType.ERROR);
            return true;
        }

        // Permission check: MANAGE trust or admin access
        String denial = claim.checkPermission(player.uniqueId(), ClaimPermission.MANAGE);
        if (denial != null && !mod.canAccessAdminClaim(player)) {
            player.sendMessage(Messages.NO_MANAGE_PERMISSION, Player.MessageType.ERROR);
            return true;
        }

        // No args: show all flags
        if (args.length == 0) {
            player.sendMessage(String.format(Messages.FLAGS_HEADER, claim.id()));
            for (ClaimFlag flag : ClaimFlag.values()) {
                boolean value = claim.getFlag(flag.key());
                String status = value ? Messages.FLAG_ON : Messages.FLAG_OFF;
                player.sendMessage(String.format(Messages.FLAG_ENTRY, flag.key(), status, flag.description()));
            }
            return true;
        }

        // 1 or 2 args: toggle a flag
        String flagKey = args[0].toLowerCase();
        ClaimFlag flag = ClaimFlag.fromKey(flagKey);
        if (flag == null) {
            player.sendMessage(String.format(Messages.FLAG_UNKNOWN, flagKey), Player.MessageType.ERROR);
            return true;
        }

        boolean newValue;
        if (args.length >= 2) {
            String toggle = args[1].toLowerCase();
            if (toggle.equals("on") || toggle.equals("true") || toggle.equals("yes")) {
                newValue = true;
            } else if (toggle.equals("off") || toggle.equals("false") || toggle.equals("no")) {
                newValue = false;
            } else {
                sender.sendMessage(String.format(Messages.USAGE, "/claimflags <flag> on|off"));
                return true;
            }
        } else {
            // Toggle: flip current value
            newValue = !claim.getFlag(flag.key());
        }

        claim.setFlag(flag.key(), newValue);
        mod.dataStore().saveClaim(claim.parent() != null ? claim.parent() : claim);

        String valueStr = newValue ? "on" : "off";
        player.sendMessage(String.format(Messages.FLAG_SET, flag.key(), valueStr), Player.MessageType.SUCCESS);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            List<String> completions = new ArrayList<>();
            for (ClaimFlag flag : ClaimFlag.values()) {
                if (flag.key().startsWith(prefix)) completions.add(flag.key());
            }
            return completions;
        }
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            List<String> completions = new ArrayList<>();
            for (String opt : List.of("on", "off")) {
                if (opt.startsWith(prefix)) completions.add(opt);
            }
            return completions;
        }
        return List.of();
    }
}
