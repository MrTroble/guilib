package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIColor extends UIComponent {

	private int color;

	public UIColor(int color) {
		this.setColor(color);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		GuiUtils.drawGradientRect(0, 0, 0, parent.getWidth(), parent.getHeight(), this.color, this.color);
	}

	@Override
	public void update() {

	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
