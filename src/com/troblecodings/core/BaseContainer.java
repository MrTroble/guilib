package com.troblecodings.core;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class BaseContainer extends AbstractContainerMenu {

	public static class BaseContainerInfo {
		public MenuType<?> type;
		public int id;
	}

	protected BaseContainer(BaseContainerInfo info) {
		super(info.type, info.id);
	}

	@Override
	public boolean stillValid(Player playerIn) {
		return false;
	}

}
