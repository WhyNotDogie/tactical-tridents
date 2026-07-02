package dev.dogie.tactical_tridents;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class TTEnchantments {
    public static final ResourceKey<Enchantment> FAKE_IMPALING = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("tactical_tridents", "fake_impaling")
    );

    public static final ResourceKey<Enchantment> HYDROSCOPIC = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("tactical_tridents", "hydroscopic")
    );

    public static final ResourceKey<Enchantment> INCANDESCENT = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("tactical_tridents", "incandescent")
    );

    public static final ResourceKey<Enchantment> UPDRAFT = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("tactical_tridents", "updraft")
    );
}
