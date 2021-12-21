package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIHBox extends UIComponent {

	private int hGap = 0;

	public UIHBox(final int hGap) {
		this.hGap = hGap;
	}

	@Override
	public void update() {
		int x = 0;
		for (final UIEntity entity : parent.children) {
			entity.x = x;
			x += entity.width + hGap;
		}
	}

	@Override
	public void onAdd(UIEntity entity) {
		super.onAdd(entity);
		update();
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

}
