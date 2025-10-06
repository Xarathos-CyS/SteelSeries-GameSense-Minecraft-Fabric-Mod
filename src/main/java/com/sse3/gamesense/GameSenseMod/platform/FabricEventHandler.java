package com.sse3.gamesense.GameSenseMod.platform;

import com.mojang.datafixers.util.Either;
import com.sse3.gamesense.GameSenseMod.api.events.GameSenseEvents;
import com.sse3.gamesense.GameSenseMod.api.events.IEventHandler;
import com.sse3.gamesense.GameSenseMod.api.events.ILoadLevelEvent;
import com.sse3.gamesense.GameSenseMod.api.events.IPlayerTickEvent;
import com.sse3.gamesense.GameSenseMod.api.events.LoadLevelEvent;
import com.sse3.gamesense.GameSenseMod.api.events.PlayerTickEvent;
import com.sse3.gamesense.GameSenseMod.platform.IEventHelper;
import com.sse3.gamesense.GameSenseMod.platform.Services;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.WorldAccess;

public class FabricEventHandler implements IEventHelper {
    @Override
    public Either<ILoadLevelEvent, WorldAccess> fireLoadLevelEvent(WorldAccess level) {
        LoadLevelEvent event = new LoadLevelEvent(level);
        if (Services.PLATFORM.isModLoaded("fabric")) {
            ((IEventHandler) GameSenseEvents.LOADLEVEL_EVENT.invoker()).handle(event);
        }
        // Explicit cast to satisfy type inference
        return Either.right((WorldAccess) event.getLevel());
    }

    @Override
    public Either<IPlayerTickEvent, LivingEntity> firePlayerTickEvent(LivingEntity player) {
        PlayerTickEvent event = new PlayerTickEvent(player);
        if (Services.PLATFORM.isModLoaded("fabric")) {
            ((IEventHandler) GameSenseEvents.PLAYERTICK_EVENT.invoker()).handle(event);
        }
        // Explicit cast to satisfy type inference
        return Either.right((LivingEntity) event.getPlayer());
    }
}
