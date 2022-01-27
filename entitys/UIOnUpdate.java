package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIOnUpdate extends UIComponent {
	
	private Runnable onUpdate;
	
	public UIOnUpdate(Runnable onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	public Runnable getOnUpdate() {
		return onUpdate;
	}
	
	public void setOnUpdate(Runnable onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
	}
	
	@Override
	public void update() {
		onUpdate.run();
	}
	
}
