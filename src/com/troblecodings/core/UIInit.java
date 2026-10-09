package com.troblecodings.core;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import net.neoforged.bus.api.IEventBus;

import com.google.common.collect.Maps;
import com.troblecodings.core.net.NetworkHandler;
import com.troblecodings.guilib.ecs.GuiHandler;

public final class UIInit {

    private UIInit() {
    }

    private static boolean debug;

    public static final String CHANNELNAME = "guisyncnet";

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     *
     * @param modid , the modid of the owning mod
     */
    public static Map.Entry<GuiHandler, NetworkHandler> initCommon(final String modid,
            final Logger logger, final IEventBus eventBus) {
        return initCommon(modid, logger, false, eventBus);
    }

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     *
     * @param modid , the modid of the owning mod
     * @param debug , whether debugging should be enabled or not
     */
    public static Map.Entry<GuiHandler, NetworkHandler> initCommon(final String modid,
            final Logger logger, final boolean debug, final IEventBus eventBus) {
        UIInit.debug = debug;
        return Maps.immutableEntry(new GuiHandler(modid, logger, eventBus),
                new NetworkHandler(modid, logger, eventBus));
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