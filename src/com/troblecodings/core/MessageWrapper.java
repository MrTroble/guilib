package com.troblecodings.core;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

public interface MessageWrapper {

    default void messageWrapper(final Player player, final String message) {
        internalMessage(player, new TextComponent(message));
    }

    default void translateMessageWrapper(final Player player, final String message) {
        internalMessage(player, new TranslatableComponent(message));
    }

    default void internalMessage(final Player player, final Component comp) {
        player.sendMessage(comp, player.getUUID());
    }

}
