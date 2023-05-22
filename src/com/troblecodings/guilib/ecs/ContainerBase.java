package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBase extends Container implements INetworkSync {

    private final GuiInfo info;

    public ContainerBase(final GuiInfo info) {
        super();
        info.base = this;
        if (info.world.isRemote) {
            final Minecraft mc = Minecraft.getMinecraft();
            mc.player.openContainer = this;
        }
        this.info = info;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("resource")
    @SideOnly(Side.CLIENT)
    public void update() {
        ((GuiBase) Minecraft.getMinecraft().currentScreen).updateFromContainer();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }

    public EntityPlayer getPlayer() {
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