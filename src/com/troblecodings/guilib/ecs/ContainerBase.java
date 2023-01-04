package com.troblecodings.guilib.ecs;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerBase extends AbstractContainerMenu {

    public ContainerBase(final GuiInfo info) {
        super(info.type, info.id);// TODO
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}