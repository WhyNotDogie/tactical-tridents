package dev.dogie.tactical_tridents.netherite_trident.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dogie.tactical_tridents.client.TTModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class NetheriteTridentBewlr extends BlockEntityWithoutLevelRenderer {

    private NetheriteTridentModel model;

    public NetheriteTridentBewlr() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = null;
    }

    private NetheriteTridentModel getOrCreateModel() {
        if (this.model == null) {
            Minecraft minecraft = Minecraft.getInstance();
            EntityModelSet modelSet = minecraft != null ? minecraft.getEntityModels() : null;
            if (modelSet == null) {
                return null;
            }
            this.model = new NetheriteTridentModel(modelSet.bakeLayer(TTModelLayers.NETHERITE_TRIDENT));
        }
        return this.model;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        NetheriteTridentModel model = this.getOrCreateModel();
        if (model == null) {
            return;
        }
        NetheriteTridentItemRenderer.render(model, stack, context, poseStack, buffer, packedLight, packedOverlay);
    }
}