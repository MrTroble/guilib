package com.troblecodings.guilib.ecs.entitys;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class DrawInfo {
    public final int mouseX;
    public final int mouseY;
    public final float tick;

    public DrawInfo(final int mouseX, final int mouseY, final float tick) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.tick = tick;
    }

    public void applyColor() {
        color();
    }

    public void push() {
        GlStateManager.pushMatrix();
    }

    public void pop() {
        GlStateManager.popMatrix();
    }

    public void translate(final double x, final double y, final double z) {
        GlStateManager.translated(x, y, z);
    }

    public void scale(final double x, final double y, final double z) {
        GlStateManager.scaled(x, y, z);
    }

    public void rotate(final Quaternion quaternion) {
        GlStateManager.rotated(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r());
    }

    public void applyTexture(final ResourceLocation location) {
        GlStateManager.enableTexture();
        Minecraft.getInstance().getTextureManager().bind(location);

    }

    public void disableTexture() {
        GlStateManager.disableTexture();
    }

    public void color() {
        this.color(1, 1, 1, 1);
    }

    public void color(final int color) {
        this.color(UIColor.red(color) / 255, UIColor.green(color) / 255, UIColor.blue(color) / 255,
                UIColor.alpha(color) / 255);
    }

    @SuppressWarnings("deprecation")
    public void color(final double r, final double g, final double b, final double a) {
        GlStateManager.color4f((float) r, (float) g, (float) b, (float) a);
    }

    public void blendOn() {
        GlStateManager.enableBlend();
    }

    public void blendOff() {
        GlStateManager.disableBlend();
    }

    public void depthOn() {
        GlStateManager.enableDepthTest();
    }

    public void depthOff() {
        GlStateManager.disableDepthTest();
    }

    public void scissorOn(final int x, final int y, final int width, final int height) {
        GlStateManager.enableScissor(x, y, width, height);
    }

    public void scissorOff() {
        GlStateManager.disableScissor();
    }

    public void alphaOn() {
        GlStateManager.enableAlphaTest();
    }

    public void alphaOff() {
        GlStateManager.disableAlphaTest();
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
        final BufferWrapper bufferbuilder = this.builder(GL11.GL_TRIANGLES,
                DefaultVertexFormats.POSITION);
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