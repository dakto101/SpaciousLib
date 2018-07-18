package org.anhcraft.spaciouslib.protocol;

import java.util.ArrayList;
import java.util.List;

public abstract class PacketBuilder<T> {
    protected PacketSender packetSender;
    protected List<PacketSender> packets = new ArrayList<>();

    protected void createPacketSender(){
        packetSender = new PacketSender(packets);
    }

    public abstract T buildPackets();
}
