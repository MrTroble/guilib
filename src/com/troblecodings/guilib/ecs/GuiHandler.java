package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {

    private final String modid;
    @SuppressWarnings("unused")
    private final Logger logger;

    public GuiHandler(final String modid, final Logger logger) {
        this.modid = modid;
        this.logger = logger;
    }

    private final Map<Class<?>, Integer> guiIDS = new HashMap<>();
    private final List<Function<GuiInfo, ? extends GuiBase>> guiBases = new ArrayList<>();
    private final List<Function<GuiInfo, ? extends ContainerBase>> guiContainer = new ArrayList<>();

    public <T> void addGui(final Class<T> clazz, final Function<GuiInfo, ? extends GuiBase> gui) {
        if (!guiIDS.containsKey(clazz))
            throw new IllegalArgumentException("Register server side before client!");
        guiBases.add(gui);
    }

    public <T> void addServer(final Class<T> clazz,
            final Function<GuiInfo, ? extends ContainerBase> gui) {
        guiContainer.add(gui);
        guiIDS.put(clazz, guiIDS.size());
    }

    public <T> void invokeGui(final Class<T> clazz, final EntityPlayer mcPlayer, final World world,
            final BlockPos pos, final String name) {
        mcPlayer.openGui(modid, guiIDS.get(clazz), world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y,
            int z) {
        if (guiContainer.size() > id) {
            return guiContainer.get(id)
                    .apply(new GuiInfo(id, world, new BlockPos(x, y, z), player, player.inventory));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y,
            int z) {
        if (guiBases.size() > id) {
            return guiBases.get(id)
                    .apply(new GuiInfo(id, world, new BlockPos(x, y, z), player, player.inventory));
        }
        return null;
    }
}