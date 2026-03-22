package com.zeldalegend.mod;

import com.zeldalegend.mod.init.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ZeldaLegendMod.MOD_ID)
public class ZeldaLegendMod {
    public static final String MOD_ID = "zeldalegend";

    public ZeldaLegendMod(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        ModItems.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
