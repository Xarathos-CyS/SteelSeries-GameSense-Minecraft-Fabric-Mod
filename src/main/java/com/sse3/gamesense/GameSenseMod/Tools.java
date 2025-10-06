package com.sse3.gamesense.GameSenseMod;

import java.util.Arrays;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class Tools {
    public static final List<Item> WOOD_TOOLS = Arrays.asList(new Item[] { Items.WOODEN_SWORD, Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.BOW, Items.CROSSBOW, Items.FISHING_ROD });

    public static final List<Item> STONE_TOOLS = Arrays.asList(new Item[] { Items.STONE_SWORD, Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SHOVEL, Items.STONE_HOE });

    public static final List<Item> IRON_TOOLS = Arrays.asList(new Item[] { Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.SHEARS, Items.SHIELD, Items.FLINT_AND_STEEL });

    public static final List<Item> GOLD_TOOLS = Arrays.asList(new Item[] { Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE });

    public static final List<Item> DIAMOND_TOOLS = Arrays.asList(new Item[] { Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE });

    public static final List<Item> NETHERITE_TOOLS = Arrays.asList(new Item[] { Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE, Items.MACE });

    public static String getItemName(Item item) {
        String itemName = item.getTranslationKey().replace("item.minecraft.", "");
        switch (itemName) {
            case "fishing_rod":
                return "FISHINGROD";
            case "crossbow":
                return "CROSSBOW";
            case "bow":
                return "BOW";
            case "shield":
                return "SHIELD";
            case "shears":
                return "SHEARS";
            case "flint_and_steel":
                return "FLINTANDSTEEL";
            case "mace":
                return "MACE";
        }
        itemName = itemName.replaceFirst("wooden_|stone_|iron_|golden_|diamond_|netherite_", "");
        switch (itemName) {
            case "sword":
            case "axe":
            case "pickaxe":
            case "shovel":
            case "hoe":
            case "fishingrod":
            case "crossbow":
            case "bow":
            case "shield":
            case "shears":
            case "flintandsteel":
            case "mace":

        }
        return

                "NONE";
    }
}
