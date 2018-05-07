package org.anhcraft.spaciouslib.attribute;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an attribute implementation.
 */
public class Attribute {
    public enum Type {
        GENERIC_MAX_HEALTH(20.0, 0.0, 1024.0, "generic.maxHealth"),
        GENERIC_FOLLOW_RANGE(32.0, 0.0, 2048.0, "generic.followRange"),
        GENERIC_KNOCKBACK_RESISTANCE(0.0, 0.0, 1.0, "generic.knockbackResistance"),
        GENERIC_MOVEMENT_SPEED(0.7, 0.0, 1024.0, "generic.movementSpeed"),
        GENERIC_ATTACK_DAMAGE(2.0, 0.0, 2048.0, "generic.attackDamage"),
        GENERIC_ARMOR(0.0, 0.0, 30.0, "generic.armor"),
        GENERIC_ARMOR_TOUGHNESS(0.0, 0.0, 20.0, "generic.armorToughness"),
        GENERIC_ATTACK_SPEED(4.0, 0.0, 1024.0, "generic.attackSpeed"),
        GENERIC_LUCK(0.0, -1024.0, 1024.0, "generic.luck"),
        @Deprecated
        HORSE_JUMP_STRENGTH(0.7, 0.0, 2.0, "generic.jumpStrength"),
        ZOMBIE_SPAWN_REINFORCEMENTS(0.0, 0.0, 1.0, "generic.spawnReinforcements"),
        PARROTS_FLYING_SPEED(0.4, 0.0, 1024.0, "generic.flyingSpeed");

        private double min;
        private double max;
        private double base;
        private String id;

        Type(double base, double min, double max, String id){
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

    /**
     * Create a new Attribute instance
     * @param type the type of an attribute
     */
    public Attribute(Type type){
        this.type = type;
        this.modifiers = new ArrayList<>();
    }

    /**
     * Create a new Attribute instance
     * @param type the type of an attribute
     * @param modifiers list of modifiers of an attribute
     */
    public Attribute(Type type, List<AttributeModifier> modifiers){
        this.type = type;
        this.modifiers = modifiers;
    }

    /**
     * Adds the given modifier to this attribute
     * @param modifier the attribute modifier
     * @return this object
     */
    public Attribute addModifier(AttributeModifier modifier){
        this.modifiers.add(modifier);
        return this;
    }

    /**
     * Removes the given modifier out of this attribute
     * @param modifier the attribute modifier
     * @return this object
     */
    public Attribute removeModifier(AttributeModifier modifier){
        this.modifiers.remove(modifier);
        return this;
    }

    /**
     * Gets the list of all modifiers
     * @return list of modifiers
     */
    public List<AttributeModifier> getModifiers(){
        return this.modifiers;
    }

    /**
     * Gets the type of this attribute
     * @return the type of attribute
     */
    public Type getType(){
        return this.type;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Attribute a = (Attribute) o;
            return new EqualsBuilder().append(a.modifiers, this.modifiers).append(a.type, this.type).build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(23, 7)
                .append(modifiers).append(type).toHashCode();
    }
}