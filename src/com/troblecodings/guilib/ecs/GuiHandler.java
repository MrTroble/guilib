package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import com.troblecodings.core.BaseContainer.BaseContainerInfo;

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

    public static class GuiCreateInfo {
    	
    }
    
    @SuppressWarnings("rawtypes")
	private final Map<Class<?>, MenuType> guiIDS = new HashMap<>();
    private final List<Function<GuiCreateInfo, ? extends GuiBase>> guiBases = new ArrayList<>();
    private final List<Function<GuiCreateInfo, ? extends ContainerBase>> guiContainer = new ArrayList<>();
    
    public <T> void addGui(final Class<T> clazz, final Function<GuiCreateInfo, ? extends GuiBase> gui) {
    }

    public <T> void addServer(final Class<T> clazz, final Function<GuiCreateInfo, ? extends ContainerBase> gui) {
    }

    public <T> void invokeGui(final Class<T> clazz, final Player player, final Level world,
            final BlockPos pos) {
    }
    
    public <T> BaseContainerInfo getContainerInfo(final Class<T> clazz) {
    	return new BaseContainerInfo();
    }
}
