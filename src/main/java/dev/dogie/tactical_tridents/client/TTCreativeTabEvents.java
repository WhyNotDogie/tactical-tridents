package dev.dogie.tactical_tridents.client;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS || event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.getParentEntries().removeIf(TTCreativeTabEvents::isHiddenBook);
            event.getSearchEntries().removeIf(TTCreativeTabEvents::isHiddenBook);
        }
    }
    private static boolean isHiddenBook(ItemStack stack) {
        if (stack.is(Items.ENCHANTED_BOOK)) {
            var enchantments = stack.getTagEnchantments().keySet();
            for (var enchantmentHolder : enchantments) {
                if (enchantmentHolder.unwrapKey().isPresent()) {
                    var key = enchantmentHolder.unwrapKey().get();
                    if (key.location().getNamespace().equals("tactical_tridents") &&
                            key.location().getPath().equals("fake_impaling")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}