package com.troblecodings.core.interfaces;

import java.nio.ByteBuffer;

public interface INetworkSync {

    default void deserializeServer(final ByteBuffer buf) {
    }

    default void deserializeClient(final ByteBuffer buf) {
    }

}
