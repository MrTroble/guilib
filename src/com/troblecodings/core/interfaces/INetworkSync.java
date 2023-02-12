package com.troblecodings.core.interfaces;

import java.nio.ByteBuffer;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent.ClientCustomPayloadEvent;
import net.minecraftforge.network.NetworkEvent.ServerCustomPayloadEvent;

public interface INetworkSync {

    default void deserializeServer(ByteBuffer buf) {}
    
    default void deserializeClient(ByteBuffer buf) {}
    
}
