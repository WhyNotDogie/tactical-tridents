package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TacticalTridents.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(Registries.ENCHANTMENT, new TTDatapackBuiltinEntriesProvider.TTEnchantmentBootstrap())
        );

        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(),
                new TTItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(),
                new TTLootModifierProvider(packOutput, lookupProvider));

        generator.addProvider(
                event.includeServer(),
                new LootTableProvider(
                        packOutput,
                        Set.of(),
                        List.of(new LootTableProvider.SubProviderEntry(
                                TTLootTableSubProvider::new,
                                LootContextParamSets.CHEST
                        )),
                        lookupProvider
                )
        );

        generator.addProvider(event.includeServer(), new TTItemTagsProvider(
                packOutput,
                lookupProvider,
                CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
                existingFileHelper
        ));

        generator.addProvider(event.includeServer(), new TTRecipeProvider(packOutput, lookupProvider));
    }
}