# SteelSeries-GameSense-Minecraft-Fabric-Mod
This is based on [SteelSeries GameSense SDK](https://github.com/SteelSeries/gamesense-sdk) forked from [JayJay1989BE's fabric implememtation](https://github.com/nevoka-be/gamesense-sdk) at [his curseforge page](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric).
- Original was a forge only mod by [SteelSeries](https://github.com/SteelSeries/).
- This mod is ported to [Fabric](https://github.com/fabricmc).
# Compiling issues
Provided source-code is de-obfuscated and has issues running even after successful build using [Gradle](https://github.com/gradle/gradle). consider source-code only for viewing.
- Only real change is **src\main\java\com\sse3\gamesense\GameSenseMod\Eventreciever.java**, which holds all real important logic.
- Original + JayJay1989BE's implementation had 1 second polling, which in fabric broke and was too slow and inefficient; all inside **Eventreciever.java**. file has been fixed to hook directly to game events and update instantly.
- JayJay1989BE's mod supported till 1.20.x, this has been updated to 1.21.x
# Unsolvables
From steelseries' end, exists a problem updating material of current held tools, i ensured mod is working;
* check **Eventreciever.java** will output tool change to console
Issue may reside in the HTTP requests sent by the mod, i may look further upon request if prompted.
# To run
This mod uses all files from [JayJay1989BE's mod](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-fabric), modification of source files to include Gradle scheme can be implemented.
- compile using gradle, and only replace **Eventreciever.java** in JayJay1989BE's mod.
- ready mod file is included on [curseforge](https://www.curseforge.com/minecraft/mc-mods/steelseries-gamesense-mod-fabric-fork)
