package com.sse3.gamesense.GameSenseMod.mixin;

import com.sse3.gamesense.GameSenseMod.platform.Services;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientWorld.class})
public class ClientLevelMixin {
    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    public void init(ClientPlayNetworkHandler connection, ClientWorld.Properties levelData, RegistryKey<World> dimension, RegistryEntry<DimensionType> dimensionTypeRegistration, int viewDistance, int serverSimulationDistance, WorldRenderer levelRenderer, boolean isDebug, long biomeZoomSeed, int seaLevel, CallbackInfo ci) {
        ClientLevelMixin clientLevelMixin = this;
        if (clientLevelMixin instanceof WorldAccess) {
            WorldAccess level = (WorldAccess)clientLevelMixin;
            Services.EVENT.fireLoadLevelEvent(level);
        }
    }
}
