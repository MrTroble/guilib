package eu.gir.girsignals.guis.guilib.entitys;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class UIBorder extends UIComponent {
	
	private int color;
	private float lineWidth;
	
	public UIBorder(int color) {
		this(color, 1);
	}
	
	public UIBorder(int color, float lineWidth) {
		this.color = color;
		this.lineWidth = lineWidth;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GL11.glLineWidth(this.lineWidth);
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(f, f1, f2, f3);
		bufferbuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(0, (double) parent.getHeight(), 0.0D).endVertex();
		bufferbuilder.pos((double) parent.getWidth(), (double) parent.getHeight(), 0.0D).endVertex();
		bufferbuilder.pos((double) parent.getWidth(), 0, 0.0D).endVertex();
		bufferbuilder.pos(0, 0, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
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
	
	public float getLineWidth() {
		return lineWidth;
	}
	
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
}
