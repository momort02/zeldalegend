package com.zeldalegend.mod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class HylianShieldItem extends Item {

    public HylianShieldItem(Item.Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6✦ Le Bouclier Sacré d'Hylia ✦"));
        tooltip.add(Component.literal("§7Forgé par la Déesse Hylia elle-même"));
        tooltip.add(Component.literal("§b→ Résistance aux dégâts lors du blocage"));
        tooltip.add(Component.literal("§b→ Immunité au feu"));
        tooltip.add(Component.literal("§b→ Durabilité légendaire"));
    }
}