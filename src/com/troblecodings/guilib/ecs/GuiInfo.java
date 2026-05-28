package com.troblecodings.guilib.ecs;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class GuiInfo {

    public final int id;
    public final World world;
    public EntityPlayer player;
    public final InventoryPlayer inventory;
    public ITextComponent component;
    @Nullable
    public final BlockPos pos;
    @Nullable
    public ContainerBase base = null;

    public GuiInfo(final int id, final World world, final BlockPos pos, final EntityPlayer player,
            final InventoryPlayer inventory) {
        this.id = id;
        this.world = world;
        this.inventory = inventory;
        this.pos = pos;
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTile() {
        return (T) world.getTileEntity(pos);
    }

    public GuiInfo with(final ITextComponent component) {
        this.component = component;
        return this;
    }
}
