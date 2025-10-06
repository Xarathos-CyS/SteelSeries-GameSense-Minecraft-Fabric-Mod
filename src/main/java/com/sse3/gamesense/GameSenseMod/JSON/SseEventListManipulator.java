/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package com.sse3.gamesense.GameSenseMod.JSON;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sse3.gamesense.GameSenseMod.JSON.SseEvent;
import java.util.List;

public class SseEventListManipulator {
    public static <T> JsonArray convertToJsonList(List<SseEvent<?>> events) {
        Gson gson = new Gson();
        JsonArray eventsArray = new JsonArray();
        for (SseEvent<?> event : events) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("event", event.event);
            JsonObject dataObject = new JsonObject();
            dataObject.add("value", gson.toJsonTree(event.value));
            jsonObject.add("data", (JsonElement)dataObject);
            eventsArray.add((JsonElement)jsonObject);
        }
        return eventsArray;
    }
}

