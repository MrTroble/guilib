package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerBase extends AbstractContainerMenu implements INetworkSync {

    private final GuiInfo info;

    public ContainerBase(final GuiInfo info) {
        super(info.type, info.id);
        info.base = this;
        this.info = info;
    }

    @SuppressWarnings("resource")
    @OnlyIn(Dist.CLIENT)
    public void update() {
        ((GuiBase) Minecraft.getInstance().screen).updateFromContainer();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }

    public Player getPlayer() {
        return info.player;
    }

}