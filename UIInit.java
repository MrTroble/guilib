package eu.gir.girsignals.guis.guilib;

import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class UIInit {

	private static FMLEventChannel channel;

	public static FMLEventChannel getChannel() {
		return channel;
	}

	public static final String CHANNELNAME = "gir|guisyncnet";

	public static void initCommon(String modid) {
		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNELNAME);
		channel.register(new GuiSyncNetwork());
		NetworkRegistry.INSTANCE.registerGuiHandler(modid, GuiHandler.getInstance(modid));
	}

}
