package net.alloymc.mod.griefprevention.command;

import net.alloymc.api.AlloyAPI;
import net.alloymc.api.command.Command;
import net.alloymc.api.command.CommandSender;
import net.alloymc.api.economy.EconomyProvider;
import net.alloymc.api.entity.Player;
import net.alloymc.mod.griefprevention.GriefPreventionMod;
import net.alloymc.mod.griefprevention.message.Messages;
import net.alloymc.mod.griefprevention.player.PlayerData;

import java.util.ArrayList;
import java.util.List;

/**
 * Economy-related claim commands: buying and giving claim blocks.
 */
public final class EconomyCommands {

    private EconomyCommands() {}

    public static void registerAll(GriefPreventionMod mod) {
        mod.registerCommand(new BuyClaimBlocksCommand(mod));
        mod.registerCommand(new GiveClaimBlocksCommand(mod));
    }

    // ---- /buyclaimblocks ----

    static class BuyClaimBlocksCommand extends Command {
        private final GriefPreventionMod mod;
        BuyClaimBlocksCommand(GriefPreventionMod mod) {
            super("buyclaimblocks", "Purchase additional claim blocks",
                    "griefprevention.buyclaimblocks", List.of("buyclaim", "purchaseclaimblocks"));
            this.mod = mod;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            if (!sender.isPlayer()) { sender.sendMessage(Messages.PLAYER_ONLY); return true; }

            if (args.length < 1) {
                sender.sendMessage(String.format(Messages.USAGE, "/buyclaimblocks <amount>"));
                return true;
            }

            Player player = (Player) sender;

            int blockCount;
            try { blockCount = Integer.parseInt(args[0]); }
            catch (NumberFormatException e) {
                sender.sendMessage(String.format(Messages.USAGE, "/buyclaimblocks <amount>"));
                return true;
            }

            if (blockCount <= 0) {
                sender.sendMessage("\u00a7cAmount must be positive.");
                return true;
            }

            double costPerBlock = mod.config().claimsBlockPurchaseCost;
            if (costPerBlock <= 0) {
                sender.sendMessage("\u00a7cPurchasing claim blocks is disabled on this server.");
                return true;
            }

            EconomyProvider economy = AlloyAPI.economy().provider();
            if (economy == null) {
                sender.sendMessage("\u00a7cNo economy provider is active. Cannot purchase claim blocks.");
                return true;
            }

            double totalCost = blockCount * costPerBlock;
            double balance = economy.getBalance(player.uniqueId());

            if (balance < totalCost) {
                int affordable = (int) (balance / costPerBlock);
                sender.sendMessage(String.format(
                        Messages.BUY_BLOCKS_INSUFFICIENT_FUNDS,
                        totalCost, balance, affordable));
                return true;
            }

            boolean withdrawn = economy.withdraw(player.uniqueId(), totalCost);
            if (!withdrawn) {
                sender.sendMessage("\u00a7cTransaction failed. Please try again.");
                return true;
            }

            PlayerData data = mod.claimManager().getPlayerData(player.uniqueId());
            data.addBonusClaimBlocks(blockCount);
            mod.dataStore().savePlayerData(player.uniqueId(), data);

            sender.sendMessage(String.format(Messages.BUY_BLOCKS_SUCCESS,
                    blockCount, totalCost, data.remainingClaimBlocks()));
            return true;
        }
    }

    // ---- /giveclaimblocks ----

    static class GiveClaimBlocksCommand extends Command {
        private final GriefPreventionMod mod;
        GiveClaimBlocksCommand(GriefPreventionMod mod) {
            super("giveclaimblocks", "Give claim blocks to a player",
                    "griefprevention.giveclaimblocks", List.of("gcb"));
            this.mod = mod;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String[] args) {
            if (args.length < 2) {
                sender.sendMessage(String.format(Messages.USAGE, "/giveclaimblocks <player> <amount>"));
                return true;
            }

            var opt = AlloyAPI.server().player(args[0]);
            if (opt.isEmpty()) { sender.sendMessage(Messages.PLAYER_NOT_FOUND); return true; }
            Player target = opt.get();

            int amount;
            try { amount = Integer.parseInt(args[1]); }
            catch (NumberFormatException e) {
                sender.sendMessage(String.format(Messages.USAGE, "/giveclaimblocks <player> <amount>"));
                return true;
            }

            if (amount <= 0) {
                sender.sendMessage("\u00a7cAmount must be positive.");
                return true;
            }

            PlayerData data = mod.claimManager().getPlayerData(target.uniqueId());
            data.addBonusClaimBlocks(amount);
            mod.dataStore().savePlayerData(target.uniqueId(), data);

            sender.sendMessage(String.format(Messages.GIVE_BLOCKS_SUCCESS,
                    amount, target.displayName(), data.bonusClaimBlocks()));

            if (sender.isPlayer() && !((Player) sender).uniqueId().equals(target.uniqueId())) {
                target.sendMessage(String.format(Messages.GIVE_BLOCKS_RECEIVED,
                        amount, data.remainingClaimBlocks()));
            }

            return true;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            if (args.length == 1) return onlinePlayerNames(args[0]);
            return List.of();
        }
    }

    // ---- Helpers ----

    private static List<String> onlinePlayerNames(String prefix) {
        String lower = prefix.toLowerCase();
        List<String> names = new ArrayList<>();
        for (var p : AlloyAPI.server().onlinePlayers()) {
            if (p.name().toLowerCase().startsWith(lower)) names.add(p.name());
        }
        return names;
    }
}
