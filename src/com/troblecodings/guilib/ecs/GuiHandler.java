package com.troblecodings.guilib.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public final class GuiHandler {

    private final String modid;
    @SuppressWarnings("unused")
    private final Logger logger;

    public GuiHandler(final String modid, final Logger logger) {
        this.modid = modid;
        this.logger = logger;
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(new RegisterHolder());
    }

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, MenuType> guiIDS = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends GuiBase>> guiBases = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends ContainerBase>> guiContainer = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void addGui(final Class<T> clazz, final Function<GuiInfo, ? extends GuiBase> gui) {
        if (!guiIDS.containsKey(clazz))
            throw new IllegalArgumentException("Register server side before client!");
        guiBases.put(clazz, gui);
        final MenuScreens.ScreenConstructor<ContainerBase, GuiBase> constructor = (base, inventory,
                component) -> gui.apply(base.getInfo().with(component));
        MenuScreens.register(guiIDS.get(clazz), constructor);
    }

    public <T> void addServer(final Class<T> clazz,
            final Function<GuiInfo, ? extends ContainerBase> gui) {
        guiContainer.put(clazz, gui);
        guiIDS.put(clazz,
                new MenuType<>(
                        (id, inventory) -> gui.apply(new GuiInfo(guiIDS.get(clazz), id,
                                inventory.player.getLevel(), null, inventory.player, inventory)),
                        FeatureFlagSet.of()));
    }

    public <T> void invokeGui(final Class<T> clazz, final Player mcPlayer, final Level world,
            final BlockPos pos, final String name) {
        mcPlayer.openMenu(
                new SimpleMenuProvider(
                        (id, inventory, player) -> guiContainer.get(clazz)
                                .apply(new GuiInfo(guiIDS.get(clazz), id, world, pos, player,
                                        inventory).with(Component.translatable(name))),
                        Component.translatable(name)));
    }

    private final class RegisterHolder {

        @SubscribeEvent
        public void registerMenuType(final RegisterEvent event) {
            event.register(Registries.MENU,
                    holder -> guiIDS.forEach((clazz, menuType) -> holder.register(
                            new ResourceLocation(modid, clazz.getTypeName().toLowerCase()),
                            menuType)));
        }
    }
}