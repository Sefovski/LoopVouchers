package co.teamloops.loopvouchers.commands;

import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.loopvouchers.LoopVouchers;
import co.teamloops.loopvouchers.object.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VoucherCommand implements CommandExecutor {

    private final LoopVouchers plugin;
    public VoucherCommand(final LoopVouchers plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] args) {
        if(args.length < 4) {
            plugin.getMessageCache().sendMessage((Player) commandSender, "Messages.HELP");
            return false;
        }

        if (!commandSender.hasPermission("vouchers.admin")) {
            plugin.getMessageCache().sendMessage((Player) commandSender, "Messages.NO-PERMISSION");
            return false;
        }

        final Player player = Bukkit.getPlayerExact(args[1]);
        final int amount = Integer.parseInt(args[3]);
        final Voucher voucher = plugin.getVoucherRegistry().getRegistry().get(args[2]);

        if(voucher==null||player==null) {
            plugin.getMessageCache().sendMessage((Player) commandSender, "Messages.HELP");
            return false;
        }

        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer();
        placeholderReplacer.addPlaceholder("%voucher%", voucher.getName());
        placeholderReplacer.addPlaceholder("%player%", player.getName());
        placeholderReplacer.addPlaceholder("%amount%", Integer.toString(amount));

        final ItemStack toGive = voucher.getItem();
        toGive.setAmount(amount);
        player.getInventory().addItem(toGive);

        plugin.getMessageCache().sendMessage((Player) commandSender, placeholderReplacer, "Messages.VOUCHER-GIVEN");
        plugin.getMessageCache().sendMessage(player, placeholderReplacer, "Messages.VOUCHER-RECEIVED");

        return false;
    }
}
