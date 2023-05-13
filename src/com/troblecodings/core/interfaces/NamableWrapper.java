package com.troblecodings.core.interfaces;

import net.minecraft.util.INameable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TranslationTextComponent;

public interface NamableWrapper extends INameable, IStringSerializable {

    default String getNameAsStringWrapper() {
        return getName().getContents();
    }

    String getNameWrapper();

    @Override
    default TranslationTextComponent getName() {
        return new TranslationTextComponent(getNameWrapper());
    }

    @Override
    default String getSerializedName() {
        return getNameWrapper();
    }
}
