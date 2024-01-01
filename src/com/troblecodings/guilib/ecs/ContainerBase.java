package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerBase extends AbstractContainerMenu implements INetworkSync {

    protected final GuiInfo info;

    public ContainerBase(final GuiInfo info) {
        super(info.type, info.id);
        info.base = this;
        info.player.containerMenu = this;
        this.info = info;
    }

    @OnlyIn(Dist.CLIENT)
    public void update() {
        final Minecraft minecraft = Minecraft.getInstance();
        final Screen screen = minecraft.screen;
        if (screen != null) {
            ((GuiBase) screen).updateFromContainer();
        }
    }

    @Override
    public boolean stillValid(final Player player) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }

    public Player getPlayer() {
        return info.player;
    }

    @Override
    public ItemStack quickMoveStack(final Player player, final int p_38942_) {
        return null;
    }

}