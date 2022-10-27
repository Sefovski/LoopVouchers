package co.teamloops.loopvouchers.utils;

import co.teamloops.commons.builders.PlaceholderReplacer;
import co.teamloops.commons.support.XMaterial;
import co.teamloops.commons.utils.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ItemUtils {

    public static ItemStack getItemFromConfig(final FileConfiguration fileConfiguration, final String path, final PlaceholderReplacer placeholderReplacer) {
        final ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> lore = new ArrayList<>();
        itemStack.setAmount(fileConfiguration.getInt(path+".Amount"));
        itemStack.setType(XMaterial.matchXMaterial(fileConfiguration.getString(path+".Material")).get().parseMaterial());
        itemMeta.setDisplayName(Color.colorize(fileConfiguration.getString(path+".Name")));
        if(placeholderReplacer!=null) {
            for (final String string : Color.colorizeList(fileConfiguration.getStringList(path + ".Lore"))) {
                lore.add(placeholderReplacer.parse(string));
            }
        } else {
            lore.addAll(Color.colorizeList(fileConfiguration.getStringList(path + ".Lore")));
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}