package co.teamloops.loopvouchers.listeners;

import co.teamloops.commons.abstracts.LoopListener;
import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.loopvouchers.LoopVouchers;
import co.teamloops.loopvouchers.object.Voucher;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class PlayerListener extends LoopListener<LoopVouchers> {

    private final LoopVouchers plugin;

    public PlayerListener(final LoopVouchers plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onEat(final PlayerInteractEvent event) {
        final NBTItem nbtItem = new NBTItem(event.getPlayer().getItemInHand());

        if(event.getPlayer().getItemInHand()==null) return;
        if(!nbtItem.hasKey("voucher")) return;

        final String name = nbtItem.getString("voucher");
        final Voucher voucher = plugin.getVoucherRegistry().getRegistry().get(name);

        if(!event.getPlayer().hasPermission(voucher.getPermission())) return;

        final PlaceholderReplacer placeholderReplacer = new PlaceholderReplacer();
        placeholderReplacer.addPlaceholder("%voucher%", voucher.getName());
        placeholderReplacer.addPlaceholder("%player%", event.getPlayer().getName());

        for (final String string : voucher.getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), placeholderReplacer.parse(string));
        }

        for (final Map.Entry<PotionEffectType, Integer> effects : voucher.getEffects().entrySet()) {
            event.getPlayer().addPotionEffect(new PotionEffect(effects.getKey(), Integer.MAX_VALUE, effects.getValue() - 1));
        }

        final ItemStack item = event.getPlayer().getItemInHand();
        item.setAmount(item.getAmount()-1);
        event.getPlayer().setItemInHand(item);

        event.setCancelled(true);
    }
}
