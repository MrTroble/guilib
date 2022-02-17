package eu.gir.guilib.ecs.entitys;

import net.minecraft.client.renderer.GlStateManager;

public class UIRotate extends UIComponent {
	
	private float rotateX, rotateY, rotateZ;
	
	@Override
	public void draw(int mouseX, int mouseY) {
		GlStateManager.rotate(this.rotateX, 1, 0, 0);
		GlStateManager.rotate(this.rotateY, 0, 1, 0);
		GlStateManager.rotate(this.rotateZ, 0, 0, 1);
	}
	
	@Override
	public void update() {
	}
	
	public float getRotateX() {
		return rotateX;
	}
	
	public void setRotateX(float rotateX) {
		this.rotateX = rotateX;
	}
	
	public float getRotateY() {
		return rotateY;
	}
	
	public void setRotateY(float rotateY) {
		this.rotateY = rotateY;
	}
	
	public float getRotateZ() {
		return rotateZ;
	}
	
	public void setRotateZ(float rotateZ) {
		this.rotateZ = rotateZ;
	}
	
}
