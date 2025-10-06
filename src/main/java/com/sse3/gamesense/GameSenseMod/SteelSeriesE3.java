package com.sse3.gamesense.GameSenseMod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sse3.gamesense.GameSenseMod.Constants;
import com.sse3.gamesense.GameSenseMod.queue.QueueListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

public class SteelSeriesE3 {
    private CloseableHttpClient sseClient = null;
    private HttpPost ssePost = null;
    private String sse3Address = "";
    private Boolean isConnected = false;
    private long lastTick = 0L;
    private final QueueListener queue = QueueListener.getInstance();
    private String url;

    public void sendGameEvent(String eventName, int data, PlayerEntity player) {
        JsonObject eventData = new JsonObject();
        eventData.addProperty("value", (Number) data);
        this.sendGameEvent(eventName, eventData, player);
    }

    public void sendGameEvent(String eventName, Boolean data, PlayerEntity player) {
        JsonObject eventData = new JsonObject();
        eventData.addProperty("value", data);
        this.sendGameEvent(eventName, eventData, player);
    }

    public void sendGameEvent(String eventName, String data, PlayerEntity player) {
        JsonObject eventData = new JsonObject();
        eventData.addProperty("value", data);
        this.sendGameEvent(eventName, eventData, player);
    }

    public void sendGameEvent(String eventName, JsonObject dataObject, PlayerEntity player) {
        JsonObject event = new JsonObject();
        event.addProperty("game", "SSMCMOD");
        event.addProperty("event", eventName);
        event.add("data", (JsonElement) dataObject);
        System.out.println("Sending " + new Gson().toJson((JsonElement) event));
        this.queue.enqueue(() -> this.executePost(event.toString(), player, false));
    }

    public void sendGameEvents(JsonArray dataObject, PlayerEntity player) {
        JsonObject event = new JsonObject();
        event.addProperty("game", "SSMCMOD");
        event.add("events", (JsonElement) dataObject);
        System.out.println("Sending " + new Gson().toJson((JsonElement) event));
        this.queue.enqueue(() -> this.executePost(event.toString(), player, true));
    }

    private void executePost(String urlParameters, PlayerEntity player, boolean isMulti) {
        try {
            if (!this.isConnected.booleanValue()) {
                if (System.currentTimeMillis() - this.lastTick < 5000L) {
                    return;
                }
                this.lastTick = System.currentTimeMillis();
            }
            this.isConnected = true;
            try {
                if (isMulti) {
                    this.ssePost.setURI(new URI("http://" + this.url + "/multiple_game_events"));
                } else {
                    this.ssePost.setURI(new URI("http://" + this.url + "/game_event"));
                }
            } catch (Exception exception) {
                // empty catch block
            }
            StringEntity se = new StringEntity(urlParameters);
            se.setContentType((Header) new BasicHeader("Content-Type", "application/json"));
            this.ssePost.setEntity((HttpEntity) se);
            CloseableHttpResponse response = this.sseClient.execute((HttpUriRequest) this.ssePost);
            if (response != null) {
                this.ssePost.reset();
            }
        } catch (ConnectTimeoutException e) {
            this.isConnected = false;
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    private void connectToSSE3() {
        BufferedReader coreProps;
        Object corePropsFileName;
        String log = "Opened coreprops.json and read: {}";
        String jsonAddress = "";
        try {
            corePropsFileName = System.getenv("PROGRAMDATA") + "\\SteelSeries\\SteelSeries Engine 3\\coreProps.json";
            coreProps = new BufferedReader(new FileReader((String) corePropsFileName));
            jsonAddress = coreProps.readLine();
            Constants.LOG.info(log, (Object) jsonAddress);
            coreProps.close();
        } catch (FileNotFoundException e) {
            Constants.LOG.error("coreprops.json not found (Mac check)");
        } catch (IOException e) {
            Constants.LOG.error("Something terrible happened looking for coreProps.json", (Throwable) e);
        }
        if (jsonAddress == "") {
            try {
                corePropsFileName = "/Library/Application Support/SteelSeries Engine 3/coreProps.json";
                coreProps = new BufferedReader(new FileReader((String) corePropsFileName));
                jsonAddress = coreProps.readLine();
                Constants.LOG.info(log, (Object) jsonAddress);
                coreProps.close();
            } catch (FileNotFoundException e) {
                Constants.LOG.error("coreprops.json not found (Windows check)", (Throwable) e);
            } catch (IOException e) {
                Constants.LOG.error("Something terrible happened looking for coreProps.json", (Throwable) e);
            }
        }
        try {
            if (jsonAddress != "") {
                JsonObject jsonObject = JsonParser.parseString((String) jsonAddress).getAsJsonObject();
                // FIX: use get("address") not getName()
                if (jsonObject.has("address")) {
                    this.url = jsonObject.get("address").getAsString();
                } else {
                    this.url = jsonObject.has("coreServerAddress") ? jsonObject.get("coreServerAddress").getAsString() : "";
                }
                this.sse3Address = "http://" + this.url + "/game_event";
            } else {
                this.sse3Address = "http://localhost:3000/game_event";
            }
            this.sseClient = HttpClients.createDefault();
            RequestConfig sseReqCfg = RequestConfig.custom().setSocketTimeout(50).setConnectTimeout(10).setConnectionRequestTimeout(50).build();
            this.ssePost = new HttpPost(this.sse3Address);
            this.ssePost.setConfig(sseReqCfg);
        } catch (JsonParseException e) {
            Constants.LOG.error("Something terrible happened creating JSONObject from coreProps.json.", (Throwable) e);
        }
    }

    public void start() {
        this.queue.startListening();
        this.connectToSSE3();
    }

    public void stop() {
        try {
            Constants.LOG.info("Stopping the SteelSeries Engine client");
            this.sseClient.close();
            this.queue.stopListening();
        } catch (IOException iOException) {
            // empty catch block
        }
    }
}
