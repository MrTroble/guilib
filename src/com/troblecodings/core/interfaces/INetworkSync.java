package com.troblecodings.core.interfaces;

import java.nio.ByteBuffer;

public interface INetworkSync {

    default void deserializeServer(ByteBuffer buf) {}
    
    default void deserializeClient(ByteBuffer buf) {}
}
