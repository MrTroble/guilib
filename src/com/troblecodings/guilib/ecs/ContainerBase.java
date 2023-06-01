package com.troblecodings.guilib.ecs;

import com.troblecodings.core.interfaces.INetworkSync;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
    }

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

    private boolean dataSend = false;

    @Override
    public void detectAndSendChanges() {
        if (!dataSend) {
            sendAllDataToRemote();
            dataSend = true;
        }
    }

    public void sendAllDataToRemote() {
    }
}