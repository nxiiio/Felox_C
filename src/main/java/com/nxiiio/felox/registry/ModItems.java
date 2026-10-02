package com.nxiiio.felox.registry;

import com.nxiiio.felox.Felox;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Felox.MOD_ID);

    public static final RegistryObject<ForgeSpawnEggItem> FELOX_SPAWN_EGG = ITEMS.register("felox_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FELOX, 0x36C94A, 0x196B2A, new Item.Properties()));

    private ModItems() {}

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
        modBus.addListener(ModItems::addCreativeItems);
    }

    private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(FELOX_SPAWN_EGG);
        }
    }
}
