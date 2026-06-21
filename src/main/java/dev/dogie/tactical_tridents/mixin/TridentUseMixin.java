package dev.dogie.tactical_tridents.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.dogie.tactical_tridents.Helper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentUseMixin {
    @WrapOperation(method = "use", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    public boolean isInWaterOrRainUse(Player instance, Operation<Boolean> original, @Local ItemStack item) {
        boolean hasHydro = Helper.canUseHydro(instance, item);

        return original.call(instance) || hasHydro;
    }

    @WrapOperation(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    public boolean isInWaterOrRainRelease(Player instance, Operation<Boolean> original, @Local(argsOnly = true) ItemStack item) {
        boolean hasHydro = Helper.canUseHydro(instance, item);

        return original.call(instance) || hasHydro;
    }
}
