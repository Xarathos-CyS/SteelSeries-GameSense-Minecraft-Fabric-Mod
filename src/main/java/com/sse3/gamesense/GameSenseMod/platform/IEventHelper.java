package com.sse3.gamesense.GameSenseMod.platform;

import com.mojang.datafixers.util.Either;
import com.sse3.gamesense.GameSenseMod.api.events.ILoadLevelEvent;
import com.sse3.gamesense.GameSenseMod.api.events.IPlayerTickEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.WorldAccess;

public interface IEventHelper {
    Either<ILoadLevelEvent, WorldAccess> fireLoadLevelEvent(WorldAccess paramWorldAccess);

    Either<IPlayerTickEvent, LivingEntity> firePlayerTickEvent(LivingEntity paramLivingEntity);
}
