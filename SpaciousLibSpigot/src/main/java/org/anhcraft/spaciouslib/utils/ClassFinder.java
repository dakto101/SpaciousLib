package org.anhcraft.spaciouslib.utils;

public class ClassFinder {
    public static class NMS {
        private static final String base = "net.minecraft.server." + GameVersion.getVersion().toString()+".";
        public static Class<?> MinecraftServer;
        public static Class<?> ChatSerializer;
        public static Class<?> IChatBaseComponent;
        public static Class<?> EnumTitleAction;
        public static Class<?> BlockPosition;
        public static Class<?> ItemStack;
        public static Class<?> EnumItemSlot;
        public static Class<?> Entity;
        public static Class<?> DataWatcher;
        public static Class<?> EntityLiving;
        public static Class<?> EntityHuman;
        public static Class<?> RegistryMaterials;
        public static Class<?> Particle;
        public static Class<?> ParticleParam;
        public static Class<?> ParticleParamRedstone;
        public static Class<?> ParticleParamItem;
        public static Class<?> ParticleParamBlock;
        public static Class<?> MinecraftKey;
        public static Class<?> IBlockData;
        public static Class<?> IRegistry;
        public static Class<?> EnumParticle;
        public static Class<?> EntityPlayer;
        public static Class<?> EnumPlayerInfoAction;
        public static Class<?> PlayerConnection;
        public static Class<?> NetworkManager;
        public static Class<?> CraftingManager;
        public static Class<?> IRecipe;
        public static Class<?> WorldServer;
        public static Class<?> PlayerList;
        public static Class<?> PlayerInteractManager;
        public static Class<?> World;
        public static Class<?> EntityArmorStand;
        public static Class<?> Vector3f;
        public static Class<?> PacketPlayOutBoss;
        public static Class<?> BossAction;
        public static Class<?> BossBattle;
        public static Class<?> BarStyle;
        public static Class<?> BarColor;
        public static Class<?> EntityEnderDragon;
        public static Class<?> PacketDataSerializer;
        public static Class<?> PacketPlayOutTitle;
        public static Class<?> PacketPlayOutChat;
        public static Class<?> PacketPlayOutAnimation;
        public static Class<?> PacketPlayOutBlockBreakAnimation;
        public static Class<?> PacketPlayOutCamera;
        public static Class<?> PacketPlayOutEntityDestroy;
        public static Class<?> PacketPlayOutEntityEquipment;
        public static Class<?> PacketPlayOutEntityLook;
        public static Class<?> PacketPlayOutEntityMetadata;
        public static Class<?> PacketPlayOutEntityTeleport;
        public static Class<?> PacketPlayOutExperience;
        public static Class<?> PacketPlayOutSpawnEntityLiving;
        public static Class<?> PacketPlayOutNamedEntitySpawn;
        public static Class<?> PacketPlayOutWorldParticles;
        public static Class<?> PacketPlayOutPlayerInfo;
        public static Class<?> PacketPlayOutPlayerListHeaderFooter;
        public static Class<?> PacketPlayOutWindowData;
        public static Class<?> PacketPlayOutCustomPayload;

        static {
            try {
                MinecraftServer = Class.forName(base + "MinecraftServer");
                IChatBaseComponent = Class.forName(base + "IChatBaseComponent");
                BlockPosition = Class.forName(base + "BlockPosition");
                ItemStack = Class.forName(base + "ItemStack");
                Entity = Class.forName(base + "Entity");
                DataWatcher = Class.forName(base + "DataWatcher");
                EntityLiving = Class.forName(base + "EntityLiving");
                EntityHuman = Class.forName(base + "EntityHuman");
                RegistryMaterials = Class.forName(base + "RegistryMaterials");
                MinecraftKey = Class.forName(base + "MinecraftKey");
                IRegistry = Class.forName(base + "IRegistry");
                EntityPlayer = Class.forName(base + "EntityPlayer");
                PlayerConnection = Class.forName(base + "PlayerConnection");
                NetworkManager = Class.forName(base + "NetworkManager");
                CraftingManager = Class.forName(base + "CraftingManager");
                IRecipe = Class.forName(base + "IRecipe");
                WorldServer = Class.forName(base + "WorldServer");
                PlayerList = Class.forName(base + "PlayerList");
                PlayerInteractManager = Class.forName(base + "PlayerInteractManager");
                World = Class.forName(base + "World");
                EntityArmorStand = Class.forName(base + "EntityArmorStand");
                Vector3f = Class.forName(base + "Vector3f");
                EntityEnderDragon = Class.forName(base + "EntityEnderDragon");
                PacketDataSerializer = Class.forName(base + "PacketDataSerializer");
                PacketPlayOutTitle = Class.forName(base + "PacketPlayOutTitle");
                PacketPlayOutAnimation = Class.forName(base + "PacketPlayOutAnimation");
                PacketPlayOutBlockBreakAnimation = Class.forName(base + "PacketPlayOutBlockBreakAnimation");
                PacketPlayOutCamera = Class.forName(base + "PacketPlayOutCamera");
                PacketPlayOutEntityDestroy = Class.forName(base + "PacketPlayOutEntityDestroy");
                PacketPlayOutEntityEquipment = Class.forName(base + "PacketPlayOutEntityEquipment");
                PacketPlayOutEntityMetadata = Class.forName(base + "PacketPlayOutEntityMetadata");
                PacketPlayOutEntityTeleport = Class.forName(base + "PacketPlayOutEntityTeleport");
                PacketPlayOutExperience = Class.forName(base + "PacketPlayOutExperience");
                PacketPlayOutSpawnEntityLiving = Class.forName(base + "PacketPlayOutSpawnEntityLiving");
                PacketPlayOutNamedEntitySpawn = Class.forName(base + "PacketPlayOutNamedEntitySpawn");
                PacketPlayOutWorldParticles = Class.forName(base + "PacketPlayOutWorldParticles");
                PacketPlayOutPlayerInfo = Class.forName(base + "PacketPlayOutPlayerInfo");
                PacketPlayOutPlayerListHeaderFooter = Class.forName(base + "PacketPlayOutPlayerListHeaderFooter");
                PacketPlayOutTitle = Class.forName(base + "PacketPlayOutTitle");
                PacketPlayOutWindowData = Class.forName(base + "PacketPlayOutWindowData");
                PacketPlayOutCustomPayload = Class.forName(base + "PacketPlayOutCustomPayload");
                if(GameVersion.getVersion().equals(GameVersion.v1_8_R1)){
                    EnumTitleAction = Class.forName(base + "EnumTitleAction");
                    ChatSerializer = Class.forName(base + "ChatSerializer");
                    PacketPlayOutEntityLook = Class.forName(base + "PacketPlayOutEntityLook");
                    EnumPlayerInfoAction = Class.forName(base + "EnumPlayerInfoAction");
                } else {
                    EnumTitleAction = Class.forName(base + "PacketPlayOutTitle$EnumTitleAction");
                    ChatSerializer = Class.forName(base + "IChatBaseComponent$ChatSerializer");
                    PacketPlayOutEntityLook = Class.forName(base + "PacketPlayOutEntity$PacketPlayOutEntityLook");
                    EnumPlayerInfoAction = Class.forName(base + "PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
                }
                if(GameVersion.is1_9Above()) {
                    EnumItemSlot = Class.forName(base + "EnumItemSlot");
                    PacketPlayOutBoss = Class.forName(base + "PacketPlayOutBoss");
                    BossAction = Class.forName(base + "PacketPlayOutBoss$Action");
                    BossBattle = Class.forName(base + "BossBattle");
                    BarColor = Class.forName(base + "BossBattle$BarColor");
                    BarStyle = Class.forName(base + "BossBattle$BarStyle");
                }
                if(GameVersion.is1_13Above()) {
                    Particle = Class.forName(base + "Particle");
                    ParticleParam = Class.forName(base + "ParticleParam");
                    ParticleParamBlock = Class.forName(base + "ParticleParamBlock");
                    ParticleParamItem = Class.forName(base + "ParticleParamItem");
                    ParticleParamRedstone = Class.forName(base + "ParticleParamRedstone");
                    IBlockData = Class.forName(base + "IBlockData");
                } else {
                    EnumParticle = Class.forName(base + "EnumParticle");
                }
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public static class CB {
        private static final String base = "org.bukkit.craftbukkit." + GameVersion.getVersion().toString()+".";
        public static Class<?> CraftServer;
        public static Class<?> CraftItemStack;
        public static Class<?> CraftEntity;
        public static Class<?> CraftLivingEntity;
        public static Class<?> CraftHumanEntity;
        public static Class<?> CraftBlockData;
        public static Class<?> CraftPlayer;
        public static Class<?> CraftWorld;

        static {
            try {
                CraftServer = Class.forName(base + "CraftServer");
                CraftItemStack = Class.forName(base + "inventory.CraftItemStack");
                CraftEntity = Class.forName(base + "entity.CraftEntity");
                CraftLivingEntity = Class.forName(base + "entity.CraftLivingEntity");
                CraftHumanEntity = Class.forName(base + "entity.CraftHumanEntity");
                CraftPlayer = Class.forName(base + "entity.CraftPlayer");
                CraftWorld = Class.forName(base + "CraftWorld");
                if(GameVersion.is1_13Above()) {
                    CraftBlockData = Class.forName(base + "block.data.CraftBlockData");
                }
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
