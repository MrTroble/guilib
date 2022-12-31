package com.troblecodings.core.interfaces;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Nameable;

public interface NamableWrapper extends Nameable, StringRepresentable {

	default String getNameAsStringWrapper() {
		return getName().getContents();
	}
	
	String getNameWrapper();
	
	@Override
	default Component getName() {
		return new TranslatableComponent(getNameWrapper());
	}
	
	@Override
	default String getSerializedName() {
		return getNameWrapper();
	}
}
