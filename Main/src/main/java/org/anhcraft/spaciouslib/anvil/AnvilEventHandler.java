package org.anhcraft.spaciouslib.anvil;

import org.anhcraft.spaciouslib.events.AnvilClickEvent;
import org.anhcraft.spaciouslib.events.AnvilCloseEvent;

public interface AnvilEventHandler {
    void onAnvilClick(AnvilClickEvent clickEvent);
    void onAnvilClose(AnvilCloseEvent closeEvent);
}