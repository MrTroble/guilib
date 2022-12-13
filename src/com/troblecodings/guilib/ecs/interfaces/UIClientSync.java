package com.troblecodings.guilib.ecs.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface UIClientSync {

    void readFromNBT(CompoundTag compound);

    Player getPlayer();
}
