import org.bukkit.Material;

public class Test2 {
    public static void main(String[] args){
        String[] types = new String[]{"DOOR", "FENCE", "FENCE_GATE", "LEAVES", "LOG", "PLANKS", "PRESSURE_PLATE", "SAPLING", "SLAB", "STAIRS", "TRAPDOOR", "WOOD", "BANNER", "BED", "CARPET", "CONCRETE", "CONCRETE_POWDER", "GLAZED_TERRACOTTA", "SHULKER_BOX", "STAINED_GLASS", "STAINED_GLASS_PANE", "TERRACOTTA", "WALL_BANNER", "WOOL", "MUSIC_DISC", "BUSH", "DYE", "CORAL", "CORAL_BLOCK", "CORAL_FAN", "CORAL_WALL_FAN", "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"};
        for(String t : types) {
            String[] n = t.toLowerCase().split("_");
            StringBuilder a = new StringBuilder();
            for(String g : n) {
                a.append(g.toUpperCase().charAt(0)).append(g.toLowerCase().substring(1));
            }
            System.out.println("    public static List<Material> get"+a.toString()+"Types(){");
            System.out.println("        ExceptionThrower.ifFalse(GameVersion.is1_13Above(), new Exception(\"Unsupported current version\"));");
            System.out.println("        List<Material> m = new ArrayList<>();");
            for(Material m : Material.values()) {
                if((m.name().endsWith(t) && !m.name().startsWith("LEGACY_")) || m.name().startsWith(t)) {
                    System.out.println("        m.add(Material." + m.name() + ");");
                }
            }
            System.out.println("        return m;");
            System.out.println("    }");
            System.out.println();
        }
        for(String t : types) {
            String[] n = t.toLowerCase().split("_");
            StringBuilder a = new StringBuilder();
            for(String g : n) {
                a.append(g.toUpperCase().charAt(0)).append(g.toLowerCase().substring(1));
            }
            System.out.println("- Added InventoryUtils#get"+a.toString()+"Types()");
        }
    }
}
