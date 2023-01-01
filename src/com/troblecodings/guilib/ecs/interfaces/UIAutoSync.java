package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.core.NBTWrapper;

public interface UIAutoSync {

    public void write(final NBTWrapper compound);

    public void read(final NBTWrapper compound);

    public String getID();

    public void setID(final String id);
}
