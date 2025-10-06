/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.event.Event
 *  net.fabricmc.fabric.api.event.EventFactory
 */
package com.sse3.gamesense.GameSenseMod.api.events;

import com.sse3.gamesense.GameSenseMod.api.events.IEventHandler;
import com.sse3.gamesense.GameSenseMod.api.events.LoadLevelEvent;
import com.sse3.gamesense.GameSenseMod.api.events.PlayerTickEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class GameSenseEvents {
    public static final Event<IEventHandler<LoadLevelEvent>> LOADLEVEL_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        for (IEventHandler listener : listeners) {
            listener.handle(event);
        }
    });
    public static final Event<IEventHandler<PlayerTickEvent>> PLAYERTICK_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        for (IEventHandler listener : listeners) {
            listener.handle(event);
        }
    });
}

