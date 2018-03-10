package org.anhcraft.spaciouslib.inventory;

import org.bukkit.entity.*;

public enum AttributeType {
    maxHealth(LivingEntity.class, 0.0, 1024.0),
    followRange(LivingEntity.class, 0.0, 2048.0),
    knockbackResistance(LivingEntity.class, 0.0, 1.0),
    movementSpeed(LivingEntity.class, 0.0, 1024.0),
    attackDamage(LivingEntity.class, 0.0, 2048.0),
    armor(LivingEntity.class, 0.0, 30.0),
    armorToughness(LivingEntity.class, 0.0, 20.0),
    attackSpeed(HumanEntity.class, 0.0, 1024.0),
    luck(HumanEntity.class, -1024.0, 1024.0),
    @Deprecated
    jumpStrength(Horse.class, 0.0, 2.0),
    spawnReinforcements(Zombie.class, 0.0, 1.0);

    private Class<? extends Entity> e;
    private double min;
    private double max;

    AttributeType(Class<? extends LivingEntity> e, double min, double max){
        this.e = e;
        this.min = min;
        this.max = max;
    }

    public Double getMinValue(){
        return min;
    }

    public Double getMaxValue(){
        return max;
    }

    public Class<? extends Entity> getEntityUse(){
        return e;
    }

    public static AttributeType getByName(String name){
        name = name.toLowerCase().trim();
        switch(name){
            case "generic.maxhealth":
                return AttributeType.maxHealth;
            case "generic.followrange":
                return AttributeType.followRange;
            case "generic.knockbackresistance":
                return AttributeType.knockbackResistance;
            case "generic.movementspeed":
                return AttributeType.movementSpeed;
            case "generic.attackdamage":
                return AttributeType.attackDamage;
            case "generic.armor":
                return AttributeType.armor;
            case "generic.armortoughness":
                return AttributeType.armorToughness;
            case "generic.attackspeed":
                return AttributeType.attackSpeed;
            case "generic.luck":
                return AttributeType.luck;
            case "generic.jumpstrength":
                return AttributeType.jumpStrength;
            case "generic.spawnreinforcements":
                return AttributeType.spawnReinforcements;
            default:
                return null;
        }
    }

    public static String getModifierName(AttributeType type){
        if(type.equals(AttributeType.maxHealth)){
            return "generic.maxHealth";
        }
        else if(type.equals(AttributeType.followRange)){
            return "generic.followRange";
        }
        else if(type.equals(AttributeType.knockbackResistance)){
            return "generic.knockbackResistance";
        }
        else if(type.equals(AttributeType.movementSpeed)){
            return "generic.movementSpeed";
        }
        else if(type.equals(AttributeType.attackDamage)){
            return "generic.attackDamage";
        }
        else if(type.equals(AttributeType.armor)){
            return "generic.armor";
        }
        else if(type.equals(AttributeType.armorToughness)){
            return "generic.armorToughness";
        }
        else if(type.equals(AttributeType.attackSpeed)){
            return "generic.attackSpeed";
        }
        else if(type.equals(AttributeType.luck)){
            return "generic.luck";
        }
        else if(type.equals(AttributeType.jumpStrength)){
            return "generic.jumpStrength";
        }
        else if(type.equals(AttributeType.spawnReinforcements)){
            return "generic.spawnReinforcements";
        } else {
            return null;
        }
    }
}
