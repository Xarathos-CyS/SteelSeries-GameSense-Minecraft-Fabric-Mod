package com.sse3.gamesense;

import com.google.gson.JsonArray;
import com.sse3.gamesense.sse.SseEvent;
import com.sse3.gamesense.sse.SseEventListManipulator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class GameSenseClient implements ClientModInitializer {
    private float lastHealth = -1f;
    private long lastTickMS = 0L;
    private int lastFoodLevel = -1;
    private boolean lastIsHungry = false;
    private int lastAir = -1;
    private int lastDurability = -1;
    private String lastItemId = "";
    private Direction lastFacing = null;
    private boolean wasFocused = true;
    private boolean wasInWorld = false;

    @Override
    public void onInitializeClient() {
        System.out.println("GameSenseClient loaded!");
        EventHandler.setupBaseUrl();
        EventHandler.registerEvent("HEALTH", 0, 100);
        EventHandler.registerEvent("HUNGERLEVEL", 0, 100);
        EventHandler.registerEvent("AIRLEVEL", 0, 100);
        EventHandler.registerEvent("DURABILITY", 0, 100);
        EventHandler.registerEvent("DIRECTION", 0, 100);
        EventHandler.registerEvent("HUNGRY", 0, 1);
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(Minecraft mc) {
        if (mc.player == null || mc.level == null) return;

        long cur = System.currentTimeMillis();
        boolean forceUpdate = (cur - lastTickMS) > 5000L; //fallback
        List<SseEvent<?>> events = new ArrayList<>();

        //health
        float health = mc.player.getHealth();
        float maxHealth = mc.player.getMaxHealth();
        int healthPct = Math.round((health / maxHealth) * 100.0f);

        if (health != lastHealth || forceUpdate) {
            lastHealth = health;
            events.add(new SseEvent<Integer>("HEALTH", healthPct));
        }

        //hunger level
        int foodLevel = mc.player.getFoodData().getFoodLevel();

        if (foodLevel != lastFoodLevel || forceUpdate) {
            lastFoodLevel = foodLevel;

            int scaled = foodLevel * 5;
            events.add(new SseEvent<>("HUNGERLEVEL", scaled));
        }

        //hunger bool
        boolean isHungry = mc.player.getFoodData().needsFood();

        if (isHungry != lastIsHungry || forceUpdate) {
            lastIsHungry = isHungry;

            events.add(new SseEvent<>("HUNGRY", isHungry));
        }

        //air
        int air = mc.player.getAirSupply();

        if (air != lastAir || forceUpdate) {
            lastAir = air;

            int scaled = air / 3;
            events.add(new SseEvent<>("AIRLEVEL", scaled));
        }

        //durability
        var held = mc.player.getMainHandItem();

        if (held != null && held.isDamageableItem()) {
            int max = held.getMaxDamage();
            int dmg = held.getDamageValue();

            int durability = (int) (100 - ((dmg / (float) max) * 100));

            if (durability != lastDurability || forceUpdate) {
                lastDurability = durability;
                events.add(new SseEvent<>("TOOLDURABILITY", durability));
            }
        } else {
            if (lastDurability != 0 || forceUpdate) {
                lastDurability = 0;
                events.add(new SseEvent<>("TOOLDURABILITY", 0));
            }
        }

        //current held item
        ItemStack cheld = mc.player.getMainHandItem();

        String itemId = BuiltInRegistries.ITEM
                .getKey(cheld.getItem())
                .toString();

        boolean toolChanged = !itemId.equals(lastItemId);
        boolean localForce = toolChanged;

        String toolType = "NONE";
        String material = "NONE";

        if (itemId.contains("sword")) toolType = "SWORD";
        else if (itemId.contains("pickaxe")) toolType = "PICKAXE";
        else if (itemId.contains("axe")) toolType = "AXE";
        else if (itemId.contains("shovel")) toolType = "SHOVEL";
        else if (itemId.contains("hoe")) toolType = "HOE";
        else if (itemId.contains("shears")) toolType = "SHEARS";
        else if (itemId.contains("flint_and_steel")) toolType = "SHEARS";
        else if (itemId.contains("mace")) toolType = "AXE";
        else if (itemId.contains("spear")) toolType = "SHOVEL";
        else if (itemId.contains("trident")) toolType = "SWORD";

        if (itemId.contains("wooden")) material = "WOOD";
        else if (itemId.contains("stone")) material = "STONE";
        else if (itemId.contains("iron")) material = "IRON";
        else if (itemId.contains("golden")) material = "GOLD";
        else if (itemId.contains("diamond")) material = "EMERALD";
        else if (itemId.contains("netherite")) material = "STONE";
        else if (itemId.contains("copper")) material = "WOOD";

        if (itemId.contains("trident")) material = "EMERALD";
        if (itemId.contains("mace")) material = "IRON";
        if (itemId.equals("minecraft:shears")) material = "IRON";
        if (itemId.equals("minecraft:flint_and_steel")) material = "IRON";

        if (toolChanged || forceUpdate) {
            lastItemId = itemId;

            if (!toolType.equals("NONE")) {
                events.add(new SseEvent<>("TOOL", toolType));
                events.add(new SseEvent<>("TOOLMATERIAL", material));
                events.add(new SseEvent<>("SHOWTOOL", 0));
                events.add(new SseEvent<>("SHOWTOOL", 1));
            } else {
                events.add(new SseEvent<>("TOOL", "NONE"));
                events.add(new SseEvent<>("TOOLDURABILITY", 0));
                events.add(new SseEvent<>("SHOWTOOL", 0));
                events.add(new SseEvent<>("SHOWTOOL", 1));
            }
        }

        //facing direction
        Direction facing = mc.player.getDirection();

        boolean facingChanged = (lastFacing == null) || !facing.equals(lastFacing);

        if (facingChanged || forceUpdate) {
            lastFacing = facing;

            String facingStr = facing.toString().toUpperCase();

            events.add(new SseEvent<>("FACING", facingStr));
        }

        //window focus
        boolean inWorld = mc.player != null && mc.level != null;
        boolean isFocused = mc.isWindowActive();


        if (inWorld && !wasInWorld) {
            EventHandler.sendSimpleEvent("START", 1);
            lastTickMS = 0;
        }

        if (!inWorld && wasInWorld) {
            EventHandler.sendSimpleEvent("FINISH", 1);
        }

        if (isFocused && !wasFocused) {
            EventHandler.sendSimpleEvent("START", 1);
            lastTickMS = 0;
        }

        if (!isFocused && wasFocused) {
            EventHandler.sendSimpleEvent("FINISH", 1);
        }

        wasInWorld = inWorld;
        wasFocused = isFocused;

        if (!inWorld || !isFocused) {
            return;
        }

        if (!events.isEmpty() || forceUpdate || localForce) {
            lastTickMS = cur;
            JsonArray arr = SseEventListManipulator.convertToJsonList(events);
            EventHandler.sendGameEvents(arr);
        }
    }
}