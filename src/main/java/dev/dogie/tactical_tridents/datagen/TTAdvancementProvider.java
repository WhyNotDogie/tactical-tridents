package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TTAdvancementProvider extends AdvancementProvider {
    public TTAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new TTAdvancementGenerator()));
    }

    public static class TTAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            ResourceKey<Advancement> obtain_ancient_debris = ResourceKey.create(
                    Registries.ADVANCEMENT,
                    ResourceLocation.withDefaultNamespace("nether/obtain_ancient_debris")
            );

            AdvancementHolder disposableIncome = Advancement.Builder.advancement()
                    .display(
                            TacticalTridents.NETHERITE_TRIDENT.get(),
                            Component.literal("Disposable Income"),
                            Component.literal("Upgrade a Trident with a Netherite Ingot."),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .parent(new AdvancementHolder(obtain_ancient_debris.location(), null))
                    .addCriterion("has_netherite_trident", InventoryChangeTrigger.TriggerInstance.hasItems(TacticalTridents.NETHERITE_TRIDENT.get()))
                    .save(saver, "tactical_tridents:disposable_income");

        }
    }
}
