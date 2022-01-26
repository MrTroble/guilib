package eu.gir.girsignals.guis.guilib.entitys;

import org.lwjgl.opengl.GL11;

import eu.gir.girsignals.guis.guilib.entitys.UIEntity.UpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIScissor extends UIComponent {

	private int x;
	private int y;
	private int width;
	private int height;

	@Override
	public void draw(int mouseX, int mouseY) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(x, y, width, height);
	}

	@Override
	public void update() {
		final UpdateEvent lastUpdateEvent = parent.getLastUpdateEvent();
		this.height = parent.getHeight() * lastUpdateEvent.scaleFactor + lastUpdateEvent.scaleFactor;
		this.width = parent.getWidth() * lastUpdateEvent.scaleFactor;
		this.x = parent.getWorldX() * lastUpdateEvent.scaleFactor;
		this.y = (lastUpdateEvent.height - parent.getWorldY() - parent.getHeight()) * lastUpdateEvent.scaleFactor;
	}
	
}
