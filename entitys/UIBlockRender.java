package eu.gir.girsignals.guis.guilib.entitys;

import org.lwjgl.opengl.GL11;

import eu.gir.girsignals.guis.guilib.DrawUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class UIBlockRender extends UIComponent {
	
	private ThreadLocal<BufferBuilder> model = ThreadLocal.withInitial(() -> new BufferBuilder(500));
	
	@Override
	public void draw(int mouseX, int mouseY) {
		final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		manager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(-0.5f, -3.5f, -0.5f);
		DrawUtil.draw(model.get());
		GlStateManager.disableRescaleNormal();
	}
	
	@Override
	public void update() {
	}
	
	public void setBlockState(final IBlockState state) {
		final BufferBuilder builder = model.get();
		final BlockModelShapes shapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		DrawUtil.addToBuffer(builder, shapes, state);
		builder.finishDrawing();
	}
	
}