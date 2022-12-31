package com.troblecodings.guilib.ecs.interfaces;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Nameable;

public interface NamableWrapper extends Nameable {

	default String getNameAsStringWrapper() {
		return getName().getContents();
	}
	
	String getNameWrapper();
	
	@Override
	default Component getName() {
		return new TranslatableComponent(getNameWrapper());
	}
}
