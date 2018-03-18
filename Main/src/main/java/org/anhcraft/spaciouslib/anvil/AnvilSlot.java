package org.anhcraft.spaciouslib.anvil;

public enum AnvilSlot {
    INPUT_LEFT(0),  INPUT_RIGHT(1),  OUTPUT(2);

    private int slot;

    AnvilSlot(int slot)
    {
        this.slot = slot;
    }

    public static AnvilSlot bySlot(int slot){
        AnvilSlot[] a;
        int j = (a = values()).length;
        for (int i = 0; i < j; i++) {
            AnvilSlot anvilSlot = a[i];
            if (anvilSlot.getSlot() == slot) {
                return anvilSlot;
            }
        }
        return null;
    }

    public int getSlot()
    {
        return this.slot;
    }
}
