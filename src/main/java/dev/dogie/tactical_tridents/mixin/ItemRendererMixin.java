package dev.dogie.tactical_tridents.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dogie.tactical_tridents.TacticalTridents;
import dev.dogie.tactical_tridents.client.TTClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    BakedModel renderItem(BakedModel model, ItemStack stack, ItemDisplayContext displayContext, boolean leftHand,
                          PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (Minecraft.getInstance().level == null) return model;
        if (!stack.is(TacticalTridents.NETHERITE_TRIDENT.get())) return model;
        if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED) {
            return tactical_tridents$getModelWithOverrides(TTClientEvents.TRIDENT_MODEL, stack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0);
        }
        return model;
    }

    @ModifyReturnValue(method = "getModel", at = @At(value = "RETURN"))
    private BakedModel getModel(BakedModel original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) @Nullable Level level, @Local(argsOnly = true) @Nullable LivingEntity entity, @Local(argsOnly = true) int seed) {
        if (stack.is(TacticalTridents.NETHERITE_TRIDENT.get())) {
            @Nullable ClientLevel clientLevel = level instanceof ClientLevel lv ? lv : null;
            return tactical_tridents$getModelWithOverrides(TTClientEvents.TRIDENT_HELD_MODEL, stack, clientLevel, entity, seed);
        }

        return original;
    }

    @Unique
    private static BakedModel tactical_tridents$getModelWithOverrides(ModelResourceLocation location, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
        return model.getOverrides().resolve(model, stack, level, entity, seed);
    }
}
