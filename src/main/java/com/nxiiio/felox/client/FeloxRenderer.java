package com.nxiiio.felox.client;

import com.nxiiio.felox.entity.FeloxEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class FeloxRenderer extends GeoEntityRenderer<FeloxEntity> {
    public FeloxRenderer(EntityRendererProvider.Context context) {
        super(context, new FeloxModel());
        shadowRadius = 0.7F;
    }
}
