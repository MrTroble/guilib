package eu.gir.guilib.ecs.interfaces;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIntegerable<T> {
	
	public T getObjFromID(int obj);
	
	public int count();
	
	public String getName();
	
	@SideOnly(Side.CLIENT)
	default public String getLocalizedName() {
		return I18n.format("property." + this.getName() + ".name");
	}
	
	@SideOnly(Side.CLIENT)
	default public String getNamedObj(int obj) {
		return getLocalizedName() + ": " + I18n.format("property.value." + getObjFromID(obj));
	}
	
	@SideOnly(Side.CLIENT)
	default public String getDescription() {
		return I18n.format("property." + this.getName() + ".desc");
	}
	
	@SideOnly(Side.CLIENT)
	default public int getMaxWidth(final FontRenderer render) {
		int ret = 0;
		for (int i = 0; i < this.count(); i++) {
			final int newVal = render.getStringWidth(getNamedObj(i));
			if (ret < newVal)
				ret = newVal;
		}
		return ret;
	}
	
}
