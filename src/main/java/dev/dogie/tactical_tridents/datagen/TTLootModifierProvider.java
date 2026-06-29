package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class TTLootModifierProvider extends GlobalLootModifierProvider {
    public TTLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, TacticalTridents.MODID);
    }

    @Override
    protected void start() {
        HolderLookup.RegistryLookup<Biome> biomes = this.registries.lookupOrThrow(Registries.BIOME);

        this.add("incandescent_from_bastion",
                new net.neoforged.neoforge.common.loot.AddTableLootModifier(
                        new LootItemCondition[]{
                                new LootTableIdCondition.Builder(BuiltInLootTables.BASTION_TREASURE.location()).build(),
                                LootItemRandomChanceCondition.randomChance(0.33f).build()
                        },
                        ResourceKey.create(
                                Registries.LOOT_TABLE,
                                ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/incandescent")
                        )
                ));
        this.add("updraft_from_ominous_vault",
                new net.neoforged.neoforge.common.loot.AddTableLootModifier(
                        new LootItemCondition[]{
                                new LootTableIdCondition.Builder(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS.location()).build(),
                                LootItemRandomChanceCondition.randomChance(0.1f).build()
                        },
                        ResourceKey.create(
                                Registries.LOOT_TABLE,
                                ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/updraft")
                        )
                ));
        this.add("hydroscopic_from_fishing",
                new net.neoforged.neoforge.common.loot.AddTableLootModifier(
                        new LootItemCondition[]{
                                new LootTableIdCondition.Builder(BuiltInLootTables.FISHING.location()).build(),
                                LootItemRandomChanceCondition.randomChance(0.03f).build(),
                                LocationCheck.checkLocation(LocationPredicate.Builder.inBiome(biomes.getOrThrow(Biomes.DESERT))).build()
                        },
                        ResourceKey.create(
                                Registries.LOOT_TABLE,
                                ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/hydroscopic")
                        )
                ));
    }
}
