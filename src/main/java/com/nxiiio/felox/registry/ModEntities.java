package com.nxiiio.felox.registry;

import com.nxiiio.felox.Felox;
import com.nxiiio.felox.entity.FeloxEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Felox.MOD_ID);

    public static final RegistryObject<EntityType<FeloxEntity>> FELOX = ENTITIES.register("felox",
            () -> EntityType.Builder.of(FeloxEntity::new, MobCategory.CREATURE)
                    .sized(1.4F, 1.8F)
                    .clientTrackingRange(10)
                    .build(Felox.MOD_ID + ":felox"));

    private ModEntities() {}

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
        modBus.addListener(ModEntities::createAttributes);
    }

    private static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(FELOX.get(), FeloxEntity.createAttributes().build());
    }
}
