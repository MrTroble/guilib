package com.troblecodings.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIBorder extends UIComponent {

    private int color;
    private float lineWidth;

    public UIBorder(final int color) {
        this(color, 1);
    }

    public UIBorder(final int color, final float lineWidth) {
        this.color = color;
        this.lineWidth = lineWidth;
    }

    @Override
    public void draw(final DrawInfo info) {
        final float f3 = (color >> 24 & 255) / 255.0F;
        final float f = (color >> 16 & 255) / 255.0F;
        final float f1 = (color >> 8 & 255) / 255.0F;
        final float f2 = (color & 255) / 255.0F;
        final Tesselator tessellator = Tesselator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuilder();
        RenderSystem.enableBlend();
        GL11.glLineWidth(this.lineWidth);
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(f, f1, f2, f3);
        bufferbuilder.begin(Mode.LINE_STRIP, DefaultVertexFormat.POSITION);
        bufferbuilder.vertex(0, parent.getHeight(), 0.0D).endVertex();
        bufferbuilder.vertex(parent.getWidth(), parent.getHeight(), 0.0D).endVertex();
        bufferbuilder.vertex(parent.getWidth(), 0, 0.0D).endVertex();
        bufferbuilder.vertex(0, 0, 0.0D).endVertex();
        tessellator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public void update() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(final float lineWidth) {
        this.lineWidth = lineWidth;
    }

}
