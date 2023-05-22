package com.troblecodings.core.interfaces;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IWorldNameable;

public interface NamableWrapper extends IWorldNameable, IStringSerializable {

    default String getNameAsStringWrapper() {
        return getName();
    }

    String getNameWrapper();

    @Override
    default String getName() {
        return getNameWrapper();
    }

    @Override
    default boolean hasCustomName() {
        return getNameWrapper() != null && !getNameWrapper().isEmpty();
    }

    @Override
    default ITextComponent getDisplayName() {
        return new TextComponentString(getNameWrapper());
    }
}
