package com.sse3.gamesense;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventHandler {
    private static String BASE_URL = "http://127.0.0.1:3000"; //fallback, may never run.
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "gamesense-http");
        t.setDaemon(true);
        return t;
    });

    private static void postSync(String endpoint, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("GameSense POST failed: " + e.getClass().getName() + " " + e.getMessage());
        }
    }

    private static void postAsync(String endpoint, String json) {
        EXEC.submit(() -> postSync(endpoint, json));
    }

    public static void sendGameEvents(JsonArray eventsArray) {
        JsonObject root = new JsonObject();
        root.addProperty("game", "SSMCMOD");
        root.add("events", eventsArray);

        String json = GSON.toJson(root);
        postAsync("/multiple_game_events", json);
    }

    public static void setupBaseUrl() {

        try {
            String corePropsPath = System.getenv("PROGRAMDATA") + "\\SteelSeries\\SteelSeries Engine 3\\coreProps.json";
            File f = new File(corePropsPath);

            if (!f.exists()) {
                corePropsPath = "/Library/Application Support/SteelSeries Engine 3/coreProps.json";
                f = new File(corePropsPath);
            }

            if (f.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String line = reader.readLine();
                reader.close();

                JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                BASE_URL = "http://" + obj.get("address").getAsString();
                System.out.println("GameSense: Using SteelSeries Engine at " + BASE_URL);
            } else {
                System.out.println("GameSense: coreProps.json not found, using fallback " + BASE_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("GameSense: Failed to read coreProps.json, using fallback " + BASE_URL);
        }
    }

    public static void registerEvent(String name, int min, int max) {
        JsonObject j = new JsonObject();
        j.addProperty("game", "SSMCMOD");
        j.addProperty("event", name);
        j.addProperty("min_value", min);
        j.addProperty("max_value", max);

        postAsync("/register_game_event", GSON.toJson(j));
    }

    public static void sendSimpleEvent(String event, int value) {
        JsonObject obj = new JsonObject();
        obj.addProperty("game", "SSMCMOD");
        obj.addProperty("event", event);

        JsonObject data = new JsonObject();
        data.addProperty("value", value);

        obj.add("data", data);

        postAsync("/game_event", GSON.toJson(obj));
    }
}