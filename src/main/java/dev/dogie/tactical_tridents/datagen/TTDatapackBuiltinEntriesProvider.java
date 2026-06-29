package dev.dogie.tactical_tridents.datagen;

import dev.dogie.tactical_tridents.TTEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.util.Unit;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.Ignite;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TTDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, new TTEnchantmentBootstrap());

    public TTDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("tactical_tridents"));
    }

    public static class TTEnchantmentBootstrap implements RegistrySetBuilder.RegistryBootstrap<Enchantment> {

        @Override
        public void run(BootstrapContext<Enchantment> context) {
            HolderGetter<Enchantment> enchantLookup = context.lookup(Registries.ENCHANTMENT);
            HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);

            TagKey<Item> incandescentSupported = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tactical_tridents", "enchantable/incandescent"));

            register(context, TTEnchantments.FAKE_IMPALING_FUCKYOUEFFECTS, Enchantment.enchantment(
                            Enchantment.definition(
                                    itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                    2,
                                    5,
                                    Enchantment.dynamicCost(1,8),
                                    Enchantment.dynamicCost(21, 8),
                                    4,
                                    EquipmentSlotGroup.MAINHAND
                            )
                    ).exclusiveWith(enchantLookup.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                    .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(2.5F, 2.5F))));

            register(context, TTEnchantments.HYDROSCOPIC, Enchantment.enchantment(
                    Enchantment.definition(
                            itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                            2,
                            1,
                            Enchantment.dynamicCost(15, 15),
                            Enchantment.dynamicCost(65, 15),
                            4,
                            EquipmentSlotGroup.HAND
                    )
            ));

            register(context, TTEnchantments.INCANDESCENT, Enchantment.enchantment(
                    Enchantment.definition(
                            itemLookup.getOrThrow(incandescentSupported),
                            2,
                            2,
                            Enchantment.dynamicCost(10, 20),
                            Enchantment.dynamicCost(60, 20),
                            4,
                            EquipmentSlotGroup.HAND
                    )
            ).withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER, EnchantmentTarget.VICTIM, new Ignite(new LevelBasedValue.Linear(4.0F, 4.0F))));

            register(context, TTEnchantments.UPDRAFT, Enchantment.enchantment(
                    Enchantment.definition(
                            itemLookup.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                            2,
                            1,
                            Enchantment.dynamicCost(15, 15),
                            Enchantment.dynamicCost(65, 15),
                            4,
                            EquipmentSlotGroup.HAND
                    )
            ).withEffect(
                    EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                    new net.minecraft.world.item.enchantment.effects.DamageImmunity(),
                    DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FALL))
                    )
            ));
        }

        public void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
            registry.register(key, builder.build(key.location()));
        }
    }
}