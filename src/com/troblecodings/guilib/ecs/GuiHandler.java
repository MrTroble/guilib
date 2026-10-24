package com.troblecodings.guilib.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.MenuScreens;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;

public final class GuiHandler {

    private final String modid;

    public GuiHandler(final String modid, final Logger logger, final IEventBus eventBus) {
        this.modid = modid;
        eventBus.register(new RegisterHolder());
        if (net.neoforged.fml.loading.FMLEnvironment.dist == net.neoforged.api.distmarker.Dist.CLIENT)
            eventBus.addListener(this::registerScreens);
    }

    private final Map<Class<?>, MenuType<ContainerBase>> guiIDS = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends GuiBase>> guiBases = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends ContainerBase>> guiContainer = new HashMap<>();

    public <T> void addGui(final Class<T> clazz, final Function<GuiInfo, ? extends GuiBase> gui) {
        if (!guiIDS.containsKey(clazz))
            throw new IllegalArgumentException("Register server side before client!");
        guiBases.put(clazz, gui);
    }

    private void registerScreens(final RegisterMenuScreensEvent event) {
        guiBases.forEach((clazz, gui) -> {
            final MenuScreens.ScreenConstructor<ContainerBase, GuiBase> constructor =
                    (base, inventory, component) -> gui.apply(base.getInfo().with(component));
            event.register(guiIDS.get(clazz), constructor);
        });
    }

    public <T> void addServer(final Class<T> clazz,
            final Function<GuiInfo, ? extends ContainerBase> gui) {
        guiContainer.put(clazz, gui);
        guiIDS.put(clazz,
                new MenuType<>((id,
                        inventory) -> gui.apply(new GuiInfo(guiIDS.get(clazz), id,
                                inventory.player.level(), null, inventory.player, inventory)),
                        FeatureFlags.VANILLA_SET));
    }

    public <T> void invokeGui(final Class<T> clazz, final Player mcPlayer, final Level world,
            final BlockPos pos, final String name) {
        mcPlayer.openMenu(
                new SimpleMenuProvider(
                        (id, inventory, player) -> guiContainer.get(clazz)
                                .apply(new GuiInfo(guiIDS.get(clazz), id, world, pos, player,
                                        inventory).with(Component.literal(name))),
                        Component.literal(name)));
    }

    private final class RegisterHolder {
        @SubscribeEvent
        public void registerMenuType(final RegisterEvent event) {
            event.register(Registries.MENU, registry -> guiIDS.forEach((clazz, type) ->
                    registry.register(ResourceLocation.fromNamespaceAndPath(modid,
                            clazz.getTypeName().toLowerCase(java.util.Locale.ROOT)), type)));
        }
    }

}