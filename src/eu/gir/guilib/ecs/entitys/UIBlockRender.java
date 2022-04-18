package eu.gir.guilib.ecs.entitys;

import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.opengl.GL11;

import eu.gir.guilib.ecs.DrawUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class UIBlockRender extends UIComponent {

    private final AtomicReference<BufferBuilder> model = new AtomicReference<BufferBuilder>(
            new BufferBuilder(500));

    @Override
    public void draw(final int mouseX, final int mouseY) {
        final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        manager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        DrawUtil.draw(model.get());
        GlStateManager.disableRescaleNormal();
    }

    @Override
    public void update() {
    }

    public void setBlockState(final IBlockState state) {
        this.setBlockState(state, 0, 0, 0);
    }

    public void setBlockState(final IBlockState state, final double x, final double y,
            final double z) {
        final BufferBuilder builder = model.get();
        final BlockModelShapes shapes = Minecraft.getMinecraft().getBlockRendererDispatcher()
                .getBlockModelShapes();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        builder.setTranslation(x, y, z);
        DrawUtil.addToBuffer(builder, shapes, state);
        builder.finishDrawing();
    }

}
