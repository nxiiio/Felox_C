package com.nxiiio.felox;

import com.nxiiio.felox.registry.ModEntities;
import com.nxiiio.felox.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod(Felox.MOD_ID)
public final class Felox {
    public static final String MOD_ID = "felox";

    public Felox(FMLJavaModLoadingContext context) {
        GeckoLib.initialize();
        IEventBus modBus = context.getModEventBus();
        ModEntities.register(modBus);
        ModItems.register(modBus);
    }
}
