package com.troblecodings.core;

import java.util.Map;

import org.apache.logging.log4j.Logger;

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
            final Logger logger) {
        return initCommon(modid, logger, false);
    }

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     *
     * @param modid , the modid of the owning mod
     * @param debug , whether debugging should be enabled or not
     */
    public static Map.Entry<GuiHandler, NetworkHandler> initCommon(final String modid,
            final Logger logger, final boolean debug) {
        UIInit.debug = debug;
        return Maps.immutableEntry(new GuiHandler(modid, logger),
                new NetworkHandler(modid, logger));
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
