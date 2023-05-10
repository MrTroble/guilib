package com.troblecodings.guilib.ecs.entitys;

import java.util.concurrent.atomic.AtomicReference;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.troblecodings.guilib.ecs.DrawUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UIBlockRender extends UIComponent {

    private final AtomicReference<BufferBuilder> model = new AtomicReference<>(
            new BufferBuilder(500));

    private final TextureManager manager;
    private final BlockModelShaper shapes;
    private AbstractTexture texture;
    private Vec3 vector;

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
        info.stack.translate(-0.5f + vector.x, -0.5f + vector.y, -0.5f + vector.z);
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
        this.vector = new Vec3(x, y, z);
        this.texture = manager.getTexture(state.getBlock().getRegistryName());
        final BufferBuilder builder = model.get();
        builder.begin(Mode.QUADS, DefaultVertexFormat.BLOCK);
        DrawUtil.addToBuffer(builder, shapes, state);
        builder.building();
    }

}
