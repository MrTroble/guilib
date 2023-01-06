package com.troblecodings.guilib.ecs;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerBase extends AbstractContainerMenu {

    private final GuiInfo info;

    public ContainerBase(final GuiInfo info) {
        super(info.type, info.id);
        info.base = this;
        this.info = info;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public GuiInfo getInfo() {
        return info;
    }
    

}