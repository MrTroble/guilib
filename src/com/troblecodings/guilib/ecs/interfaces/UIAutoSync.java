package com.troblecodings.guilib.ecs.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface UIAutoSync {

    public void write(final CompoundTag compound);

    public void read(final CompoundTag compound);

    public String getID();

    public void setID(final String id);
}
