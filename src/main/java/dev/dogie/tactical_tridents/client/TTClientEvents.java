package dev.dogie.tactical_tridents.client;

import dev.dogie.tactical_tridents.TacticalTridents;
import dev.dogie.tactical_tridents.netherite_trident.render.NetheriteTridentBewlr;
import dev.dogie.tactical_tridents.netherite_trident.render.NetheriteTridentModel;
import dev.dogie.tactical_tridents.netherite_trident.render.ThrownNetheriteTridentRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@EventBusSubscriber(modid = "tactical_tridents", value = Dist.CLIENT)
public class TTClientEvents {
    public static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "item/netherite_trident"));
    public static final ModelResourceLocation TRIDENT_HELD_MODEL = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "item/netherite_trident_in_hand"));

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        registerModelPredicates();
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(TRIDENT_MODEL);
        event.register(TRIDENT_HELD_MODEL);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new NetheriteTridentBewlr();
            }
        }, TacticalTridents.NETHERITE_TRIDENT.get());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TacticalTridents.THROWN_NETHERITE_TRIDENT.get(), ThrownNetheriteTridentRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TTModelLayers.NETHERITE_TRIDENT, NetheriteTridentModel::createBodyLayer);
    }

    public static void registerModelPredicates() {
        ItemProperties.register(TacticalTridents.NETHERITE_TRIDENT.get(), ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "throwing"), (ClampedItemPropertyFunction)((stack, level, entity, p_234999_) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }
}
