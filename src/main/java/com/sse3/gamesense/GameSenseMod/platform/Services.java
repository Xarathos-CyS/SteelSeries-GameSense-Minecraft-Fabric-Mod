/*
 * Decompiled with CFR 0.152.
 */
package com.sse3.gamesense.GameSenseMod.platform;

import com.sse3.gamesense.GameSenseMod.Constants;
import com.sse3.gamesense.GameSenseMod.platform.IEventHelper;
import com.sse3.gamesense.GameSenseMod.platform.services.IPlatformHelper;
import java.util.ServiceLoader;

public class Services {
    public static final IEventHelper EVENT = Services.load(IEventHelper.class);
    public static final IPlatformHelper PLATFORM = Services.load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}

