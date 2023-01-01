package com.troblecodings.guilib.ecs;

import com.troblecodings.guilib.ecs.GuiHandler.GuiCreateInfo;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerBase extends AbstractContainerMenu {

	protected ContainerBase(final GuiCreateInfo info) {
		super(info.type, 0);// TODO
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

}
