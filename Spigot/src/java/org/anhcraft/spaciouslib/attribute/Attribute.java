package org.anhcraft.spaciouslib.attribute;

import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an attribute implementation.
 */
public class Attribute {
    public enum Type {
        GENERIC_MAX_HEALTH(LivingEntity.class, 20.0, 0.0, 1024.0, "generic.maxHealth"),
        GENERIC_FOLLOW_RANGE(LivingEntity.class, 32.0, 0.0, 2048.0, "generic.followRange"),
        GENERIC_KNOCKBACK_RESISTANCE(LivingEntity.class, 0.0, 0.0, 1.0, "generic.knockbackResistance"),
        GENERIC_MOVEMENT_SPEED(LivingEntity.class, 0.7, 0.0, 1024.0, "generic.movementSpeed"),
        GENERIC_ATTACK_DAMAGE(LivingEntity.class, 2.0, 0.0, 2048.0, "generic.attackDamage"),
        GENERIC_ARMOR(LivingEntity.class, 0.0, 0.0, 30.0, "generic.armor"),
        GENERIC_ARMOR_TOUGHNESS(LivingEntity.class, 0.0, 0.0, 20.0, "generic.armorToughness"),
        GENERIC_ATTACK_SPEED(HumanEntity.class, 4.0, 0.0, 1024.0, "generic.attackSpeed"),
        GENERIC_LUCK(HumanEntity.class, 0.0, -1024.0, 1024.0, "generic.luck"),
        @Deprecated
        HORSE_JUMP_STRENGTH(Horse.class, 0.7, 0.0, 2.0, "generic.jumpStrength"),
        ZOMBIE_SPAWN_REINFORCEMENTS(Zombie.class, 0.0, 0.0, 1.0, "generic.spawnReinforcements"),
        PARROTS_FLYING_SPEED(Parrot.class, 0.4, 0.0, 1024.0, "generic.flyingSpeed");

        private Class<? extends Entity> e;
        private double min;
        private double max;
        private double base;
        private String id;

        Type(Class<? extends LivingEntity> e, double base, double min, double max, String id){
            this.e = e;
            this.min = min;
            this.max = max;
            this.base = base;
            this.id = id;
        }

        public double getMinValue(){
            return min;
        }

        public double getMaxValue(){
            return max;
        }

        public double getBaseValue(){
            return base;
        }

        public void setBaseValue(double base){
            this.base = base;
        }

        /**
         * Gets the entity can use this attribute
         * @return Entity's class
         */
        public Class<? extends Entity> getAllowedEntity(){
            return e;
        }

        public String getID(){
            return this.id;
        }

        public static Type getByID(String id){
            for(Type attr : values()){
                if(id.equals(attr.getID())){
                    return attr;
                }
            }
            return null;
        }
    }

    private Type type;
    private List<AttributeModifier> modifiers;

    public Attribute(Type type){
        this.type = type;
        this.modifiers = new ArrayList<>();
    }

    public Attribute(Type type, List<AttributeModifier> modifiers){
        this.type = type;
        this.modifiers = modifiers;
    }

    public Attribute addModifier(AttributeModifier modifier){
        this.modifiers.add(modifier);
        return this;
    }

    public Attribute removeModifier(AttributeModifier modifier){
        this.modifiers.remove(modifier);
        return this;
    }

    public List<AttributeModifier> getModifiers(){
        return this.modifiers;
    }

    public Attribute setModifiers(List<AttributeModifier> modifiers){
        this.modifiers = modifiers;
        return this;
    }

    public Type getType(){
        return this.type;
    }
}