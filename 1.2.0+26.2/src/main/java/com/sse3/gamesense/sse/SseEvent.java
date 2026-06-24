package com.sse3.gamesense.sse;

public class SseEvent<T> {
    private final String event;
    private final T value;

    public SseEvent(String event, T value) {
        this.event = event;
        this.value = value;
    }

    public String getEvent() { return event; }
    public T getValue() { return value; }
}