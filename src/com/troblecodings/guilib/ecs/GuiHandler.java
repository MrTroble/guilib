package com.troblecodings.guilib.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

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
    private final Map<Class<?>, ContainerType> guiIDS = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends GuiBase>> guiBases = new HashMap<>();
    private final Map<Class<?>, Function<GuiInfo, ? extends ContainerBase>> guiContainer = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void addGui(final Class<T> clazz, final Function<GuiInfo, ? extends GuiBase> gui) {
        if (!guiIDS.containsKey(clazz))
            throw new IllegalArgumentException("Register server side before client!");
        guiBases.put(clazz, gui);
        final IScreenFactory<ContainerBase, GuiBase> constructor = (container, inventory,
                component) -> {
            return gui.apply(
                    new GuiInfo(guiIDS.get(clazz), 0, inventory.player.getCommandSenderWorld(),
                            inventory.player.blockPosition(), inventory.player, inventory)
                                    .with(component));
        };
        ScreenManager.register(guiIDS.get(clazz), constructor);
    }

    public <T> void addServer(final Class<T> clazz,
            final Function<GuiInfo, ? extends ContainerBase> gui) {
        guiContainer.put(clazz, gui);
        guiIDS.put(clazz,
                new ContainerType<>((id,
                        inventory) -> gui.apply(new GuiInfo(guiIDS.get(clazz), id,
                                inventory.player.getCommandSenderWorld(), null, inventory.player,
                                inventory))).setRegistryName(modid,
                                        clazz.getTypeName().toLowerCase()));
    }

    public <T> void invokeGui(final Class<T> clazz, final PlayerEntity mcPlayer, final World world,
            final BlockPos pos, final String name) {
        mcPlayer.openMenu(
                new SimpleNamedContainerProvider(
                        (id, inventory, player) -> guiContainer.get(clazz)
                                .apply(new GuiInfo(guiIDS.get(clazz), id, world, pos, player,
                                        inventory).with(new StringTextComponent(name))),
                        new StringTextComponent(name)));
    }

    private final class RegisterHolder {
        @SubscribeEvent
        public void registerMenuType(final RegistryEvent.Register<ContainerType<?>> event) {
            final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
            guiIDS.values().forEach(registry::register);
        }
    }
}