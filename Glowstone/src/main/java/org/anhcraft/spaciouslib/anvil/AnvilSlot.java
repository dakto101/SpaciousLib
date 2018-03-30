package org.anhcraft.spaciouslib.anvil;

public enum AnvilSlot {
    INPUT_LEFT(0),
    INPUT_RIGHT(1),
    OUTPUT(2);

    private int slot;

    AnvilSlot(int slot) {
        this.slot = slot;
    }

    public int getID() {
        return this.slot;
    }
}
