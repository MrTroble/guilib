package com.troblecodings.guilib.ecs;

import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class UIInit {

    private UIInit() {
    }

    private static SimpleChannel channel;
    private static boolean debug;

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static final String CHANNELNAME = "guisyncnet";

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     * 
     * @param modid , the modid of the owning mod
     */
    public static void initCommon(final String modid, final Logger logger) {
        initCommon(modid, logger, false);
    }

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     * 
     * @param modid , the modid of the owning mod
     * @param debug , whether debugging should be enabled or not
     */
    public static GuiHandler initCommon(final String modid, final Logger logger,
            final boolean debug) {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modid, CHANNELNAME), () -> "1", _u -> true, _u -> true);
        UIInit.debug = debug;
        final GuiHandler handler = new GuiHandler(modid, logger);
        return handler;
    }

    /**
     * Checks if debugging is enabled or not
     * 
     * @return true if debug is enabled false otherwise
     */
    public static boolean isDebugEnabled() {
        return debug;
    }
}
