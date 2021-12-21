package eu.gir.girsignals.guis.guilib;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GuiHandler implements IGuiHandler {

	private GuiHandler() {}
	private static final GuiHandler INSTANCE = new GuiHandler();
	private static String modid;

	@FunctionalInterface
	public static interface GuiSupplier<T> {
		T get(EntityPlayer player, World world, BlockPos pos);
	}
	
	private static HashMap<Class<?>, Integer> guiIDS = new HashMap<>();
	private static ArrayList<GuiSupplier<GuiBase>> guiBases = new ArrayList<>();
	private static ArrayList<GuiSupplier<Container>> guiContainer = new ArrayList<>();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(guiContainer.size() > ID) {
			final GuiSupplier<Container> supplier = guiContainer.get(ID);
			if(supplier != null)
				return supplier.get(player, world, new BlockPos(x, y, z));
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (guiBases.size() > ID) {
			return guiBases.get(ID).get(player, world, new BlockPos(x, y, z));
		}
		return null;
	}
	
	public static <T extends GuiBase> void addGui(Class<T> clazz, GuiSupplier<GuiBase> gui) {
		addGui(clazz, gui, null);
	}
	
	public static <T extends GuiBase> void addGui(Class<T> clazz, GuiSupplier<GuiBase> gui, GuiSupplier<Container> container) {
		int size = guiBases.size();
		guiBases.add(gui);
		guiContainer.add(container);
		guiIDS.put(clazz, size);
	}
	
	public static <T extends GuiBase> void invokeGui(Class<T> clazz, EntityPlayer player, World world, BlockPos pos) {
		player.openGui(modid, guiIDS.get(clazz), world, pos.getX(), pos.getY(), pos.getZ());
	}

	public static GuiHandler getInstance(String modid) {
		GuiHandler.modid = modid;
		return INSTANCE;
	}
}
