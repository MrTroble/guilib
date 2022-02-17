package eu.gir.guilib.ecs.entitys;

import net.minecraft.client.renderer.GlStateManager;

public class UIScale extends UIComponent {
	
	private float scaleX, scaleY, scaleZ;
	
	public UIScale() {
		this.scaleX = 1;
		this.scaleY = 1;
		this.scaleZ = 1;
	}
	
	public UIScale(float scaleX, float scaleY, float scaleZ) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
	}
	
	@Override
	public void update() {
		
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	public float getScaleZ() {
		return scaleZ;
	}
	
	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}
	
}
