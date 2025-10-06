package com.sse3.gamesense.GameSenseMod;

import com.google.gson.JsonArray;
import com.sse3.gamesense.GameSenseMod.JSON.SseEvent;
import com.sse3.gamesense.GameSenseMod.JSON.SseEventListManipulator;
import com.sse3.gamesense.GameSenseMod.api.events.ILoadLevelEvent;
import com.sse3.gamesense.GameSenseMod.api.events.IPlayerTickEvent;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.LightType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.network.ClientPlayerEntity;

public class EventReceiver {
    private float lastHealth;

    private int lastFoodLevel;

    private boolean isHungry;

    private boolean isStarted;

    private final MinecraftClient _mcInst;

    private long lastTickMS;

    private int timeOfDay;

    private int lastAir;

    private Direction lastFacing;

    private ItemStack lastHeldItem;

    private final GameSenseModCommon gsmInst;

    public EventReceiver(MinecraftClient mcInst, GameSenseModCommon gsmInst) {
        this._mcInst = mcInst;
        this.gsmInst = gsmInst;
        reset();
    }

    public void reset() {
        this.lastHealth = 100.0F;
        this.lastFoodLevel = 100;
        this.isHungry = false;
        this.isStarted = false;
        this.lastTickMS = 0L;
        this.timeOfDay = 0;
        this.lastAir = 100;
        this.lastFacing = Direction.NORTH;
        this.lastHeldItem = null;
    }

    public void onLivingUpdate(IPlayerTickEvent event) {
        if (!this.isStarted) {
            return;
        }

        long curElapsed = System.currentTimeMillis() - this.lastTickMS;
        long fallbackThreshold = 5000L;
        boolean forceUpdate = (curElapsed > fallbackThreshold);

        ArrayList events = new ArrayList();

        LivingEntity maybePlayer = event.getPlayer();
        if (maybePlayer instanceof ClientPlayerEntity) {
            ClientPlayerEntity player = (ClientPlayerEntity) maybePlayer;

            // Health
            if (player.getHealth() != this.lastHealth || forceUpdate) {
                this.lastHealth = player.getHealth();
                float maxHealth = player.getMaxHealth();
                events.add(new SseEvent<Integer>("HEALTH",
                        100 * (int) this.lastHealth / (int) maxHealth));
            }

            // Hunger level
            int foodLevel = player.getHungerManager().getFoodLevel();
            if (foodLevel != this.lastFoodLevel || forceUpdate) {
                this.lastFoodLevel = foodLevel;
                events.add(new SseEvent<Integer>("HUNGERLEVEL", this.lastFoodLevel * 5));
            }

            // Hunger status
            boolean hungry = player.getHungerManager().isNotFull();
            if (hungry != this.isHungry || forceUpdate) {
                this.isHungry = hungry;
                events.add(new SseEvent<Boolean>("HUNGRY", this.isHungry));
            }

            // Air
            int air = player.getAir();
            if (air != this.lastAir || forceUpdate) {
                this.lastAir = air;
                events.add(new SseEvent<Integer>("AIRLEVEL", this.lastAir / 3));
            }

            // Facing direction
            Direction facing = player.getHorizontalFacing();
            if (facing != this.lastFacing || forceUpdate) {
                this.lastFacing = facing;
                events.add(new SseEvent<String>("FACING", this.lastFacing.toString().toUpperCase()));
            }

            // Held item
            ItemStack held = player.getMainHandStack();
            Item heldItem = held.getItem();

            // Get registry name
            String heldItemId = heldItem.getTranslationKey();
            String lastHeldItemId = (this.lastHeldItem != null) ? this.lastHeldItem.getItem().getTranslationKey() : "";

            if (!heldItemId.equals(lastHeldItemId) || forceUpdate) {
                this.lastHeldItem = held.copy();

                int heldItemDurability = (int)(100.0 - this.getDurabilityForDisplay(held) * 100.0);
                String heldItemMaterialName = this.getMaterialForTool(heldItem);
                String heldItemType = Tools.getItemName(heldItem);

                Constants.LOG.info("Detected tool change: {} -> {}", lastHeldItemId, heldItemId);

                if (!heldItemType.equals("NONE")) {
                    events.add(new SseEvent<String>("TOOL", heldItemType));
                    events.add(new SseEvent<String>("TOOLMATERIAL", heldItemMaterialName));
                    events.add(new SseEvent<Integer>("TOOLDURABILITY", heldItemDurability));
                    events.add(new SseEvent<Integer>("SHOWTOOL", 1));
                } else {
                    events.add(new SseEvent<String>("TOOL", "NONE"));
                    events.add(new SseEvent<Integer>("TOOLDURABILITY", 0));
                    events.add(new SseEvent<Integer>("SHOWTOOL", 1));
                }
            }

            // Time of day
            int dayPercentage = this.getDayPercentage((World) this._mcInst.world, player.getBlockPos());
            if (dayPercentage != this.timeOfDay || forceUpdate) {
                this.timeOfDay = dayPercentage;
                events.add(new SseEvent<Integer>("TIMEOFDAY", this.timeOfDay));
            }

            if (!events.isEmpty() || forceUpdate) {
                try {
                    if (this.gsmInst != null && this.gsmInst.sse3 != null) {
                        JsonArray dataObject = null;
                        try {
                            dataObject = SseEventListManipulator.convertToJsonList(events);
                        } catch (Exception e) {
                            Constants.LOG.error("Failed to convert events to JSON", e);
                        }

                        if (dataObject != null) {
                            try {
                                this.gsmInst.sse3.sendGameEvents(dataObject, (PlayerEntity) player);
                            } catch (Exception e) {
                                Constants.LOG.error("Failed to send game events to SteelSeries", e);
                                this.isStarted = false;
                            }
                        }
                    } else {
                        Constants.LOG.warn("SteamSeries client (sse3) not available; skipping send");
                    }
                } finally {
                    this.lastTickMS = System.currentTimeMillis();
                }
            }
        }
    }

    private double getDurabilityForDisplay(ItemStack stack) {
        if (stack == null) return 0.0;
        try {
            double max = stack.getMaxDamage();
            if (max <= 0) return 0.0;
            return (double) stack.getDamage() / max;
        } catch (Exception e) {
            Constants.LOG.error("Error computing durability", e);
            return 0.0;
        }
    }


    public void onLevelLoad(ILoadLevelEvent event) {
        try {
            if (this.gsmInst != null && this.gsmInst.sse3 != null) {
                try {
                    this.gsmInst.sse3.sendGameEvent("START", 1, null);
                } catch (Exception e) {
                    Constants.LOG.error("Failed to send START game event", e);
                }
            } else {
                Constants.LOG.warn("gsmInst or sse3 null on level load");
            }
        } finally {
            this.isStarted = true;
        }
    }


    private String getMaterialForTool(Item item) {
        if (Tools.WOOD_TOOLS.contains(item))
            return "WOOD";
        if (Tools.STONE_TOOLS.contains(item))
            return "STONE";
        if (Tools.IRON_TOOLS.contains(item))
            return "IRON";
        if (Tools.GOLD_TOOLS.contains(item))
            return "GOLD";
        if (Tools.DIAMOND_TOOLS.contains(item))
            return "EMERALD";
        if (Tools.NETHERITE_TOOLS.contains(item))
            return "EMERALD";
        return "NONE";
    }

    private int getDayPercentage(World level, BlockPos blockPos) {
        if (level == null)
            return 0;
        int i = level.getLightLevel(LightType.SKY, blockPos) - level.getAmbientDarkness();
        float f = level.getSkyAngleRadians(1.0F);
        if (i > 0) {
            float g = (f < 3.1415927F) ? 0.0F : 6.2831855F;
            f += (g - f) * 0.2F;
            i = Math.round(i * MathHelper.cos(f));
        }
        i = 100 - i;
        i = MathHelper.clamp(i, 0, 100);
        return i;
    }
}