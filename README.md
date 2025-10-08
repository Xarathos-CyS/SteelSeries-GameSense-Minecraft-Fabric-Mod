# SteelSeries GameSense Minecraft (Fabric Mod)

This mod is based on the [SteelSeries GameSense SDK](https://github.com/SteelSeries/gamesense-sdk),  
forked from [JayJay1989BE's Fabric implementation](https://github.com/nevoka-be/gamesense-sdk),  
originally published on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).

- The original was a Forge-only mod by [SteelSeries](https://github.com/SteelSeries).  
- This version is a continuation and update for [Fabric](https://github.com/FabricMC).

---

## Downloads

- You can get the ready `.jar` from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric-continued),
- Or in `.zip` form in [releases](https://github.com/Xarathos-CyS/SteelSeries-GameSense-Minecraft-Fabric-Mod/releases/tag/1.21.x).

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
The mod itself is working; check `Eventreciever.java` for console output confirming tool change events.  
The issue may reside in the **HTTP requests** sent by the mod.  
I may investigate this further upon request.

---

## How to Build & Run

This mod relies on all files from [JayJay1989BE’s Fabric mod](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).  
To compile:
1. Use **Gradle** to build the project, ensure structure is under src/main/ and use the [Fabric Template Mod Generator](https://fabricmc.net/develop/template/) for a proper dev env.
2. Rename the `.jar` to `.rar` or `.zip`
3. Replace only `Eventreciever.java` in JayJay1989BE’s mod source, if the build was successful it should be a `.class` extension.
4. Rename back to `.jar`.  
5. Drop into your mods folder and launch the game.

---

### Credits
- [SteelSeries](https://github.com/SteelSeries) — Original GameSense SDK  
- [JayJay1989BE](https://github.com/nevoka-be) — Fabric adaptation  
- [Xarathos-CyS](https://github.com/Xarathos-CyS) — Update to 1.21.x, event system improvements

**For any issues/requests [add a comment or create a new issue here](https://github.com/Xarathos-CyS/SteelSeries-GameSense-Minecraft-Fabric-Mod/issues/1)**
