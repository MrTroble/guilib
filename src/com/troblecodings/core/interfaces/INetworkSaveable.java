package com.troblecodings.core.interfaces;

import com.troblecodings.core.ReadBuffer;
import com.troblecodings.core.WriteBuffer;

public interface INetworkSaveable {

    public void readNetwork(final ReadBuffer buffer);

    public void writeNetwork(final WriteBuffer buffer);

}