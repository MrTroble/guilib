package com.troblecodings.core.interfaces;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Nameable;

public interface NamableWrapper extends Nameable, StringRepresentable {

    default ComponentContents getNameAsStringWrapper() {
        return getName().getContents();
    }

    String getNameWrapper();

    @Override
    default Component getName() {
        return Component.translatable(getNameWrapper());
    }

    @Override
    default String getSerializedName() {
        return getNameWrapper();
    }
}
