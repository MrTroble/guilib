package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraft.client.renderer.GlStateManager;

public class UIIndependentTranslate extends UIComponent {
	
	private double x, y, z;
	
	public UIIndependentTranslate() {
	}
	
	public UIIndependentTranslate(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		GlStateManager.translate(x, y, z);
	}
	
	@Override
	public void update() {
	}
	
}
