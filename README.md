# SteelSeries GameSense Minecraft (Fabric Mod)

This mod is based on the [SteelSeries GameSense SDK](https://github.com/SteelSeries/gamesense-sdk),  
forked from [JayJay1989BE's Fabric implementation](https://github.com/nevoka-be/gamesense-sdk),  
originally published on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).

- The original was a Forge-only mod by [SteelSeries](https://github.com/SteelSeries).  
- This version is a continuation and update for [Fabric](https://github.com/FabricMC).

---

## Compiling Notes

The provided source code is **de-obfuscated** and may have issues running,  
even after a successful build using [Gradle](https://github.com/gradle/gradle).  
Please consider the source code **for reference only**.

Main changes:
- Modified `src/main/java/com/sse3/gamesense/GameSenseMod/Eventreciever.java`, which contains the core logic.
- The original and JayJay1989BE’s implementation used **1-second polling**, which caused slow and inefficient updates in Fabric.
- This version hooks directly into game events, allowing **instant updates**.
- Updated support from **Minecraft 1.20.x → 1.21.x**.

---

## Known Issue

From the SteelSeries SDK side, there’s a problem updating the material of the currently held tool.  
The mod itself is working — check `Eventreciever.java` for console output confirming tool change events.  
The issue may reside in the **HTTP requests** sent by the mod.  
I may investigate this further upon request.

---

## How to Build & Run

This mod relies on all files from [JayJay1989BE’s Fabric mod](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).  
To compile:
1. Use **Gradle** to build the project.  
2. Replace only `Eventreciever.java` in JayJay1989BE’s mod source.  
3. A ready-to-use `.jar` is available on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-mod-fabric-fork).

---

### Credits
- [SteelSeries](https://github.com/SteelSeries) — Original GameSense SDK  
- [JayJay1989BE](https://github.com/nevoka-be) — Fabric adaptation  
- [Xarathos-CyS](https://github.com/Xarathos-CyS) — Update to 1.21.x, event system improvements
