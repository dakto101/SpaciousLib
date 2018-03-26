package org.anhcraft.spaciouslib.attribute;

import java.util.UUID;

/**
 * Represents an attribute modifier implementation.
 */
public class AttributeModifier {
    public enum Operation{
        /**
         * Adds (or subtracts) its amount to the base value of the attribute.<br>
         * <base value 1> + <base value 2> + <attribute amount>
         */
        ADD(0),
        /**
         * Sums all amount of modifiers which has this operation, and calls as x.<br>
         * After that multiplies the base value of the attribute with (x + 1).
         * (<base value 1> + <base value 2> + 1) * <attribute amount>
         */
        MULTIPLY(1),
        /**
         * For every modifier which has this operation, sums by (x + 1) where x is the amount of that modifier.<br>
         * After that multiplies each those results together and also multiplies with the base value of the <br>attribute.
         * (<base value 1> + 1) + (<base value 2> + 1) * <attribute amount>
         */
        MULTIPLY_ALL(2);

        private int id;

        Operation(int id) {
            this.id = id;
        }

        public static Operation getByID(int id){
            for(Operation o : values()){
                if(o.getID() == id){
                    return o;
                }
            }
            return null;
        }

        public int getID(){
            return this.id;
        }
    }

    private String name;
    private double amount;
    private Operation operation;
    private UUID uuid;

    public AttributeModifier(String name, double amount, Operation operation){
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.uuid = UUID.randomUUID();
    }

    public AttributeModifier(UUID uuid, String name, double amount, Operation operation){
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.uuid = uuid;
    }

    public String getName(){
        return this.name;
    }

    public double getAmount(){
        return this.amount;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public UUID getUniqueID() {
        return this.uuid;
    }
}
