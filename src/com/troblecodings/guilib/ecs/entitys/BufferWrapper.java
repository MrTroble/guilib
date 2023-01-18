package com.troblecodings.guilib.ecs.entitys;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;

import net.minecraft.util.FastColor;

public class BufferWrapper {

    private final BufferBuilder builder;
    private final Matrix4f matrix;

    public BufferWrapper(BufferBuilder builder, Matrix4f matrix) {
        super();
        this.builder = builder;
        this.matrix = matrix;
    }

    public BufferWrapper pos(double x, double y, double z) {
        builder.vertex(matrix, (float) x, (float) y, (float) z);
        return this;
    }

    public BufferWrapper color(int color) {
        return this.color(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color),
                FastColor.ARGB32.blue(color), FastColor.ARGB32.alpha(color));
    }

    public BufferWrapper color(float r, float g, float b, float a) {
        builder.color(r, g, b, a);
        return this;
    }

    public BufferWrapper tex(double u, double v) {
        builder.uv((float) u, (float) v);
        return this;
    }

    public void end() {
        builder.endVertex();
    }

    public BufferWrapper quad(final int xLeft, final int xRight, final int yTop, final int yBottom,
            int color) {
        builder.vertex(matrix, xRight, yTop, 0).color(color).endVertex();
        builder.vertex(matrix, xLeft, yTop, 0).color(color).endVertex();
        builder.vertex(matrix, xLeft, yBottom, 0).color(color).endVertex();
        builder.vertex(matrix, xRight, yBottom, 0).color(color).endVertex();
        return this;
    }

    public BufferWrapper quad(final int xLeft, final int xRight, final int yTop, final int yBottom,
            float uStart, float uEnd, float vStart, float vEnd) {
        builder.vertex(matrix, xRight, yTop, 0).uv(((float) uEnd), ((float) vStart)).endVertex();
        builder.vertex(matrix, xLeft, yTop, 0).uv(((float) uStart), ((float) vStart)).endVertex();
        builder.vertex(matrix, xLeft, yBottom, 0).uv(((float) uStart), ((float) vEnd)).endVertex();
        builder.vertex(matrix, xRight, yBottom, 0).uv(((float) uEnd), ((float) vEnd)).endVertex();
        return this;
    }

    public BufferWrapper quadNonNormalized(final int xLeft, final int xRight, final int yTop,
            final int yBottom, int uStart, int uEnd, int vStart, int vEnd) {
        final float offset = 1f / 256.0f;
        return quad(xLeft, xRight, yTop, yBottom, uStart * offset, uEnd * offset, vStart * offset,
                vEnd * offset);
    }
}
