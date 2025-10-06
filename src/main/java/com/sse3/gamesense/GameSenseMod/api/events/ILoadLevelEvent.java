package com.sse3.gamesense.GameSenseMod.api.events;

/**
 * Using Object here avoids depending on a concrete Level class at compile-time.
 * The runtime code still passes the real level object; callers can cast it if needed.
 */
public interface ILoadLevelEvent {
    Object getLevel();
}
