package dev.dogie.tactical_tridents;

import dev.dogie.tactical_tridents.TacticalTridents;
import dev.dogie.tactical_tridents.netherite_trident.render.NetheriteTridentBewlr;
import dev.dogie.tactical_tridents.netherite_trident.render.ThrownNetheriteTridentRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

@EventBusSubscriber(modid = "tactical_tridents", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TTClientEvents {
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
}