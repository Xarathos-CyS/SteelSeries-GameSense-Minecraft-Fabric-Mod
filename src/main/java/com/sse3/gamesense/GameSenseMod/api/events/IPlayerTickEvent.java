package com.sse3.gamesense.GameSenseMod.api.events;

import net.minecraft.entity.LivingEntity;

public interface IPlayerTickEvent {
    LivingEntity getPlayer();
}
