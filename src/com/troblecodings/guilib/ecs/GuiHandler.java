package com.troblecodings.guilib.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public final class GuiHandler {

    private final String modid;
    private final Logger logger;

    public GuiHandler(final String modid, final Logger logger) {
        this.modid = modid;
        this.logger = logger;
    }

    public static class GuiCreateInfo {
        public final Level world;
        public final BlockPos pos;
        public final Player player;
        public final MenuType<?> type;

        public GuiCreateInfo(final Level world, final BlockPos pos, final Player player,
                final MenuType<?> type) {
            super();
            this.world = world;
            this.pos = pos;
            this.player = player;
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        public <T> T getTile() {
            return (T) world.getBlockEntity(pos);
        }
    }

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, MenuType> guiIDS = new HashMap<>();
    private final List<Function<GuiCreateInfo, ? extends GuiBase>> guiBases = new ArrayList<>();
    private final List<Function<GuiCreateInfo, ? extends ContainerBase>> guiContainer = new ArrayList<>();

    public <T> void addGui(final Class<T> clazz,
            final Function<GuiCreateInfo, ? extends GuiBase> gui) {
        guiBases.add(gui);
    }

    public <T> void addServer(final Class<T> clazz,
            final Function<GuiCreateInfo, ? extends ContainerBase> gui) {
        guiContainer.add(gui);
        guiIDS.put(clazz, new MenuType<>((id, inventory) -> {
            return new ContainerBase(new GuiCreateInfo(inventory.player.getLevel(), null,
                    inventory.player, guiIDS.get(clazz)));
        }));

    }

    public <T> void invokeGui(final Class<T> clazz, final Player player, final Level world,
            final BlockPos pos) {
        player.openMenu(new MenuProvider() {

            @Override
            public AbstractContainerMenu createMenu(final int id, final Inventory inventory,
                    final Player player) {
                return new AbstractContainerMenu(guiIDS.get(clazz), id) {

                    @Override
                    public boolean stillValid(final Player player) {
                        return true;
                    }
                };
            }

            @Override
            public Component getDisplayName() {
                return new TextComponent(clazz.getTypeName());
            }
        });
    }
}