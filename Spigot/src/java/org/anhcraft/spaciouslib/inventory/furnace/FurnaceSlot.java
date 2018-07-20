package org.anhcraft.spaciouslib.inventory.furnace;

public enum FurnaceSlot {
    /**
     * The ingredient slot
     */
    INGREDIENT(0),
    /**
     * The fuel slot
     */
    FUEL(1),
    /**
     * The output slot
     */
    OUTPUT(2);

    private int slot;

    FurnaceSlot(int slot) {
        this.slot = slot;
    }

    public int getId() {
        return this.slot;
    }
}
