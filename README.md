# SteelSeries GameSense Minecraft Mod - for Fabric (Modern rewrite)

A modern rewrite of the SteelSeries GameSense Minecraft integration for 1.21.11+.
This mod is based on the [SteelSeries GameSense SDK](https://github.com/SteelSeries/gamesense-sdk),
originally published on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).

- This mod only runs on [Fabric](https://github.com/FabricMC) and requires [Fabric API](https://github.com/FabricMC/fabric-api) to run.
- This version is a complete rewrite from scratch to work with SteelSeries GameSense API Through the SteelSeries GG application.
---

## Downloads

- You can get the ready mod file from [Curseforge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric-continued),
- Or from the [releases](https://github.com/Xarathos-CyS/SteelSeries-GameSense-Minecraft-Fabric-Mod/releases) page.

---

## Why Rewrite?
- 1.21.11 update removed many integral libraries and modules the previous versions relied on.
- Complete working sourcecode was not available and the previous versions were incomplete reverse engineered implementations.
- The transition to Mojang's official mappings in the 1.21.11 update caused many problems with the previous versions.
- Code fixes were practically impossible due to the immense reliance on obfuscated code from the previous versions.

---

## Changes and fixes:
- direct hooks to game events in contrast to time based polling for instant updates of the SteelSeries hardware.
- Removed unnecessary code and implementations that never worked in previous versions (e.g. sending time of day updates, attempting to display bow/fishing rod/crossbow).
- fixed a long lasting bug since the [original SDK implementation](https://github.com/SteelSeries/gamesense-sdk).
- Sending of game events through modern libraries and modules. 

## Tool display now works!
Since the 1.7 original mod was made by SteelSeries, displaying the current held item on keyboards did not work; the tool would only show up after unfocusing the game window/rejoining the world. It is now fixed! however, please bear the following in mind:

The items that can be displayed are statically present only on the SteelSeries GG application and cannot be altered unless SteelSeries updates the plugin. this means that every weapon beyond 1.8 (trident, mace, copper tools, spear, etc.) cannot be displayed on your keyboard.

To remedy that, I've either disabled the attempt to show some tools (shield, bow, crossbow, fishing rod) or likened the tools to others that look similar enough. Please refer to the [changelog](https://github.com/Xarathos-CyS/SteelSeries-GameSense-Minecraft-Fabric-Mod/blob/main/CHANGELOG) for the full changes.

---

## How to Build, Run and Use

*pre 1.21.11 versions are no longer supported. consider their source code for reference only.

1. Use `./gradlew build` in project root to build, output mod jar will be in `/build/libs/`.
*The provided source files are free of cache or generated data.
2. Drop into your mods folder and launch the game.
3. You will need the SteelSeries GG application to use this mod. navigate to `"Engine"` then to `"Apps"` and turn on the `"Gamesense Minecraft Mod"` plugin, ensure to `configure` your Steelseries hardware and it will show game statuses based on that!

---

## Credits / History

- Original GameSense integration by [SteelSeries](https://github.com/SteelSeries).
- Community implementations (Forge / early Fabric) inspired this project.
- Special thanks to [JayJay1989BE](https://github.com/JayJay1989) for maintaining a Fabric version up to 1.21.5.

Note: Versions prior to 1.21.11 in this repository are based on earlier community work.
The 1.21.11+ version is a full rewrite with no reused code.

**For any issues/requests [add a comment or create a new issue here](https://github.com/Xarathos-CyS/SteelSeries-GameSense-Minecraft-Fabric-Mod/issues/1).**
