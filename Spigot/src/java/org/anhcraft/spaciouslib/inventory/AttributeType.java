package org.anhcraft.spaciouslib.inventory;

import org.bukkit.entity.*;

public enum AttributeType {
    MAX_HEALTH(LivingEntity.class, 0.0, 1024.0, "generic.maxHealth"),
    FOLLOW_RANGE(LivingEntity.class, 0.0, 2048.0, "generic.followRange"),
    KNOCKBACK_RESISTANCE(LivingEntity.class, 0.0, 1.0, "generic.knockbackResistance"),
    MOVEMENT_SPEED(LivingEntity.class, 0.0, 1024.0, "generic.movementSpeed"),
    ATTACK_DAMAGE(LivingEntity.class, 0.0, 2048.0, "generic.attackDamage"),
    ARMOR(LivingEntity.class, 0.0, 30.0, "generic.armor"),
    ARMOR_TOUGHNESS(LivingEntity.class, 0.0, 20.0, "generic.armorToughness"),
    ATTACK_SPEED(HumanEntity.class, 0.0, 1024.0, "generic.attackSpeed"),
    LUCK(HumanEntity.class, -1024.0, 1024.0, "generic.luck"),
    @Deprecated
    JUMP_STRENGTH(Horse.class, 0.0, 2.0, "generic.jumpStrength"),
    SPAWN_REINFORCEMENTS(Zombie.class, 0.0, 1.0, "generic.spawnReinforcements");

    private Class<? extends Entity> e;
    private double min;
    private double max;
    private String id;

    AttributeType(Class<? extends LivingEntity> e, double min, double max, String id){
        this.e = e;
        this.min = min;
        this.max = max;
        this.id = id;
    }

    public Double getMinValue(){
        return min;
    }

    public Double getMaxValue(){
        return max;
    }

    public Class<? extends Entity> getAllowedEntity(){
        return e;
    }

    public String getID(){
        return this.id;
    }

    public static AttributeType getById(String id){
        for(AttributeType attr : values()){
            if(id.equals(attr.getID())){
                return attr;
            }
        }
        return null;
    }
}
