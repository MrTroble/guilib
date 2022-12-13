package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class GuiHandler {

    private final String modid;
    private final Logger logger;

    public GuiHandler(final String modid, final Logger logger) {
        this.modid = modid;
        this.logger = logger;
    }

    @FunctionalInterface
    public static interface GuiSupplier {

        Object get(Player player, Level world, BlockPos pos);
    }

    @SuppressWarnings("rawtypes")
	private final HashMap<Class<?>, MenuType> guiIDS = new HashMap<>();
    private final ArrayList<GuiSupplier> guiBases = new ArrayList<>();
    private final ArrayList<GuiSupplier> guiContainer = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <T, R> void addGui(final Class<T> clazz, final Class<R> tileClass,
            final Function<R, Object> object) {
        addGui(clazz, (p, w, bp) -> {
            final BlockEntity entity = w.getBlockEntity(bp);
            if (!tileClass.isInstance(entity)) {
                logger.warn("Could not get tileentity for {}!", clazz);
                return null;
            }
            return object.apply((R) entity);
        });
    }

    public <T> void addGui(final Class<T> clazz, final GuiSupplier gui) {
    }

    public <T> void addServer(final Class<T> clazz, final GuiSupplier gui) {
    }

    public <T> void invokeGui(final Class<T> clazz, final Player player, final Level world,
            final BlockPos pos) {
    }
}
