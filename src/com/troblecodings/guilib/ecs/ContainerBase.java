package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerBase extends Container implements INetworkSync {

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
    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }

    public PlayerEntity getPlayer() {
        return info.player;
    }

}