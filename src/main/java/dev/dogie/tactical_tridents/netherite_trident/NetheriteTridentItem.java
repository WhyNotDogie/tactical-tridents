package dev.dogie.tactical_tridents.netherite_trident;

import dev.dogie.tactical_tridents.Helper;
import dev.dogie.tactical_tridents.netherite_trident.ThrownNetheriteTrident;
import dev.dogie.tactical_tridents.netherite_trident.render.NetheriteTridentBewlr;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class NetheriteTridentItem extends TridentItem {

    public NetheriteTridentItem(Item.Properties properties) {
        super(properties);
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (living instanceof Player player) {
            int elapsed = this.getUseDuration(stack, living) - timeLeft;
            if (elapsed >= 10) {
                var riptideEnchantment = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.RIPTIDE);
                int riptideLevel = stack.getEnchantmentLevel(riptideEnchantment);
                if (riptideLevel <= 0 || player.isInWaterOrRain() || Helper.canUseHydro(player, stack) || player.isInLava()) {
                    if (!level.isClientSide) {
                        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        if (riptideLevel == 0) {
                            ThrownNetheriteTrident tridentEntity = new ThrownNetheriteTrident(level, player, stack);
                            tridentEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + riptideLevel * 0.5F, 1.0F);
                            if (player.getAbilities().instabuild) {
                                tridentEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(tridentEntity);
                            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().instabuild) {
                                player.getInventory().removeItem(stack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (riptideLevel > 0) {
                        float yaw = player.getYRot();
                        float pitch = player.getXRot();
                        float x = -Mth.sin(yaw * (float) (Math.PI / 180.0F)) * Mth.cos(pitch * (float) (Math.PI / 180.0F));
                        float y = -Mth.sin(pitch * (float) (Math.PI / 180.0F));
                        float z = Mth.cos(yaw * (float) (Math.PI / 180.0F)) * Mth.cos(pitch * (float) (Math.PI / 180.0F));
                        float magnitude = Mth.sqrt(x * x + y * y + z * z);
                        float velocity = 3.0F * ((1.0F + riptideLevel) / 4.0F);
                        x *= velocity / magnitude;
                        y *= velocity / magnitude;
                        z *= velocity / magnitude;
                        player.push(x, y, z);
                        player.startAutoSpinAttack(20, 8.0F, stack);
                        if (player.onGround()) {
                            float bounce = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0D, bounce, 0.0D));
                        }

                        SoundEvent soundEvent;
                        if (riptideLevel >= 3) {
                            soundEvent = SoundEvents.TRIDENT_RIPTIDE_3.value();
                        } else if (riptideLevel == 2) {
                            soundEvent = SoundEvents.TRIDENT_RIPTIDE_2.value();
                        } else {
                            soundEvent = SoundEvents.TRIDENT_RIPTIDE_1.value();
                        }

                        level.playSound(null, player, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(stack);
        } else {
            var riptideEnchantment = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.RIPTIDE);
            if (stack.getEnchantmentLevel(riptideEnchantment) > 0 && !((player.isInWaterOrRain() || Helper.canUseHydro(player, stack)) || player.isInLava())) {
                return InteractionResultHolder.fail(stack);
            }
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
    }
}