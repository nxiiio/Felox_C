package com.nxiiio.felox.client;

import com.nxiiio.felox.Felox;
import com.nxiiio.felox.entity.FeloxEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class FeloxModel extends GeoModel<FeloxEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(Felox.MOD_ID, "geo/felox.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Felox.MOD_ID, "textures/entity/felox.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(Felox.MOD_ID, "animations/felox.animation.json");

    @Override
    public ResourceLocation getModelResource(FeloxEntity entity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FeloxEntity entity) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FeloxEntity entity) {
        return ANIMATIONS;
    }
}
