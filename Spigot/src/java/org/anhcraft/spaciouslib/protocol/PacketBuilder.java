package org.anhcraft.spaciouslib.protocol;

import java.util.LinkedList;

public abstract class PacketBuilder<T> {
    protected PacketSender packetSender;
    protected LinkedList<PacketSender> packets = new LinkedList<>();

    protected void createPacketSender(){
        packetSender = new PacketSender(packets);
    }

    public abstract T buildPackets();
}
