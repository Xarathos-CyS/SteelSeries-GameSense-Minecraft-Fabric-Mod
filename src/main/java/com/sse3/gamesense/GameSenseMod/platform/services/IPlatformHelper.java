/*
 * Decompiled with CFR 0.152.
 */
package com.sse3.gamesense.GameSenseMod.platform.services;

public interface IPlatformHelper {
    public String getPlatformName();

    public boolean isModLoaded(String var1);

    public boolean isDevelopmentEnvironment();

    default public String getEnvironmentName() {
        return this.isDevelopmentEnvironment() ? "development" : "production";
    }
}

