package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TTItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> ENCHANTABLE_INCANDESCENT = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "enchantable/incandescent")
    );

    public TTItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, TacticalTridents.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.TRIDENT_ENCHANTABLE)
                .add(TacticalTridents.NETHERITE_TRIDENT.get());
        this.tag(ENCHANTABLE_INCANDESCENT)
                .add(TacticalTridents.NETHERITE_TRIDENT.get());
    }
}