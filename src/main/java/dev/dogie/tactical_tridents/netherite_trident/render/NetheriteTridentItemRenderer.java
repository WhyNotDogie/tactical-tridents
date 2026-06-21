package dev.dogie.tactical_tridents.netherite_trident.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class NetheriteTridentItemRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("tactical_tridents", "textures/entity/netherite_trident.png");

    private NetheriteTridentItemRenderer() {
    }

    public static void render(TridentModel model, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(TEXTURE), false, stack.hasFoil());
        model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, -1);
        poseStack.popPose();
    }
}