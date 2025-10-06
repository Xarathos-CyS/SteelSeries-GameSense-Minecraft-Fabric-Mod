package com.sse3.gamesense.GameSenseMod.mixin;

import com.sse3.gamesense.GameSenseMod.GameSenseModCommon;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public class MinecraftMixin {
    @Inject(at = {@At("HEAD")}, method = {"close"})
    private void close(CallbackInfo info) {
        GameSenseModCommon.instance.dispose();
    }
}
