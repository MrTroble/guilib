package com.troblecodings.guilib.ecs.entitys;

import com.mojang.blaze3d.vertex.BufferBuilder;
import org.joml.Matrix4f;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;

public class BufferWrapper {

    public final BufferBuilder builder;
    private final Matrix4f matrix;

    public BufferWrapper(final BufferBuilder builder, final Matrix4f matrix) {
        super();
        this.builder = builder;
        this.matrix = matrix;
    }

    public BufferWrapper pos(final double x, final double y, final double z) {
        builder.addVertex(matrix, (float) x, (float) y, (float) z);
        return this;
    }

    public BufferWrapper normal(final float x, final float y, final float z) {
        builder.setNormal(x, y, z);
        return this;
    }

    public BufferWrapper color(final int color) {
        return this.color(UIColor.red(color), UIColor.green(color), UIColor.blue(color),
                UIColor.alpha(color));
    }

    public BufferWrapper color(final float r, final float g, final float b, final float a) {
        builder.setColor(r, g, b, a);
        return this;
    }

    public BufferWrapper tex(final double u, final double v) {
        builder.setUv((float) u, (float) v);
        return this;
    }

    public void end() {
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final int color) {
        builder.addVertex(matrix, xRight, yTop, 0).setColor(color);
        builder.addVertex(matrix, xLeft, yTop, 0).setColor(color);
        builder.addVertex(matrix, xLeft, yBottom, 0).setColor(color);
        builder.addVertex(matrix, xRight, yBottom, 0).setColor(color);
        return this;
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final float uStart, final float uEnd, final float vStart,
            final float vEnd) {
        builder.addVertex(matrix, xRight, yTop, 0).setUv((uEnd), (vStart));
        builder.addVertex(matrix, xLeft, yTop, 0).setUv((uStart), (vStart));
        builder.addVertex(matrix, xLeft, yBottom, 0).setUv((uStart), (vEnd));
        builder.addVertex(matrix, xRight, yBottom, 0).setUv((uEnd), (vEnd));
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