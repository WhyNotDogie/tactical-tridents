package dev.dogie.tactical_tridents.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.dogie.tactical_tridents.TTEnchantments;
import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentHelper.class)
public class ModifyDamageMixin {
    @WrapMethod(method = "modifyDamage")
    private static float modifyDamage(ServerLevel level, ItemStack stack, Entity entity, DamageSource damageSource, float initialDamage, Operation<Float> original) {
        boolean isNetheriteTrident = stack.is(TacticalTridents.NETHERITE_TRIDENT);
        if (!(stack.is(Items.TRIDENT) || isNetheriteTrident)) {
            return original.call(level, stack, entity, damageSource, initialDamage);
        }

        RegistryAccess registryAccess = level.registryAccess();
        Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);

        Holder<Enchantment> hydroscopicHolder = enchantmentRegistry.getHolderOrThrow(TTEnchantments.HYDROSCOPIC);
        int hydroscopicLevel = stack.getEnchantmentLevel(hydroscopicHolder);
        Holder<Enchantment> impalingHolder = enchantmentRegistry.getHolderOrThrow(Enchantments.IMPALING);
        int impalingLevel = stack.getEnchantmentLevel(impalingHolder);
        if (hydroscopicLevel == 0 || impalingLevel == 0) {
            return original.call(level, stack, entity, damageSource, initialDamage);
        }
        boolean isNether = entity.level().dimension() == Level.NETHER;
        if (isNether && !isNetheriteTrident) {
            return original.call(level, stack, entity, damageSource, initialDamage);
        }

        ItemStack evilAssFakeTrident = stack.copy();
        Holder<Enchantment> fakeImpalingHolder = enchantmentRegistry.getHolderOrThrow(TTEnchantments.FAKE_IMPALING);
        EnchantmentHelper.updateEnchantments(evilAssFakeTrident, mutableEnchants -> {
            mutableEnchants.set(impalingHolder, 0);
            mutableEnchants.set(fakeImpalingHolder, impalingLevel);
        });
        return original.call(level, evilAssFakeTrident, entity, damageSource, initialDamage);
    }
}
