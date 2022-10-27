package co.teamloops.loopvouchers;

import co.teamloops.commons.chat.MessageCache;
import co.teamloops.commons.config.ConfigManager;
import co.teamloops.loopvouchers.commands.VoucherCommand;
import co.teamloops.loopvouchers.listeners.PlayerListener;
import co.teamloops.loopvouchers.object.Voucher;
import co.teamloops.loopvouchers.registry.VoucherRegistry;
import co.teamloops.loopvouchers.utils.ItemUtils;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class LoopVouchers extends JavaPlugin {

    public ConfigManager<LoopVouchers> configManager;
    @Getter public VoucherRegistry voucherRegistry;
    @Getter public FileConfiguration configuration;
    @Getter public MessageCache messageCache;
    @Getter public LoopVouchers plugin;

    @Override
    public void onEnable() {
        init();
        loadConfigurations();
        loadMessages();
        loadCommands();
        loadListeners();
        loadFood();

    }

    private void loadListeners() {
      new PlayerListener(this);
    }

    private void loadCommands() {
         this.getCommand("vouchers").setExecutor(new VoucherCommand(this));
    }

    private void loadMessages() {
        this.messageCache = new MessageCache(configuration);

        for (final String key : configuration.getConfigurationSection("Messages").getKeys(false)) {
            this.messageCache.loadMessage("Messages." + key);
        }

    }

    private void init() {
        plugin = this;
        this.voucherRegistry = new VoucherRegistry();
    }

    private void loadConfigurations() {
        this.configManager = new ConfigManager<>(this);

        this.configManager.loadConfiguration("settings");

        this.configuration = this.configManager.getConfig("settings");
    }

    private void loadFood() {
        for(final String code : configuration.getConfigurationSection("Vouchers").getKeys(false)) {
            final Voucher voucher = new Voucher(code);
            voucher.setCommands(configuration.getStringList("Vouchers."+code+".Commands"));
            for (final String effectKeys : configuration.getStringList("Vouchers."+code+".Effects")) {
                final String[] effect = effectKeys.split(";");

                final ItemStack itemStack = ItemUtils.getItemFromConfig(configuration, "Vouchers."+code+".Item", null);
                final NBTItem nbtItem = new NBTItem(itemStack);
                nbtItem.setString("voucher", code);

                voucher.getEffects().put(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]));
                voucher.setItem(nbtItem.getItem());
                voucher.setPermission(configuration.getString("Vouchers." + code + ".Permission"));
                voucherRegistry.getRegistry().put(code, voucher);
            }
        }
    }


}
