package com.troblecodings.guilib.ecs.entitys;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Quaternion;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class DrawInfo {
    public final int mouseX;
    public final int mouseY;
    public final PoseStack stack;
    public final float tick;

    public DrawInfo(int mouseX, int mouseY, PoseStack stack, float tick) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.stack = stack;
        this.tick = tick;
    }
    
    public void applyColor() {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
    }
    
    public void push() {
        this.stack.pushPose();
    }
    
    public void pop() {
        this.stack.popPose();
    }

    public void translate(double x, double y, double z) {
        this.stack.translate(x, y, z);
    }

    public void scale(double x, double y, double z) {
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

    public void color(double r, double g, double b, double a) {
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

    public void scissorOn(int x, int y, int width, int height) {
        RenderSystem.enableScissor(x, y, width, height);
    }
    
    public void scissorOff() {
        RenderSystem.disableScissor();
    }
    
    public BufferWrapper builder(VertexFormat.Mode mode, VertexFormat format) {
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(mode, format);
        return new BufferWrapper(builder, this.stack.last().pose());
    }

    public void end() {
        Tesselator.getInstance().end();
    }
}