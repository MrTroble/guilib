package com.troblecodings.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public interface MessageWrapper {

    default void messageWrapper(final EntityPlayer player, final String message) {
        internalMessage(player, new TextComponentString(message));
    }

    default void translateMessageWrapper(final EntityPlayer player, final String message) {
        internalMessage(player, new TextComponentTranslation(message));
    }

    default void internalMessage(final EntityPlayer player, final ITextComponent comp) {
        player.sendMessage(comp);
    }

}
