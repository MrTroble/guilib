package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent.ClientCustomPayloadEvent;
import net.minecraftforge.network.NetworkEvent.ServerCustomPayloadEvent;

public class GuiSyncNetwork {

    public static final byte SEND_TO_ITEM = 0;
    public static final byte SEND_TO_POS = 1;
    public static final int MAX_PACKET = 32600;

    private static final HashMap<Integer, ArrayList<Packet>> PACKET_QUEUE = new HashMap<>();

    // TODO rewrite networking
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onCustomClientPacket(final ClientCustomPayloadEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        final Player player = mc.player;
        final AbstractContainerMenu container = player.containerMenu;
    }

    @SubscribeEvent
    public void onCustomPacket(final ServerCustomPayloadEvent event) {
    }

    public static void sendToItemServer(final CompoundTag compound) {
    }

    public static void sendToPosServer(final CompoundTag compound, final BlockPos pos) {
    }

    public static void sendToClient(final CompoundTag compound, final Player player) {
        if (player == null)
            return;
    }

}
