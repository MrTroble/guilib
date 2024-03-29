package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GuiHandler implements IGuiHandler {

    private final String modid;
    private final Logger logger;

    public GuiHandler(final String modid, final Logger logger) {
        this.modid = modid;
        this.logger = logger;
    }

    @FunctionalInterface
    public static interface GuiSupplier {

        Object get(EntityPlayer player, World world, BlockPos pos);
    }

    private final HashMap<Class<?>, Integer> guiIDS = new HashMap<>();
    private final ArrayList<GuiSupplier> guiBases = new ArrayList<>();
    private final ArrayList<GuiSupplier> guiContainer = new ArrayList<>();

    @Override
    public Object getServerGuiElement(final int id, final EntityPlayer player, final World world,
            final int x, final int y, final int z) {
        if (guiContainer.size() > id) {
            return guiContainer.get(id).get(player, world, new BlockPos(x, y, z));
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(final int id, final EntityPlayer player, final World world,
            final int x, final int y, final int z) {
        if (guiBases.size() > id) {
            return guiBases.get(id).get(player, world, new BlockPos(x, y, z));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T, R> void addGui(final Class<T> clazz, final Class<R> tileClass,
            final Function<R, Object> object) {
        addGui(clazz, (p, w, bp) -> {
            final TileEntity entity = w.getTileEntity(bp);
            if (!tileClass.isInstance(entity)) {
                logger.warn("Could not get tileentity for {}!", clazz);
                return null;
            }
            return object.apply((R) entity);
        });
    }

    public <T> void addGui(final Class<T> clazz, final GuiSupplier gui) {
        addGui(clazz, gui, guiBases, guiContainer);
    }

    public <T> void addServer(final Class<T> clazz, final GuiSupplier gui) {
        addGui(clazz, gui, guiContainer, guiBases);
    }

    private <T> int addGui(final Class<T> clazz, final GuiSupplier gui,
            final ArrayList<GuiSupplier> base, final ArrayList<GuiSupplier> container) {
        if (guiIDS.containsKey(clazz)) {
            final int id = guiIDS.get(clazz);
            if (gui != null)
                base.set(id, gui);
            return id;
        }
        final int size = guiBases.size();
        base.add(gui);
        container.add((p, w, d) -> null);
        guiIDS.put(clazz, size);
        return size;
    }

    public <T> void invokeGui(final Class<T> clazz, final EntityPlayer player, final World world,
            final BlockPos pos) {
        player.openGui(modid, guiIDS.get(clazz), world, pos.getX(), pos.getY(), pos.getZ());
    }
}
