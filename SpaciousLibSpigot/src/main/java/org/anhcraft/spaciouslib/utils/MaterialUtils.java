package org.anhcraft.spaciouslib.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class MaterialUtils {
    /**
     * Serializes the given material data to string
     * @param md MaterialData object
     * @return a string represents for the object
     */
    public static String materialData2Str(MaterialData md){
        return md.getItemType().toString()+":"+md.getData();
    }

    /**
     * Deserialize the given string to its material data
     * @param s a string represents for the object
     * @return the material data
     */
    public static MaterialData str2MaterialData(String s){
        if(s == null || s.equalsIgnoreCase("null")){
            return new MaterialData(Material.AIR);
        }
        String[] x = s.split(":");
        Material mt;
        if(!GameVersion.is1_13Above() && StringUtils.isNumeric(x[0])) {
            mt = (Material) ReflectionUtils.getStaticMethod("getMaterial",
                    Material.class, new Group<>(
                            new Class<?>[]{int.class},
                            new Object[]{CommonUtils.toInteger(x[0])}
                    ));
        } else {
            mt = Material.valueOf(x[0].toUpperCase());
        }
        if(x.length == 2) {
            return new MaterialData(mt, (byte) Integer.parseInt(x[1]));
        } else {
            return new MaterialData(mt, (byte) 0);
        }
    }

    public static List<Material> getSkullTypes(){
        List<Material> m = new ArrayList<>();
        if(GameVersion.is1_13Above()){
            m.add(Material.CREEPER_HEAD);
            m.add(Material.DRAGON_HEAD);
            m.add(Material.PLAYER_HEAD);
            m.add(Material.ZOMBIE_HEAD);
            m.add(Material.SKELETON_SKULL);
            m.add(Material.WITHER_SKELETON_SKULL);
        } else {
            m.add(Material.valueOf("SKULL_ITEM"));
        }
        return m;
    }

    public static List<Material> getArmorTypes(){
        List<Material> m = new ArrayList<>();
        if(GameVersion.is1_9Above()) {
            m.add(Material.ELYTRA);
        }
        m.add(Material.LEATHER_HELMET);
        m.add(Material.LEATHER_CHESTPLATE);
        m.add(Material.LEATHER_LEGGINGS);
        m.add(Material.LEATHER_BOOTS);
        m.add(Material.CHAINMAIL_HELMET);
        m.add(Material.CHAINMAIL_CHESTPLATE);
        m.add(Material.CHAINMAIL_LEGGINGS);
        m.add(Material.CHAINMAIL_BOOTS);
        m.add(Material.IRON_HELMET);
        m.add(Material.IRON_CHESTPLATE);
        m.add(Material.IRON_LEGGINGS);
        m.add(Material.IRON_BOOTS);
        m.add(Material.DIAMOND_HELMET);
        m.add(Material.DIAMOND_CHESTPLATE);
        m.add(Material.DIAMOND_LEGGINGS);
        m.add(Material.DIAMOND_BOOTS);
        if(GameVersion.is1_13Above()){
            m.add(Material.GOLDEN_HELMET);
            m.add(Material.GOLDEN_CHESTPLATE);
            m.add(Material.GOLDEN_LEGGINGS);
            m.add(Material.GOLDEN_BOOTS);
        } else {
            m.add(Material.valueOf("GOLD_HELMET"));
            m.add(Material.valueOf("GOLD_CHESTPLATE"));
            m.add(Material.valueOf("GOLD_LEGGINGS"));
            m.add(Material.valueOf("GOLD_BOOTS"));
        }
        return m;
    }

    public static List<Material> getOreTypes(){
        List<Material> m = new ArrayList<>();
        m.add(Material.COAL_ORE);
        m.add(Material.GOLD_ORE);
        m.add(Material.DIAMOND_ORE);
        m.add(Material.IRON_ORE);
        m.add(Material.EMERALD_ORE);
        m.add(Material.LAPIS_ORE);
        m.add(Material.REDSTONE_ORE);
        if(GameVersion.is1_13Above()) {
            m.add(Material.NETHER_QUARTZ_ORE);
        } else{
            m.add(Material.valueOf("QUARTZ_ORE"));
        }
        return m;
    }

    public static List<Material> getBoatTypes(){
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_BOAT);
        m.add(Material.BIRCH_BOAT);
        m.add(Material.DARK_OAK_BOAT);
        m.add(Material.JUNGLE_BOAT);
        m.add(Material.OAK_BOAT);
        m.add(Material.SPRUCE_BOAT);
        m.add(Material.LEGACY_BOAT);
        return m;
    }

    public static List<Material> getButtonTypes(){
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_BUTTON);
        m.add(Material.BIRCH_BUTTON);
        m.add(Material.DARK_OAK_BUTTON);
        m.add(Material.JUNGLE_BUTTON);
        m.add(Material.OAK_BUTTON);
        m.add(Material.SPRUCE_BUTTON);
        m.add(Material.STONE_BUTTON);
        m.add(Material.LEGACY_STONE_BUTTON);
        m.add(Material.LEGACY_WOOD_BUTTON);
        return m;
    }

    public static List<Material> getDoorTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_DOOR);
        m.add(Material.ACACIA_TRAPDOOR);
        m.add(Material.BIRCH_DOOR);
        m.add(Material.BIRCH_TRAPDOOR);
        m.add(Material.DARK_OAK_DOOR);
        m.add(Material.DARK_OAK_TRAPDOOR);
        m.add(Material.IRON_DOOR);
        m.add(Material.IRON_TRAPDOOR);
        m.add(Material.JUNGLE_DOOR);
        m.add(Material.JUNGLE_TRAPDOOR);
        m.add(Material.OAK_DOOR);
        m.add(Material.OAK_TRAPDOOR);
        m.add(Material.SPRUCE_DOOR);
        m.add(Material.SPRUCE_TRAPDOOR);
        return m;
    }

    public static List<Material> getFenceTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_FENCE);
        m.add(Material.BIRCH_FENCE);
        m.add(Material.DARK_OAK_FENCE);
        m.add(Material.JUNGLE_FENCE);
        m.add(Material.NETHER_BRICK_FENCE);
        m.add(Material.OAK_FENCE);
        m.add(Material.SPRUCE_FENCE);
        return m;
    }

    public static List<Material> getFenceGateTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_FENCE_GATE);
        m.add(Material.BIRCH_FENCE_GATE);
        m.add(Material.DARK_OAK_FENCE_GATE);
        m.add(Material.JUNGLE_FENCE_GATE);
        m.add(Material.OAK_FENCE_GATE);
        m.add(Material.SPRUCE_FENCE_GATE);
        return m;
    }

    public static List<Material> getLeavesTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_LEAVES);
        m.add(Material.BIRCH_LEAVES);
        m.add(Material.DARK_OAK_LEAVES);
        m.add(Material.JUNGLE_LEAVES);
        m.add(Material.OAK_LEAVES);
        m.add(Material.SPRUCE_LEAVES);
        return m;
    }

    public static List<Material> getLogTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_LOG);
        m.add(Material.BIRCH_LOG);
        m.add(Material.DARK_OAK_LOG);
        m.add(Material.JUNGLE_LOG);
        m.add(Material.OAK_LOG);
        m.add(Material.SPRUCE_LOG);
        m.add(Material.STRIPPED_ACACIA_LOG);
        m.add(Material.STRIPPED_BIRCH_LOG);
        m.add(Material.STRIPPED_DARK_OAK_LOG);
        m.add(Material.STRIPPED_JUNGLE_LOG);
        m.add(Material.STRIPPED_OAK_LOG);
        m.add(Material.STRIPPED_SPRUCE_LOG);
        return m;
    }

    public static List<Material> getPlanksTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_PLANKS);
        m.add(Material.BIRCH_PLANKS);
        m.add(Material.DARK_OAK_PLANKS);
        m.add(Material.JUNGLE_PLANKS);
        m.add(Material.OAK_PLANKS);
        m.add(Material.SPRUCE_PLANKS);
        return m;
    }

    public static List<Material> getPressurePlateTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_PRESSURE_PLATE);
        m.add(Material.BIRCH_PRESSURE_PLATE);
        m.add(Material.DARK_OAK_PRESSURE_PLATE);
        m.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        m.add(Material.JUNGLE_PRESSURE_PLATE);
        m.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        m.add(Material.OAK_PRESSURE_PLATE);
        m.add(Material.SPRUCE_PRESSURE_PLATE);
        m.add(Material.STONE_PRESSURE_PLATE);
        return m;
    }

    public static List<Material> getSaplingTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_SAPLING);
        m.add(Material.BIRCH_SAPLING);
        m.add(Material.DARK_OAK_SAPLING);
        m.add(Material.JUNGLE_SAPLING);
        m.add(Material.OAK_SAPLING);
        m.add(Material.POTTED_ACACIA_SAPLING);
        m.add(Material.POTTED_BIRCH_SAPLING);
        m.add(Material.POTTED_DARK_OAK_SAPLING);
        m.add(Material.POTTED_JUNGLE_SAPLING);
        m.add(Material.POTTED_OAK_SAPLING);
        m.add(Material.POTTED_SPRUCE_SAPLING);
        m.add(Material.SPRUCE_SAPLING);
        return m;
    }

    public static List<Material> getSlabTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_SLAB);
        m.add(Material.BIRCH_SLAB);
        m.add(Material.BRICK_SLAB);
        m.add(Material.COBBLESTONE_SLAB);
        m.add(Material.DARK_OAK_SLAB);
        m.add(Material.DARK_PRISMARINE_SLAB);
        m.add(Material.JUNGLE_SLAB);
        m.add(Material.NETHER_BRICK_SLAB);
        m.add(Material.OAK_SLAB);
        m.add(Material.PETRIFIED_OAK_SLAB);
        m.add(Material.PRISMARINE_BRICK_SLAB);
        m.add(Material.PRISMARINE_SLAB);
        m.add(Material.PURPUR_SLAB);
        m.add(Material.QUARTZ_SLAB);
        m.add(Material.RED_SANDSTONE_SLAB);
        m.add(Material.SANDSTONE_SLAB);
        m.add(Material.SPRUCE_SLAB);
        m.add(Material.STONE_BRICK_SLAB);
        m.add(Material.STONE_SLAB);
        return m;
    }

    public static List<Material> getStairsTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_STAIRS);
        m.add(Material.BIRCH_STAIRS);
        m.add(Material.BRICK_STAIRS);
        m.add(Material.COBBLESTONE_STAIRS);
        m.add(Material.DARK_OAK_STAIRS);
        m.add(Material.DARK_PRISMARINE_STAIRS);
        m.add(Material.JUNGLE_STAIRS);
        m.add(Material.NETHER_BRICK_STAIRS);
        m.add(Material.OAK_STAIRS);
        m.add(Material.PRISMARINE_BRICK_STAIRS);
        m.add(Material.PRISMARINE_STAIRS);
        m.add(Material.PURPUR_STAIRS);
        m.add(Material.QUARTZ_STAIRS);
        m.add(Material.RED_SANDSTONE_STAIRS);
        m.add(Material.SANDSTONE_STAIRS);
        m.add(Material.SPRUCE_STAIRS);
        m.add(Material.STONE_BRICK_STAIRS);
        return m;
    }

    public static List<Material> getTrapdoorTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_TRAPDOOR);
        m.add(Material.BIRCH_TRAPDOOR);
        m.add(Material.DARK_OAK_TRAPDOOR);
        m.add(Material.IRON_TRAPDOOR);
        m.add(Material.JUNGLE_TRAPDOOR);
        m.add(Material.OAK_TRAPDOOR);
        m.add(Material.SPRUCE_TRAPDOOR);
        return m;
    }

    public static List<Material> getWoodTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.ACACIA_WOOD);
        m.add(Material.BIRCH_WOOD);
        m.add(Material.DARK_OAK_WOOD);
        m.add(Material.JUNGLE_WOOD);
        m.add(Material.OAK_WOOD);
        m.add(Material.SPRUCE_WOOD);
        m.add(Material.STRIPPED_ACACIA_WOOD);
        m.add(Material.STRIPPED_BIRCH_WOOD);
        m.add(Material.STRIPPED_DARK_OAK_WOOD);
        m.add(Material.STRIPPED_JUNGLE_WOOD);
        m.add(Material.STRIPPED_OAK_WOOD);
        m.add(Material.STRIPPED_SPRUCE_WOOD);
        m.add(Material.WOODEN_AXE);
        m.add(Material.WOODEN_HOE);
        m.add(Material.WOODEN_PICKAXE);
        m.add(Material.WOODEN_SHOVEL);
        m.add(Material.WOODEN_SWORD);
        return m;
    }

    public static List<Material> getBannerTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_BANNER);
        m.add(Material.BLACK_WALL_BANNER);
        m.add(Material.BLUE_BANNER);
        m.add(Material.BLUE_WALL_BANNER);
        m.add(Material.BROWN_BANNER);
        m.add(Material.BROWN_WALL_BANNER);
        m.add(Material.CYAN_BANNER);
        m.add(Material.CYAN_WALL_BANNER);
        m.add(Material.GRAY_BANNER);
        m.add(Material.GRAY_WALL_BANNER);
        m.add(Material.GREEN_BANNER);
        m.add(Material.GREEN_WALL_BANNER);
        m.add(Material.LIGHT_BLUE_BANNER);
        m.add(Material.LIGHT_BLUE_WALL_BANNER);
        m.add(Material.LIGHT_GRAY_BANNER);
        m.add(Material.LIGHT_GRAY_WALL_BANNER);
        m.add(Material.LIME_BANNER);
        m.add(Material.LIME_WALL_BANNER);
        m.add(Material.MAGENTA_BANNER);
        m.add(Material.MAGENTA_WALL_BANNER);
        m.add(Material.ORANGE_BANNER);
        m.add(Material.ORANGE_WALL_BANNER);
        m.add(Material.PINK_BANNER);
        m.add(Material.PINK_WALL_BANNER);
        m.add(Material.PURPLE_BANNER);
        m.add(Material.PURPLE_WALL_BANNER);
        m.add(Material.RED_BANNER);
        m.add(Material.RED_WALL_BANNER);
        m.add(Material.WHITE_BANNER);
        m.add(Material.WHITE_WALL_BANNER);
        m.add(Material.YELLOW_BANNER);
        m.add(Material.YELLOW_WALL_BANNER);
        return m;
    }

    public static List<Material> getBedTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BEDROCK);
        m.add(Material.BLACK_BED);
        m.add(Material.BLUE_BED);
        m.add(Material.BROWN_BED);
        m.add(Material.CYAN_BED);
        m.add(Material.GRAY_BED);
        m.add(Material.GREEN_BED);
        m.add(Material.LIGHT_BLUE_BED);
        m.add(Material.LIGHT_GRAY_BED);
        m.add(Material.LIME_BED);
        m.add(Material.MAGENTA_BED);
        m.add(Material.ORANGE_BED);
        m.add(Material.PINK_BED);
        m.add(Material.PURPLE_BED);
        m.add(Material.RED_BED);
        m.add(Material.WHITE_BED);
        m.add(Material.YELLOW_BED);
        return m;
    }

    public static List<Material> getCarpetTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_CARPET);
        m.add(Material.BLUE_CARPET);
        m.add(Material.BROWN_CARPET);
        m.add(Material.CYAN_CARPET);
        m.add(Material.GRAY_CARPET);
        m.add(Material.GREEN_CARPET);
        m.add(Material.LIGHT_BLUE_CARPET);
        m.add(Material.LIGHT_GRAY_CARPET);
        m.add(Material.LIME_CARPET);
        m.add(Material.MAGENTA_CARPET);
        m.add(Material.ORANGE_CARPET);
        m.add(Material.PINK_CARPET);
        m.add(Material.PURPLE_CARPET);
        m.add(Material.RED_CARPET);
        m.add(Material.WHITE_CARPET);
        m.add(Material.YELLOW_CARPET);
        return m;
    }

    public static List<Material> getConcreteTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_CONCRETE);
        m.add(Material.BLUE_CONCRETE);
        m.add(Material.BROWN_CONCRETE);
        m.add(Material.CYAN_CONCRETE);
        m.add(Material.GRAY_CONCRETE);
        m.add(Material.GREEN_CONCRETE);
        m.add(Material.LIGHT_BLUE_CONCRETE);
        m.add(Material.LIGHT_GRAY_CONCRETE);
        m.add(Material.LIME_CONCRETE);
        m.add(Material.MAGENTA_CONCRETE);
        m.add(Material.ORANGE_CONCRETE);
        m.add(Material.PINK_CONCRETE);
        m.add(Material.PURPLE_CONCRETE);
        m.add(Material.RED_CONCRETE);
        m.add(Material.WHITE_CONCRETE);
        m.add(Material.YELLOW_CONCRETE);
        return m;
    }

    public static List<Material> getConcretePowderTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_CONCRETE_POWDER);
        m.add(Material.BLUE_CONCRETE_POWDER);
        m.add(Material.BROWN_CONCRETE_POWDER);
        m.add(Material.CYAN_CONCRETE_POWDER);
        m.add(Material.GRAY_CONCRETE_POWDER);
        m.add(Material.GREEN_CONCRETE_POWDER);
        m.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        m.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        m.add(Material.LIME_CONCRETE_POWDER);
        m.add(Material.MAGENTA_CONCRETE_POWDER);
        m.add(Material.ORANGE_CONCRETE_POWDER);
        m.add(Material.PINK_CONCRETE_POWDER);
        m.add(Material.PURPLE_CONCRETE_POWDER);
        m.add(Material.RED_CONCRETE_POWDER);
        m.add(Material.WHITE_CONCRETE_POWDER);
        m.add(Material.YELLOW_CONCRETE_POWDER);
        return m;
    }

    public static List<Material> getGlazedTerracottaTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_GLAZED_TERRACOTTA);
        m.add(Material.BLUE_GLAZED_TERRACOTTA);
        m.add(Material.BROWN_GLAZED_TERRACOTTA);
        m.add(Material.CYAN_GLAZED_TERRACOTTA);
        m.add(Material.GRAY_GLAZED_TERRACOTTA);
        m.add(Material.GREEN_GLAZED_TERRACOTTA);
        m.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        m.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        m.add(Material.LIME_GLAZED_TERRACOTTA);
        m.add(Material.MAGENTA_GLAZED_TERRACOTTA);
        m.add(Material.ORANGE_GLAZED_TERRACOTTA);
        m.add(Material.PINK_GLAZED_TERRACOTTA);
        m.add(Material.PURPLE_GLAZED_TERRACOTTA);
        m.add(Material.RED_GLAZED_TERRACOTTA);
        m.add(Material.WHITE_GLAZED_TERRACOTTA);
        m.add(Material.YELLOW_GLAZED_TERRACOTTA);
        return m;
    }

    public static List<Material> getShulkerBoxTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_SHULKER_BOX);
        m.add(Material.BLUE_SHULKER_BOX);
        m.add(Material.BROWN_SHULKER_BOX);
        m.add(Material.CYAN_SHULKER_BOX);
        m.add(Material.GRAY_SHULKER_BOX);
        m.add(Material.GREEN_SHULKER_BOX);
        m.add(Material.LIGHT_BLUE_SHULKER_BOX);
        m.add(Material.LIGHT_GRAY_SHULKER_BOX);
        m.add(Material.LIME_SHULKER_BOX);
        m.add(Material.MAGENTA_SHULKER_BOX);
        m.add(Material.ORANGE_SHULKER_BOX);
        m.add(Material.PINK_SHULKER_BOX);
        m.add(Material.PURPLE_SHULKER_BOX);
        m.add(Material.RED_SHULKER_BOX);
        m.add(Material.SHULKER_BOX);
        m.add(Material.WHITE_SHULKER_BOX);
        m.add(Material.YELLOW_SHULKER_BOX);
        return m;
    }

    public static List<Material> getStainedGlassTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_STAINED_GLASS);
        m.add(Material.BLUE_STAINED_GLASS);
        m.add(Material.BROWN_STAINED_GLASS);
        m.add(Material.CYAN_STAINED_GLASS);
        m.add(Material.GRAY_STAINED_GLASS);
        m.add(Material.GREEN_STAINED_GLASS);
        m.add(Material.LIGHT_BLUE_STAINED_GLASS);
        m.add(Material.LIGHT_GRAY_STAINED_GLASS);
        m.add(Material.LIME_STAINED_GLASS);
        m.add(Material.MAGENTA_STAINED_GLASS);
        m.add(Material.ORANGE_STAINED_GLASS);
        m.add(Material.PINK_STAINED_GLASS);
        m.add(Material.PURPLE_STAINED_GLASS);
        m.add(Material.RED_STAINED_GLASS);
        m.add(Material.WHITE_STAINED_GLASS);
        m.add(Material.YELLOW_STAINED_GLASS);
        return m;
    }

    public static List<Material> getStainedGlassPaneTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_STAINED_GLASS_PANE);
        m.add(Material.BLUE_STAINED_GLASS_PANE);
        m.add(Material.BROWN_STAINED_GLASS_PANE);
        m.add(Material.CYAN_STAINED_GLASS_PANE);
        m.add(Material.GRAY_STAINED_GLASS_PANE);
        m.add(Material.GREEN_STAINED_GLASS_PANE);
        m.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        m.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        m.add(Material.LIME_STAINED_GLASS_PANE);
        m.add(Material.MAGENTA_STAINED_GLASS_PANE);
        m.add(Material.ORANGE_STAINED_GLASS_PANE);
        m.add(Material.PINK_STAINED_GLASS_PANE);
        m.add(Material.PURPLE_STAINED_GLASS_PANE);
        m.add(Material.RED_STAINED_GLASS_PANE);
        m.add(Material.WHITE_STAINED_GLASS_PANE);
        m.add(Material.YELLOW_STAINED_GLASS_PANE);
        return m;
    }

    public static List<Material> getTerracottaTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_GLAZED_TERRACOTTA);
        m.add(Material.BLACK_TERRACOTTA);
        m.add(Material.BLUE_GLAZED_TERRACOTTA);
        m.add(Material.BLUE_TERRACOTTA);
        m.add(Material.BROWN_GLAZED_TERRACOTTA);
        m.add(Material.BROWN_TERRACOTTA);
        m.add(Material.CYAN_GLAZED_TERRACOTTA);
        m.add(Material.CYAN_TERRACOTTA);
        m.add(Material.GRAY_GLAZED_TERRACOTTA);
        m.add(Material.GRAY_TERRACOTTA);
        m.add(Material.GREEN_GLAZED_TERRACOTTA);
        m.add(Material.GREEN_TERRACOTTA);
        m.add(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        m.add(Material.LIGHT_BLUE_TERRACOTTA);
        m.add(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        m.add(Material.LIGHT_GRAY_TERRACOTTA);
        m.add(Material.LIME_GLAZED_TERRACOTTA);
        m.add(Material.LIME_TERRACOTTA);
        m.add(Material.MAGENTA_GLAZED_TERRACOTTA);
        m.add(Material.MAGENTA_TERRACOTTA);
        m.add(Material.ORANGE_GLAZED_TERRACOTTA);
        m.add(Material.ORANGE_TERRACOTTA);
        m.add(Material.PINK_GLAZED_TERRACOTTA);
        m.add(Material.PINK_TERRACOTTA);
        m.add(Material.PURPLE_GLAZED_TERRACOTTA);
        m.add(Material.PURPLE_TERRACOTTA);
        m.add(Material.RED_GLAZED_TERRACOTTA);
        m.add(Material.RED_TERRACOTTA);
        m.add(Material.TERRACOTTA);
        m.add(Material.WHITE_GLAZED_TERRACOTTA);
        m.add(Material.WHITE_TERRACOTTA);
        m.add(Material.YELLOW_GLAZED_TERRACOTTA);
        m.add(Material.YELLOW_TERRACOTTA);
        return m;
    }

    public static List<Material> getWallBannerTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_WALL_BANNER);
        m.add(Material.BLUE_WALL_BANNER);
        m.add(Material.BROWN_WALL_BANNER);
        m.add(Material.CYAN_WALL_BANNER);
        m.add(Material.GRAY_WALL_BANNER);
        m.add(Material.GREEN_WALL_BANNER);
        m.add(Material.LIGHT_BLUE_WALL_BANNER);
        m.add(Material.LIGHT_GRAY_WALL_BANNER);
        m.add(Material.LIME_WALL_BANNER);
        m.add(Material.MAGENTA_WALL_BANNER);
        m.add(Material.ORANGE_WALL_BANNER);
        m.add(Material.PINK_WALL_BANNER);
        m.add(Material.PURPLE_WALL_BANNER);
        m.add(Material.RED_WALL_BANNER);
        m.add(Material.WHITE_WALL_BANNER);
        m.add(Material.YELLOW_WALL_BANNER);
        return m;
    }

    public static List<Material> getWoolTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BLACK_WOOL);
        m.add(Material.BLUE_WOOL);
        m.add(Material.BROWN_WOOL);
        m.add(Material.CYAN_WOOL);
        m.add(Material.GRAY_WOOL);
        m.add(Material.GREEN_WOOL);
        m.add(Material.LIGHT_BLUE_WOOL);
        m.add(Material.LIGHT_GRAY_WOOL);
        m.add(Material.LIME_WOOL);
        m.add(Material.MAGENTA_WOOL);
        m.add(Material.ORANGE_WOOL);
        m.add(Material.PINK_WOOL);
        m.add(Material.PURPLE_WOOL);
        m.add(Material.RED_WOOL);
        m.add(Material.WHITE_WOOL);
        m.add(Material.YELLOW_WOOL);
        return m;
    }

    public static List<Material> getMusicDiscTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.MUSIC_DISC_11);
        m.add(Material.MUSIC_DISC_13);
        m.add(Material.MUSIC_DISC_BLOCKS);
        m.add(Material.MUSIC_DISC_CAT);
        m.add(Material.MUSIC_DISC_CHIRP);
        m.add(Material.MUSIC_DISC_FAR);
        m.add(Material.MUSIC_DISC_MALL);
        m.add(Material.MUSIC_DISC_MELLOHI);
        m.add(Material.MUSIC_DISC_STAL);
        m.add(Material.MUSIC_DISC_STRAD);
        m.add(Material.MUSIC_DISC_WAIT);
        m.add(Material.MUSIC_DISC_WARD);
        return m;
    }

    public static List<Material> getBushTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.DEAD_BUSH);
        m.add(Material.POTTED_DEAD_BUSH);
        m.add(Material.ROSE_BUSH);
        return m;
    }

    public static List<Material> getDyeTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.CYAN_DYE);
        m.add(Material.GRAY_DYE);
        m.add(Material.LIGHT_BLUE_DYE);
        m.add(Material.LIGHT_GRAY_DYE);
        m.add(Material.LIME_DYE);
        m.add(Material.MAGENTA_DYE);
        m.add(Material.ORANGE_DYE);
        m.add(Material.PINK_DYE);
        m.add(Material.PURPLE_DYE);
        return m;
    }

    public static List<Material> getCoralTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BRAIN_CORAL);
        m.add(Material.BUBBLE_CORAL);
        m.add(Material.FIRE_CORAL);
        m.add(Material.HORN_CORAL);
        m.add(Material.TUBE_CORAL);
        return m;
    }

    public static List<Material> getCoralBlockTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BRAIN_CORAL_BLOCK);
        m.add(Material.BUBBLE_CORAL_BLOCK);
        m.add(Material.DEAD_BRAIN_CORAL_BLOCK);
        m.add(Material.DEAD_BUBBLE_CORAL_BLOCK);
        m.add(Material.DEAD_FIRE_CORAL_BLOCK);
        m.add(Material.DEAD_HORN_CORAL_BLOCK);
        m.add(Material.DEAD_TUBE_CORAL_BLOCK);
        m.add(Material.FIRE_CORAL_BLOCK);
        m.add(Material.HORN_CORAL_BLOCK);
        m.add(Material.TUBE_CORAL_BLOCK);
        return m;
    }

    public static List<Material> getCoralFanTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BRAIN_CORAL_FAN);
        m.add(Material.BUBBLE_CORAL_FAN);
        m.add(Material.DEAD_BRAIN_CORAL_FAN);
        m.add(Material.DEAD_BUBBLE_CORAL_FAN);
        m.add(Material.DEAD_FIRE_CORAL_FAN);
        m.add(Material.DEAD_HORN_CORAL_FAN);
        m.add(Material.DEAD_TUBE_CORAL_FAN);
        m.add(Material.FIRE_CORAL_FAN);
        m.add(Material.HORN_CORAL_FAN);
        m.add(Material.TUBE_CORAL_FAN);
        return m;
    }

    public static List<Material> getCoralWallFanTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.BRAIN_CORAL_WALL_FAN);
        m.add(Material.BUBBLE_CORAL_WALL_FAN);
        m.add(Material.DEAD_BRAIN_CORAL_WALL_FAN);
        m.add(Material.DEAD_BUBBLE_CORAL_WALL_FAN);
        m.add(Material.DEAD_FIRE_CORAL_WALL_FAN);
        m.add(Material.DEAD_HORN_CORAL_WALL_FAN);
        m.add(Material.DEAD_TUBE_CORAL_WALL_FAN);
        m.add(Material.FIRE_CORAL_WALL_FAN);
        m.add(Material.HORN_CORAL_WALL_FAN);
        m.add(Material.TUBE_CORAL_WALL_FAN);
        return m;
    }

    public static List<Material> getHelmetTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.CHAINMAIL_HELMET);
        m.add(Material.DIAMOND_HELMET);
        m.add(Material.GOLDEN_HELMET);
        m.add(Material.IRON_HELMET);
        m.add(Material.LEATHER_HELMET);
        m.add(Material.TURTLE_HELMET);
        return m;
    }

    public static List<Material> getChestplateTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.CHAINMAIL_CHESTPLATE);
        m.add(Material.DIAMOND_CHESTPLATE);
        m.add(Material.GOLDEN_CHESTPLATE);
        m.add(Material.IRON_CHESTPLATE);
        m.add(Material.LEATHER_CHESTPLATE);
        return m;
    }

    public static List<Material> getLeggingsTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.CHAINMAIL_LEGGINGS);
        m.add(Material.DIAMOND_LEGGINGS);
        m.add(Material.GOLDEN_LEGGINGS);
        m.add(Material.IRON_LEGGINGS);
        m.add(Material.LEATHER_LEGGINGS);
        return m;
    }

    public static List<Material> getBootsTypes(){
        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception("Unsupported current version"));
        List<Material> m = new ArrayList<>();
        m.add(Material.CHAINMAIL_BOOTS);
        m.add(Material.DIAMOND_BOOTS);
        m.add(Material.GOLDEN_BOOTS);
        m.add(Material.IRON_BOOTS);
        m.add(Material.LEATHER_BOOTS);
        return m;
    }
}
