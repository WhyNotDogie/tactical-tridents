package dev.dogie.tactical_tridents.client;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = "tactical_tridents", bus = EventBusSubscriber.Bus.MOD)
public class TTCreativeTabEvents {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(
                    new ItemStack(net.minecraft.world.item.Items.TRIDENT),
                    new ItemStack(TacticalTridents.NETHERITE_TRIDENT.get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }
}