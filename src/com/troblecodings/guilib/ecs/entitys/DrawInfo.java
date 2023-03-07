package com.troblecodings.guilib.ecs.entitys;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Quaternion;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class DrawInfo {
    public final int mouseX;
    public final int mouseY;
    public final PoseStack stack;
    public final float tick;

    public DrawInfo(final int mouseX, final int mouseY, final PoseStack stack, final float tick) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.stack = stack;
        this.tick = tick;
    }

    public void applyColor() {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
    }

    public void color() {
        this.color(1, 1, 1, 1);
    }

    public void color(final int color) {
        this.color(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color),
                FastColor.ARGB32.blue(color), FastColor.ARGB32.alpha(color));
    }

    public void color(final double r, final double g, final double b, final double a) {
        RenderSystem.setShaderColor((float) r, (float) g, (float) b, (float) a);
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

    private double[] normalVector(final double[] vectors, final int offset, final double multiple) {
        final double xR = vectors[offset] - vectors[offset + 2];
        final double yR = vectors[offset + 1] - vectors[offset + 3];
        final double value = Math.hypot(xR, yR);
        return new double[] {
                -(yR / value) * multiple, (xR / value) * multiple
        };
    }

    public void lines(final float width, final double[] lines) {
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(width);
        final BufferWrapper bufferbuilder = this.builder(Mode.LINES,
                DefaultVertexFormat.POSITION_COLOR_NORMAL);
        for (int i = 0; i < lines.length; i += 4) {
            final double xR = lines[i] - lines[i + 2];
            final double yR = lines[i + 1] - lines[i + 3];
            final double hypot = Math.hypot(xR, yR);
            final float normalX = Math.abs((float) (xR / hypot));
            final float normalY = Math.abs((float) (yR / hypot));
            bufferbuilder.pos(lines[i], lines[i + 1], 0.0D).color(0, 0, 0, 255)
                    .normal(normalX, normalY, 0.0F).end();
            bufferbuilder.pos(lines[i + 2], lines[i + 3], 0.0D).color(0, 0, 0, 255)
                    .normal(normalX, normalY, 0.0F).end();
        }
        this.end();
    }

    public BufferWrapper builder(final VertexFormat.Mode mode, final VertexFormat format) {
        final BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(mode, format);
        return new BufferWrapper(builder, this.stack.last().pose());
    }

    public void end() {
        Tesselator.getInstance().end();
    }
}