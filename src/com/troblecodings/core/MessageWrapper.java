package com.troblecodings.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public interface MessageWrapper {

    default void messageWrapper(final Player player, final Component message) {
        internalMessage(player, message);
    }

    default void translateMessageWrapper(final Player player, final String message) {
        internalMessage(player, Component.translatable(message));
    }

    default void internalMessage(final Player player, final Component comp) {
        player.sendSystemMessage(comp);
    }

}
