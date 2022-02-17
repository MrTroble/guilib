package eu.gir.guilib.ecs.entitys.render;

import eu.gir.guilib.ecs.entitys.UIComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UITexture extends UIComponent {
	
	private ResourceLocation texture;
	private double u, v, mu, mv;
	
	public UITexture(TextureAtlasSprite sprite) {
		this.texture = TextureMap.LOCATION_BLOCKS_TEXTURE;
		this.u = sprite.getMinU();
		this.v = sprite.getMinV();
		this.mu = sprite.getMaxU();
		this.mv = sprite.getMaxV();
	}
	
	public UITexture(ResourceLocation texture) {
		this.texture = texture;
		this.mu = 1;
		this.mv = 1;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		final Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(this.texture);
		
		final double w = this.parent.getWidth();
		final double h = this.parent.getHeight();
		
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, h, 0).tex(u, mv).endVertex();
		bufferbuilder.pos(w, h, 0).tex(mu, mv).endVertex();
		bufferbuilder.pos(w, 0, 0).tex(mu, v).endVertex();
		bufferbuilder.pos(0, 0, 0).tex(u, v).endVertex();
		tessellator.draw();
	}
	
	@Override
	public void update() {
	}
	
}
