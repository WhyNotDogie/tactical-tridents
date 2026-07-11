package dev.dogie.tactical_tridents.netherite_trident.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.dogie.tactical_tridents.client.TTModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class NetheriteTridentBewlr extends BlockEntityWithoutLevelRenderer {
    private NetheriteTridentModel model;

    public NetheriteTridentBewlr() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    private NetheriteTridentModel getOrCreateModel() {
        if (this.model == null) {
            Minecraft minecraft = Minecraft.getInstance();
            EntityModelSet modelSet = minecraft.getEntityModels();
            this.model = new NetheriteTridentModel(modelSet.bakeLayer(TTModelLayers.NETHERITE_TRIDENT));
        }
        return this.model;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        getOrCreateModel();
        if (this.model == null) return;
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(NetheriteTridentModel.TEXTURE), false, stack.hasFoil());
        model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, -1);
        poseStack.popPose();
    }
}
