package com.troblecodings.guilib.ecs.entitys;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Quaternion;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

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

    public void drawTexture(final ResourceLocation location, 
    		double w, double h, double u, double v, double mu, double mv) {
        depthOn();
        blendOn();
        applyTexture(location);

        final BufferWrapper bufferbuilder = builder(Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.pos(0, h, 0).tex((float) u, (float) mv).end();
        bufferbuilder.pos(w, h, 0).tex((float) mu, (float) mv).end();
        bufferbuilder.pos(w, 0, 0).tex((float) mu, (float) v).end();
        bufferbuilder.pos(0, 0, 0).tex((float) u, (float) v).end();
        end();
        blendOff();
        depthOff();
        disableTexture();
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

    public void applyState(final UIBlockRenderInfo info) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.cutout());
        stack.pushPose();
        final ModelBlockRenderer render = mc.getBlockRenderer().getModelRenderer();
        this.translate(info.vector.getX(), info.vector.getY(), info.vector.getZ());
        info.consumer.accept(this);
        render.renderModel(this.stack.last(), consumer, info.state, info.model, 1.0f, 1.0f, 1.0f,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, info.wrapper);
        stack.popPose();
        bufferSource.endBatch();
    }

    public void disableTexture() {
        RenderSystem.disableTexture();
    }

    public void color() {
        this.color(1, 1, 1, 1);
    }

    public void color(final int color) {
        this.color((float) UIColor.red(color) / 255, (float) UIColor.green(color) / 255,
                (float) UIColor.blue(color) / 255, (float) UIColor.alpha(color) / 255);
    }

    public void color(final float r, final float g, final float b, final float a) {
        RenderSystem.setShaderColor(r, g, b, a);
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
    
    public boolean isScissorEnabled() {
    	return GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
    }

    public int[] getScissorState() {
    	if(!isScissorEnabled()) return null;
    	int[] values = new int[4];
    	GL20.glGetIntegerv(GL20.GL_SCISSOR_BOX, values);
    	return values;
    }
    
    public void scissorOn(final int x, final int y, final int width, final int height) {
        RenderSystem.enableScissor(x, y, width, height);
    }

    public void scissorOn() {
	    GlStateManager._enableScissorTest();
    }

    public void scissorOff() {
        RenderSystem.disableScissor();
    }

    public void alphaOn() {
        // Not longer being used
    }

    public void alphaOff() {
        // Not longer being used
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
        RenderSystem.setShader(GameRenderer::getPositionShader);
        this.color(color);
        final BufferWrapper bufferbuilder =
                this.builder(Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        for (int i = 0; i < lines.length; i += 4) {
            singleLine(color, bufferbuilder, lines[i], lines[i + 2], lines[i + 1], lines[i + 3],
                    width);
        }
        this.end();
        this.color();
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