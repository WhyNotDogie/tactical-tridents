package dev.dogie.tactical_tridents.netherite_trident;

import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

// this is the biggest fucking headache on the face of the earth
public class ThrownNetheriteTrident extends AbstractArrow {
    private static final EntityDataAccessor<Byte> LOYALTY = SynchedEntityData.defineId(ThrownNetheriteTrident.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.defineId(ThrownNetheriteTrident.class, EntityDataSerializers.BOOLEAN);

    private ItemStack tridentStack = TacticalTridents.NETHERITE_TRIDENT.get().getDefaultInstance();
    private boolean dealtDamage;
    public int returnTimer;

    public ThrownNetheriteTrident(EntityType<? extends ThrownNetheriteTrident> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownNetheriteTrident(Level level, LivingEntity owner, ItemStack stack) {
        super(TacticalTridents.THROWN_NETHERITE_TRIDENT.get(), owner, level, stack, stack);
        this.tridentStack = stack.copy();
        this.entityData.set(LOYALTY, this.getLoyaltyFromItem(stack));
        this.entityData.set(ENCHANTED, stack.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LOYALTY, (byte) 0);
        builder.define(ENCHANTED, false);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && owner != null) {
            int loyalty = this.entityData.get(LOYALTY);
            if (loyalty > 0 && !this.isOwnerAlive()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else if (loyalty > 0) {
                this.setNoPhysics(true);
                Vec3 motion = new Vec3(owner.getX() - this.getX(), owner.getEyeY() - this.getY(), owner.getZ() - this.getZ());
                this.setPos(this.getX(), this.getY() + motion.y * 0.015D * loyalty, this.getZ());
                if (this.level().isClientSide) {
                    this.yo = this.getY();
                }
                Vec3 velocity = this.getDeltaMovement().scale(0.95D).add(motion.normalize().scale(0.05D * loyalty));
                this.setDeltaMovement(velocity);
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.returnTimer;
            }
        }
        super.tick();
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity instanceof Player player) {
            return player.isAlive() && !player.isSpectator();
        }
        return entity != null && entity.isAlive();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        if (this.tridentStack == null) {
            return TacticalTridents.NETHERITE_TRIDENT.get().getDefaultInstance();
        };
        return this.tridentStack.copy();
    }

    public boolean isEnchanted() {
        return this.entityData.get(ENCHANTED);
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        float damage = 10.0F;
        Entity owner = this.getOwner();
        DamageSource source = this.damageSources().trident(this, owner == null ? this : owner);
        ServerLevel serverLevel = this.level() instanceof ServerLevel server ? server : null;

        if (serverLevel != null) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.tridentStack, target, source, damage);
        }

        this.dealtDamage = true;
        SoundEvent sound = SoundEvents.TRIDENT_HIT;
        float thunderSoundVolume = 1.0F;

        // Execute the actual damage punch to the target
        if (target.hurt(source, damage)) {
            if (target instanceof LivingEntity livingEntity && serverLevel != null) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, livingEntity, source);
            }
        }

        // Handle lightning strike logic
        if (serverLevel != null && serverLevel.isThundering() && this.hasChanneling()) {
            BlockPos pos = target.blockPosition();
            if (serverLevel.canSeeSky(pos)) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (lightning != null) {
                    lightning.moveTo(Vec3.atBottomCenterOf(pos));
                    lightning.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                    serverLevel.addFreshEntity(lightning);
                    sound = SoundEvents.TRIDENT_THUNDER.value();
                    thunderSoundVolume = 5.0F;
                }
            }
        }

        this.playSound(sound, thunderSoundVolume, 1.0F);

        // Bounce calculation: Stops the projectile from passing through bodies infinitely
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        Entity owner = this.getOwner();
        if (owner == null || owner.getUUID().equals(player.getUUID())) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Trident", 10)) {
            this.tridentStack = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("Trident")).result().orElse(ItemStack.EMPTY);
        }
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.entityData.set(LOYALTY, this.getLoyaltyFromItem(this.tridentStack));
        this.entityData.set(ENCHANTED, this.tridentStack.hasFoil());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Trident", ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, this.tridentStack).result().orElse(new CompoundTag()));
    }

    @Override
    protected void tickDespawn() {
        int loyalty = this.entityData.get(LOYALTY);
        if (this.pickup != Pickup.ALLOWED || loyalty <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            return (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, stack, this), 0, 127);
        }
        return 0;
    }

    private boolean hasChanneling() {
        if (this.level() instanceof ServerLevel serverLevel) {
            var enchantment = serverLevel.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.CHANNELING);
            return this.tridentStack.getEnchantmentLevel(enchantment) > 0;
        }
        return false;
    }
}