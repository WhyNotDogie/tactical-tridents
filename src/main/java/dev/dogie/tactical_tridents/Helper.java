package dev.dogie.tactical_tridents;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class Helper {
    public static boolean canUseHydro(Player instance, ItemStack item) {
        RegistryAccess registryAccess = instance.level().registryAccess();
        Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = enchantmentRegistry.getHolderOrThrow(TTEnchantments.HYDROSCOPIC);
        int level = item.getEnchantmentLevel(holder);

        boolean isNether = instance.level().dimension() == Level.NETHER;
        boolean isNetheriteTrident = item.is(TacticalTridents.NETHERITE_TRIDENT);

        return (level > 0) && !(isNether && !isNetheriteTrident);
    }
}
