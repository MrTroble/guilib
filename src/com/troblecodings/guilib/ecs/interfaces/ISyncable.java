package com.troblecodings.guilib.ecs.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface ISyncable {

    void updateTag(CompoundTag compound);

    CompoundTag getTag();

    boolean isValid(Player player);
}
