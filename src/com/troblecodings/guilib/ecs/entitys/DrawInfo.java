package com.troblecodings.guilib.ecs.entitys;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
        RenderSystem.enableTexture();
        Minecraft.getInstance().getTextureManager().bind(location);
    }

    @SuppressWarnings("deprecation")
    public void applyState(final IBakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper) {
        this.depthOn();
        this.blendOn();
        this.alphaOn();
        this.applyTexture(AtlasTexture.LOCATION_BLOCKS);
        final Minecraft mc = Minecraft.getInstance();
        final BlockModelRenderer render = mc.getBlockRenderer().getModelRenderer();
        final BufferWrapper builder = this.builder(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        render.renderModel(this.stack.last(), builder.builder, state, model, 1.0f, 1.0f, 1.0f,
                OverlayTexture.NO_OVERLAY, OverlayTexture.NO_OVERLAY, wrapper);
        this.end();
        this.alphaOff();
        this.blendOff();
        this.depthOff();
    }

    public void disableTexture() {
        RenderSystem.disableTexture();
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

    @SuppressWarnings("deprecation")
    public void alphaOn() {
        RenderSystem.enableAlphaTest();
    }

    @SuppressWarnings("deprecation")
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