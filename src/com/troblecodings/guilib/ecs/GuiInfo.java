package com.troblecodings.guilib.ecs;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class GuiInfo {

    public final ContainerType<?> type;
    public final int id;
    public final World world;
    public PlayerEntity player;
    public final PlayerInventory inventory;
    public StringTextComponent component;
    @Nullable
    public final BlockPos pos;
    @Nullable
    public ContainerBase base = null;

    public GuiInfo(final ContainerType<?> type, final int id, final World world, final BlockPos pos,
            final PlayerEntity player, final PlayerInventory inventory) {
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

    public GuiInfo with(final StringTextComponent component) {
        this.component = component;
        return this;
    }
}
