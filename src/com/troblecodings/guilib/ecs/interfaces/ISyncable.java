package com.troblecodings.guilib.ecs.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface ISyncable {

    boolean isValid(PlayerEntity player);
}
