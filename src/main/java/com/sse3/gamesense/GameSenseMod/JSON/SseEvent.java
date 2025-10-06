/*
 * Decompiled with CFR 0.152.
 */
package com.sse3.gamesense.GameSenseMod.JSON;

public class SseEvent<T> {
    public String event;
    public T value;

    public SseEvent(String event, T value) {
        this.event = event;
        this.value = value;
    }
}

