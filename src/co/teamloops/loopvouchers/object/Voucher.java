package co.teamloops.loopvouchers.object;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Voucher {

    private final String name;
    private final Map<PotionEffectType, Integer> effects;
    private List<String> commands;
    private String permission;
    private ItemStack item;

    public Voucher(final String name) {
        this.name = name;
        this.commands = new ArrayList<>();
        this.effects = new HashMap<>();
    }
}
