package eu.gir.girsignals.guis.guilib;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GuiHandler implements IGuiHandler {

	private GuiHandler() {
	}

	private static final GuiHandler INSTANCE = new GuiHandler();
	private static String modid;

	@FunctionalInterface
	public static interface GuiSupplier {
		Object get(EntityPlayer player, World world, BlockPos pos);
	}

	private static HashMap<Class<?>, Integer> guiIDS = new HashMap<>();
	private static ArrayList<GuiSupplier> guiBases = new ArrayList<>();
	private static ArrayList<GuiSupplier> guiContainer = new ArrayList<>();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (guiBases.size() > ID) {
			return guiBases.get(ID).get(player, world, new BlockPos(x, y, z));
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

	public static <T> void addGui(Class<T> clazz, GuiSupplier gui) {
		addGui(clazz, gui, guiBases);
	}

	public static <T> void addServer(Class<T> clazz, GuiSupplier gui) {
		addGui(clazz, gui, guiContainer);
	}

	public static <T> int addGui(Class<T> clazz, GuiSupplier gui, ArrayList<GuiSupplier> guiBases) {
		if (guiIDS.containsKey(clazz)) {
			int id = guiIDS.get(clazz);
			if (gui != null)
				guiBases.set(id, gui);
			return id;
		}
		int size = guiBases.size();
		guiBases.add(gui);
		guiIDS.put(clazz, size);
		return size;
	}

	public static <T> void invokeGui(Class<T> clazz, EntityPlayer player, World world, BlockPos pos) {
		player.openGui(modid, guiIDS.get(clazz), world, pos.getX(), pos.getY(), pos.getZ());
	}

	public static GuiHandler getInstance(String modid) {
		GuiHandler.modid = modid;
		return INSTANCE;
	}
}
