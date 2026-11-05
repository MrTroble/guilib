package com.troblecodings.guilib.ecs.entitys;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import org.joml.Quaternionf;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.data.ModelData;

public class DrawInfo {
    public final int mouseX;
    public final int mouseY;
    public final PoseStack stack;
    public final float tick;
    private BufferBuilder activeBuilder;

    public DrawInfo(final int mouseX, final int mouseY, final PoseStack stack, final float tick) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.stack = stack;
        this.tick = tick;
    }

    public GuiGraphics graphics() {
        final Minecraft mc = Minecraft.getInstance();
        final GuiGraphics graphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
        graphics.pose().last().pose().set(this.stack.last().pose());
        graphics.pose().last().normal().set(this.stack.last().normal());
        return graphics;
    }

    public void drawString(final Font font, final String text, final float x, final float y,
            final int color, final boolean shadow) {
        final GuiGraphics graphics = graphics();
        graphics.pose().translate(x, y, 0);
        graphics.drawString(font, text, 0, 0, color, shadow);
        graphics.flush();
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

    public void rotate(final Quaternionf quaternion) {
        this.stack.mulPose(quaternion);
    }
    
    public void rotate(final float x, final float y, final float z) {
        rotate(new Quaternionf().rotationXYZ(x, y, z));
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
        this.translate(info.vector.getX(), info.vector.getY(), info.vector.getZ());
        info.consumer.accept(this);
        final RandomSource random = RandomSource.create();
        final ModelData data = info.wrapper.toModelData();
        final Direction[] directions = Direction.values();
        for (int side = 0; side <= directions.length; side++) {
            random.setSeed(42L);
            final Direction direction = side == directions.length ? null : directions[side];
            for (final BakedQuad quad : info.model.getQuads(info.state, direction, random, data, null)) {
                final int tint = quad.isTinted()
                        ? mc.getBlockColors().getColor(info.state, null, null, quad.getTintIndex()) : -1;
                consumer.putBulkData(this.stack.last(), quad, (tint >> 16 & 255) / 255.0f,
                        (tint >> 8 & 255) / 255.0f, (tint & 255) / 255.0f, 1.0f,
                        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }
        }
        stack.popPose();
        bufferSource.endBatch();
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
        if (activeBuilder != null)
            throw new IllegalStateException("Finish the current GUI buffer before starting another");
        activeBuilder = Tesselator.getInstance().begin(mode, format);
        return new BufferWrapper(activeBuilder, this.stack.last().pose());
    }

    public void end() {
        if (activeBuilder == null)
            throw new IllegalStateException("No GUI buffer has been started");
        final BufferBuilder builder = activeBuilder;
        activeBuilder = null;
        final MeshData mesh = builder.build();
        if (mesh != null)
            BufferUploader.drawWithShader(mesh);
    }
}