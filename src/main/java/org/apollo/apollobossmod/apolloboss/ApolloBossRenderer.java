package org.apollo.apollobossmod.apolloboss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.apollo.apollobossmod.Apollobossmod;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ApolloBossRenderer extends GeoEntityRenderer<ApolloBoss> {
    public ApolloBossRenderer(EntityRendererProvider.Context renderManager)
    {
        super(renderManager,new ApolloBossModel());
        this.shadowRadius=0.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(ApolloBoss instance) {
        return new ResourceLocation(Apollobossmod.MODID,"textures/entity/apollo_boss_texture.png");
    }

    @Override
    public RenderType getRenderType(ApolloBoss animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        stack.scale(1.0F,1.0F,1.0F);
        return super.getRenderType(animatable,partialTicks,stack,renderTypeBuffer,vertexBuilder,packedLightIn,textureLocation);
    }
}
