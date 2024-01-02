package com.troblecodings.guilib.ecs;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public class GuiInfo {

    public final MenuType<?> type;
    public final int id;
    public final Level world;
    public Player player;
    public final Inventory inventory;
    public Component component = Component.translatable("");
    @Nullable
    public final BlockPos pos;
    @Nullable
    public ContainerBase base = null;

    public GuiInfo(final MenuType<?> type, final int id, final Level world, final BlockPos pos,
            final Player player, final Inventory inventory) {
        this.type = type;
        this.id = id;
        this.world = world;
        this.inventory = inventory;
        this.pos = pos;
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTile() {
        return (T) world.getBlockEntity(pos);
    }

    public GuiInfo with(final Component component) {
        this.component = component;
        return this;
    }
}
