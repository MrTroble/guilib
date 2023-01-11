package com.troblecodings.core.interfaces;

import io.netty.buffer.ByteBuf;

public interface INetworkSync {

    default void deserializeServer(ByteBuf buf) {}
    
    default void deserializeClient(ByteBuf buf) {}
}
