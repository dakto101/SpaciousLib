package org.anhcraft.spaciouslib.inventory.anvil;

public enum AnvilSlot {
    /**
     * The item on the left side, represents for the first input of an anvil
     */
    INPUT_LEFT(0),
    /**
     * The item on the center side, represents for the second input of an anvil
     */
    INPUT_RIGHT(1),
    /**
     * The item on the right side, represents for the output of an anvil
     */
    OUTPUT(2);

    private int slot;

    AnvilSlot(int slot) {
        this.slot = slot;
    }

    public int getID() {
        return this.slot;
    }
}
