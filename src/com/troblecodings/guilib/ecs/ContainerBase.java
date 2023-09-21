package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ContainerBase extends Container implements INetworkSync {

    private final GuiInfo info;

    public ContainerBase(final GuiInfo info) {
        super(info.type, info.id);
        info.base = this;
        if (info.world.isClientSide) {
            final Minecraft mc = Minecraft.getInstance();
            mc.player.containerMenu = this;
        }
        this.info = info;
        MinecraftForge.EVENT_BUS.register(this);
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
    public boolean stillValid(final PlayerEntity player) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }

    public PlayerEntity getPlayer() {
        return info.player;
    }

    @SubscribeEvent
    public void onContainerOpen(final PlayerContainerEvent.Open event) {
        if (event.getContainer() == this)
            sendAllDataToRemote();
    }

    public void sendAllDataToRemote() {
    }

}