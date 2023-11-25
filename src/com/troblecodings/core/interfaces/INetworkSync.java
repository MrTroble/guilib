package com.troblecodings.core.interfaces;

import java.nio.ByteBuffer;

import com.troblecodings.core.ReadBuffer;

public interface INetworkSync {

    default void deserializeServer(final ByteBuffer buf) {
        deserializeServer(new ReadBuffer(buf));
    }

    default void deserializeClient(final ByteBuffer buf) {
        deserializeClient(new ReadBuffer(buf));
    }

    default void deserializeServer(final ReadBuffer buf) {
    }

    default void deserializeClient(final ReadBuffer buf) {
    }

}