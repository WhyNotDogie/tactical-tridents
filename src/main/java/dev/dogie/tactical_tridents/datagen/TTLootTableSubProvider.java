package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TTEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class TTLootTableSubProvider implements LootTableSubProvider {
    private final HolderLookup.Provider registries;

    public TTLootTableSubProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        builder.accept(ResourceKey.create(
                        Registries.LOOT_TABLE,
                        ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/incandescent")
                ), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(3)
                                .apply(new EnchantRandomlyFunction.Builder()
                                        .withEnchantment(enchantments.getOrThrow(TTEnchantments.INCANDESCENT))
                                )
                        )
                )
        );

        builder.accept(ResourceKey.create(
                        Registries.LOOT_TABLE,
                        ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/updraft")
                ), LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.BOOK)
                                        .setWeight(3)
                                        .apply(new EnchantRandomlyFunction.Builder()
                                                .withEnchantment(enchantments.getOrThrow(TTEnchantments.UPDRAFT))
                                        )
                                )
                        )
        );

        builder.accept(ResourceKey.create(
                        Registries.LOOT_TABLE,
                        ResourceLocation.fromNamespaceAndPath("tactical_tridents", "inject/hydroscopic")
                ), LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.BOOK)
                                        .setWeight(3)
                                        .apply(new EnchantRandomlyFunction.Builder()
                                                .withEnchantment(enchantments.getOrThrow(TTEnchantments.HYDROSCOPIC))
                                        )
                                )
                        ).setParamSet(LootContextParamSets.FISHING)
        );
    }
}