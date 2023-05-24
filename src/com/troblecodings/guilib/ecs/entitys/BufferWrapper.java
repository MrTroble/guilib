package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.entitys.render.UIColor;

import net.minecraft.client.renderer.BufferBuilder;

public class BufferWrapper {

    private final BufferBuilder builder;

    public BufferWrapper(final BufferBuilder builder) {
        super();
        this.builder = builder;
    }

    public BufferWrapper pos(final double x, final double y, final double z) {
        builder.pos((float) x, (float) y, (float) z);
        return this;
    }

    public BufferWrapper normal(final float x, final float y, final float z) {
        builder.normal(x, y, z);
        return this;
    }

    public BufferWrapper color(final int color) {
        return this.color(UIColor.red(color), UIColor.green(color), UIColor.blue(color),
                UIColor.alpha(color));
    }

    public BufferWrapper color(final float r, final float g, final float b, final float a) {
        builder.color(r, g, b, a);
        return this;
    }

    public BufferWrapper tex(final double u, final double v) {
        builder.tex((float) u, (float) v);
        return this;
    }

    public void end() {
        builder.endVertex();
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final int color) {
        builder.pos(xRight, yTop, 0).color(UIColor.red(color), UIColor.green(color),
                UIColor.blue(color), UIColor.alpha(color)).endVertex();
        builder.pos(xLeft, yTop, 0).color(UIColor.red(color), UIColor.green(color),
                UIColor.blue(color), UIColor.alpha(color)).endVertex();
        builder.pos(xLeft, yBottom, 0).color(UIColor.red(color), UIColor.green(color),
                UIColor.blue(color), UIColor.alpha(color)).endVertex();
        builder.pos(xRight, yBottom, 0).color(UIColor.red(color), UIColor.green(color),
                UIColor.blue(color), UIColor.alpha(color)).endVertex();
        return this;
    }

    public BufferWrapper quad(final float xLeft, final float xRight, final float yTop,
            final float yBottom, final float uStart, final float uEnd, final float vStart,
            final float vEnd) {
        builder.pos(xRight, yTop, 0).tex((uEnd), (vStart)).endVertex();
        builder.pos(xLeft, yTop, 0).tex((uStart), (vStart)).endVertex();
        builder.pos(xLeft, yBottom, 0).tex((uStart), (vEnd)).endVertex();
        builder.pos(xRight, yBottom, 0).tex((uEnd), (vEnd)).endVertex();
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
