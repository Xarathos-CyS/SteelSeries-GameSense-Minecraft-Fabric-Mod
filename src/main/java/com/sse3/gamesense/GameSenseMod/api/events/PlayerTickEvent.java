package com.sse3.gamesense.GameSenseMod.api.events;

import net.minecraft.entity.LivingEntity;

public class PlayerTickEvent implements IPlayerTickEvent {
    private final LivingEntity player;

    public PlayerTickEvent(LivingEntity player) {
        this.player = player;
    }

    @Override
    public LivingEntity getPlayer() {
        return this.player;
    }
}
