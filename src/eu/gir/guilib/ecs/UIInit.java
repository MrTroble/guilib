package eu.gir.guilib.ecs;

import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public final class UIInit {

    private UIInit() {
    }

    private static FMLEventChannel channel;
    private static boolean debug;

    public static FMLEventChannel getChannel() {
        return channel;
    }

    public static final String CHANNELNAME = "gir|guisyncnet";

    /**
     * Initializes the GUI system must be called by the mod in it's according
     * initialization
     * 
     * @param modid , the modid of the owning mod
     * @param debug , whether debugging should be enabled or not
     */
    public static void initCommon(final String modid, final boolean debug) {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNELNAME);
        channel.register(new GuiSyncNetwork());
        UIInit.debug = debug;
        NetworkRegistry.INSTANCE.registerGuiHandler(modid, GuiHandler.getInstance(modid));
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
