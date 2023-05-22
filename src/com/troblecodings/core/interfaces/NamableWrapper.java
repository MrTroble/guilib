package com.troblecodings.core.interfaces;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldNameable;

public interface NamableWrapper extends IWorldNameable, IStringSerializable {

    default String getNameAsStringWrapper() {
        return getName().getContents();
    }

    String getNameWrapper();

    @Override
    default ITextComponent getName() {
        return new TranslationTextComponent(getNameWrapper());
    }

    @Override
    default String getSerializedName() {
        return getNameWrapper();
    }
}
