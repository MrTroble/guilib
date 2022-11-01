package com.troblecodings.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
    public void draw(final int mouseX, final int mouseY) {
        final float f3 = (color >> 24 & 255) / 255.0F;
        final float f = (color >> 16 & 255) / 255.0F;
        final float f1 = (color >> 8 & 255) / 255.0F;
        final float f2 = (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GL11.glLineWidth(this.lineWidth);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(0, parent.getHeight(), 0.0D).endVertex();
        bufferbuilder.pos(parent.getWidth(), parent.getHeight(), 0.0D).endVertex();
        bufferbuilder.pos(parent.getWidth(), 0, 0.0D).endVertex();
        bufferbuilder.pos(0, 0, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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
