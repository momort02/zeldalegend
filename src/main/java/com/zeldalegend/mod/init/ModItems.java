package com.zeldalegend.mod.init;

import com.zeldalegend.mod.ZeldaLegendMod;
import com.zeldalegend.mod.items.HylianShieldItem;
import com.zeldalegend.mod.items.LegendarySwordItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, ZeldaLegendMod.MOD_ID);

    private static final ToolMaterial LEGENDARY_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        3561,
        10.0F,
        8.0F,
        22,
        ItemTags.DIAMOND_TOOL_MATERIALS
    );

    public static final RegistryObject<Item> LEGENDARY_SWORD =
        ITEMS.register("legendary_sword", () -> new LegendarySwordItem(
            LEGENDARY_MATERIAL.applySwordProperties(
                new Item.Properties()
                    .setId(ITEMS.key("legendary_sword"))
                    .rarity(Rarity.EPIC)
                    .fireResistant(),
                3.0f,
                -2.4f
            )
        ));

    public static final RegistryObject<Item> HYLIAN_SHIELD =
        ITEMS.register("hylian_shield", () -> new HylianShieldItem(
            new Item.Properties()
                .setId(ITEMS.key("hylian_shield"))
                .rarity(Rarity.EPIC)
                .fireResistant()
                .durability(3072)
                .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(
                    0.05f,
                    0.5f,
                    List.of(new BlocksAttacks.DamageReduction(
                        90f,
                        Optional.empty(),
                        1.0f,
                        1.0f
                    )),
                    new BlocksAttacks.ItemDamageFunction(3.0f, 1.0f, 0f),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
                ))
        ));
}