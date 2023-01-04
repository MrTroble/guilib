package com.troblecodings.guilib.ecs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class GuiInfo {

    public final MenuType<?> type;
    public final int id;
    public final Level world;
    public final BlockPos pos;
    public final Player player;

    public GuiInfo(final MenuType<?> type, final int id, final Level world, final BlockPos pos,
            final Player player) {
        this.type = type;
        this.id = id;
        this.world = world;
        this.pos = pos;
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTile() {
        return (T) world.getBlockEntity(pos);
    }

}
