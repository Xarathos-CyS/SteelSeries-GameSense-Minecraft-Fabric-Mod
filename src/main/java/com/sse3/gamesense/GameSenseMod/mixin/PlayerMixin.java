package com.sse3.gamesense.GameSenseMod.mixin;

import com.sse3.gamesense.GameSenseMod.platform.Services;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        if ((Object)this instanceof ClientPlayerEntity player && player.getEntityWorld().isClient()) {
            Services.EVENT.firePlayerTickEvent(player);
        }
    }
}
