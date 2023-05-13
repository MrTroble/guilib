package com.troblecodings.guilib.ecs.entitys;

import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.DrawUtil;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class UIBlockRender extends UIComponent {

    private final AtomicReference<BufferBuilder> model = new AtomicReference<>(
            new BufferBuilder(500));

    private final TextureManager manager;
    private final BlockModelShapes shapes;
    private ITextureObject texture;
    private Vec3d vector;

    public UIBlockRender() {
        final Minecraft mc = Minecraft.getInstance();
        manager = mc.getTextureManager();
        shapes = mc.getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public void draw(final DrawInfo info) {
        if (this.texture == null)
            return;
        this.texture.bind();
        info.stack.translate(-0.5f + vector.x(), -0.5f + vector.y(), -0.5f + vector.z());
        DrawUtil.draw(model.get());
    }

    @Override
    public void update() {
    }

    public void setBlockState(final BlockState state) {
        this.setBlockState(state, 0, 0, 0);
    }

    public void setBlockState(final BlockState state, final double x, final double y,
            final double z) {
        this.vector = new Vec3d(x, y, z);
        this.texture = manager.getTexture(state.getBlock().getRegistryName());
        final BufferBuilder builder = model.get();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        DrawUtil.addToBuffer(builder, shapes, state);
        builder.building();
    }

}
