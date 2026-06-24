package com.sse3.gamesense.sse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class SseEventListManipulator {
    private static final Gson GSON = new Gson();

    public static JsonArray convertToJsonList(List<SseEvent<?>> events) {
        JsonArray arr = new JsonArray();

        for (SseEvent<?> e : events) {
            JsonObject obj = new JsonObject();
            obj.addProperty("event", e.getEvent());

            JsonObject data = new JsonObject();
            Object val = e.getValue();

            if (val instanceof JsonElement) {
                data.add("value", (JsonElement) val);
            } else {
                JsonElement je = GSON.toJsonTree(val);
                data.add("value", je);
            }

            obj.add("data", data);
            arr.add(obj);
        }

        return arr;
    }
}