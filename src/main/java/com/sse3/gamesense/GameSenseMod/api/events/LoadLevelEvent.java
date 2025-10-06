package com.sse3.gamesense.GameSenseMod.api.events;

/**
 * Using Object here avoids depending on a concrete Level class at compile-time.
 */
public class LoadLevelEvent implements ILoadLevelEvent {
    private final Object level;

    public LoadLevelEvent(Object level) {
        this.level = level;
    }

    @Override
    public Object getLevel() {
        return this.level;
    }
}
