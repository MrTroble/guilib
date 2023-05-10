package com.troblecodings.guilib.ecs.entitys;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.vector.Matrix4f;

public class BufferWrapper {

    private final BufferBuilder builder;
    private final Matrix4f matrix;

    public BufferWrapper(final BufferBuilder builder, final Matrix4f matrix) {
        super();
        this.builder = builder;
        this.matrix = matrix;
    }

    public BufferWrapper pos(final double x, final double y, final double z) {
        builder.vertex(matrix, (float) x, (float) y, (float) z);
        return this;
    }

    public BufferWrapper normal(final float x, final float y, final float z) {
        builder.normal(x, y, z);
        return this;
    }

    public BufferWrapper color(final int color) {
        return this.color(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24);
    }

    public BufferWrapper color(final float r, final float g, final float b, final float a) {
        builder.color(r, g, b, a);
        return this;
    }

    public BufferWrapper tex(final double u, final double v) {
        builder.uv((float) u, (float) v);
        return this;
    }

    public void end() {
        builder.endVertex();
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final int color) {
        builder.vertex(matrix, xRight, yTop, 0).color(color, color, color, color).endVertex();
        builder.vertex(matrix, xLeft, yTop, 0).color(color, color, color, color).endVertex();
        builder.vertex(matrix, xLeft, yBottom, 0).color(color, color, color, color).endVertex();
        builder.vertex(matrix, xRight, yBottom, 0).color(color, color, color, color).endVertex();
        return this;
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final float uStart, final float uEnd, final float vStart,
            final float vEnd) {
        builder.vertex(matrix, xRight, yTop, 0).uv((uEnd), (vStart)).endVertex();
        builder.vertex(matrix, xLeft, yTop, 0).uv((uStart), (vStart)).endVertex();
        builder.vertex(matrix, xLeft, yBottom, 0).uv((uStart), (vEnd)).endVertex();
        builder.vertex(matrix, xRight, yBottom, 0).uv((uEnd), (vEnd)).endVertex();
        return this;
    }

    public BufferWrapper quadNonNormalized(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final int uStart, final int uEnd, final int vStart,
            final int vEnd) {
        final float offset = 1f / 256.0f;
        return quad(xLeft, xRight, yTop, yBottom, uStart * offset, uEnd * offset, vStart * offset,
                vEnd * offset);
    }
}
