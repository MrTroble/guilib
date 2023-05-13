package com.troblecodings.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface MessageWrapper {

    default void messageWrapper(final PlayerEntity player, final String message) {
        internalMessage(player,new StringTextComponent(message));
    }

    default void translateMessageWrapper(final PlayerEntity player, final String message) {
        internalMessage(player, new TranslationTextComponent(message));
    }

    default void internalMessage(final PlayerEntity player, final ITextComponent comp) {
        player.sendMessage(comp, player.getUUID());
    }

}
