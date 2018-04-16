package org.anhcraft.spaciouslib.attribute;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    /**
     * Creates a new AttributeModifier instance
     * @param name the name of the modifier
     * @param amount the amount value of the modifier
     * @param operation the operation of the modifier
     */
    public AttributeModifier(String name, double amount, Operation operation){
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Creates a new AttributeModifier instance
     * @param uuid the unique id of the modifier
     * @param name the name of the modifier
     * @param amount the amount value of the modifier
     * @param operation the operation of the modifier
     */
    public AttributeModifier(UUID uuid, String name, double amount, Operation operation){
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.uuid = uuid;
    }

    /**
     * Gets the name of this modifier
     * @return a name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the amount value of this modifier
     * @return amount
     */
    public double getAmount(){
        return this.amount;
    }

    /**
     * Gets the operation type of this modifier
     * @return an operation type
     */
    public Operation getOperation() {
        return this.operation;
    }

    /**
     * Gets the unique id of this modifier
     * @return an unique id
     */
    public UUID getUniqueID() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            AttributeModifier am = (AttributeModifier) o;
            return new EqualsBuilder()
                    .append(am.amount, this.amount)
                    .append(am.uuid, this.uuid)
                    .append(am.operation, this.operation)
                    .append(am.name, this.name)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(5, 14)
                .append(amount)
                .append(uuid)
                .append(operation)
                .append(name).toHashCode();
    }
}
