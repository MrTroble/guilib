package com.troblecodings.guilib.ecs.entitys;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;

public class DrawInfo {
    public final int mouseX;
    public final int mouseY;
    public final MatrixStack stack;
    public final float tick;

    public DrawInfo(final int mouseX, final int mouseY, final MatrixStack stack, final float tick) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.stack = stack;
        this.tick = tick;
    }

    public void applyColor() {
        color();
    }

    public void push() {
        this.stack.pushPose();
    }

    public void pop() {
        this.stack.popPose();
    }

    public void translate(final double x, final double y, final double z) {
        this.stack.translate(x, y, z);
    }

    public void scale(final double x, final double y, final double z) {
        this.stack.scale((float) x, (float) y, (float) z);
    }

    public void rotate(final Quaternion quaternion) {
        this.stack.mulPose(quaternion);
    }

    public void applyTexture(final ResourceLocation location) {
        Minecraft.getInstance().getTextureManager().bind(location);
    }

    public void color() {
        this.color(1, 1, 1, 1);
    }

    public void color(final int color) {
        this.color((color >> 16 & 255) / 255, (color >> 8 & 255) / 255, (color & 255) / 255, (color >>> 24) / 255);
    }

    @SuppressWarnings("deprecation")
    public void color(final double r, final double g, final double b, final double a) {
        RenderSystem.color4f((float) r, (float) g, (float) b, (float) a);
    }

    public void blendOn() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public void blendOff() {
        RenderSystem.disableBlend();
    }

    public void depthOn() {
        RenderSystem.enableDepthTest();
    }

    public void depthOff() {
        RenderSystem.disableDepthTest();
    }

    public void scissorOn(final int x, final int y, final int width, final int height) {
        RenderSystem.enableScissor(x, y, width, height);
    }

    public void scissorOff() {
        RenderSystem.disableScissor();
    }
    
    public void alphaOn() {
        RenderSystem.enableAlphaTest();
    }
    
    public void alphaOff() {
        RenderSystem.disableAlphaTest();
    }

    public void singleLine(final int color, final BufferWrapper wrapper, final float xLeft,
            final float xRight, final float yTop, final float yBottom, final float width) {
        final double deltaX = xLeft - xRight;
        final double deltaY = yTop - yBottom;
        final double hypot = Math.hypot(deltaX, deltaY);
        final float normalX = Math.abs((float) (deltaX / hypot)) * (width / 2);
        final float normalY = (float) (deltaY / hypot) * (width / 2);
        wrapper.pos(xLeft - normalY, yTop - normalX, 0).end();
        wrapper.pos(xLeft + normalY, yTop + normalX, 0).end();
        wrapper.pos(xRight - normalY, yBottom - normalX, 0).end();
        wrapper.pos(xRight - normalY, yBottom - normalX, 0).end();
        wrapper.pos(xLeft + normalY, yTop + normalX, 0).end();
        wrapper.pos(xRight + normalY, yBottom + normalX, 0).end();
    }

    public void lines(final int color, final float width, final float[] lines) {
        this.color(color);
        final BufferWrapper bufferbuilder = this.builder(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        for (int i = 0; i < lines.length; i += 4) {
            singleLine(color, bufferbuilder, lines[i], lines[i + 2], lines[i + 1], lines[i + 3],
                    width);
        }
        this.end();
        this.color();
    }

    public BufferWrapper builder(final int mode, final VertexFormat format) {
        final BufferBuilder builder = Tessellator.getInstance().getBuilder();
        builder.begin(mode, format);
        return new BufferWrapper(builder, this.stack.last().pose());
    }

    public void end() {
        Tessellator.getInstance().end();
    }
}