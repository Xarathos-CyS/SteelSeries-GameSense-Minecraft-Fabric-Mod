/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 */
package com.sse3.gamesense.GameSenseMod;

import com.sse3.gamesense.GameSenseMod.Constants;
import com.sse3.gamesense.GameSenseMod.EventReceiver;
import com.sse3.gamesense.GameSenseMod.SteelSeriesE3;
import com.sse3.gamesense.GameSenseMod.platform.Services;
import net.minecraft.client.MinecraftClient;

public class GameSenseModCommon {
    public static GameSenseModCommon instance;
    public EventReceiver receiver;
    public SteelSeriesE3 sse3;

    public void init(MinecraftClient mcInst) {
        this.sse3 = new SteelSeriesE3();
        this.receiver = new EventReceiver(mcInst, this);
        this.sse3.start();
        Constants.LOG.info("{} is now loaded on {}! we are currently in a {} environment!", new Object[]{"GameSenseMod", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName()});
    }

    public void dispose() {
        this.sse3.stop();
        this.sse3 = null;
        this.receiver = null;
    }
}

