package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UILabel extends UIComponent {

    public static final int DEFAULT_STRING_COLOR = 4210752;
    private String string;
    private int stringColor;
    private final FontRenderer renderer;
    private int restHeight = 0;
    private int restWidth = 0;
    private boolean centerX = true;
    private boolean centerY = true;

    public UILabel(final String text) {
        this.string = text;
        this.stringColor = DEFAULT_STRING_COLOR;
        this.renderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
        if (this.visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            renderer.drawString(string, restWidth, restHeight, stringColor);
            GlStateManager.color(1, 1, 1, 1);
        }
    }

    @Override
    public void update() {
        if (this.centerY)
            this.restHeight = (this.parent.getHeight() - this.getTextHeight()) / 2;
        if (this.centerX)
            this.restWidth = (this.parent.getWidth() - this.getTextWidth()) / 2;
    }

    public String getText() {
        return string;
    }

    public void setText(final String string) {
        this.string = string;
    }

    public int getTextColor() {
        return stringColor;
    }

    public void setTextColor(final int stringColor) {
        this.stringColor = stringColor;
    }

    public int getTextX() {
        return restHeight;
    }

    public int getTextY() {
        return restWidth;
    }

    public int getTextWidth() {
        return renderer.getStringWidth(string);
    }

    public int getTextHeight() {
        return renderer.FONT_HEIGHT;
    }

    public boolean isCenterX() {
        return centerX;
    }

    public void setCenterX(final boolean centerX) {
        this.centerX = centerX;
    }

    public boolean isCenterY() {
        return centerY;
    }

    public void setCenterY(final boolean centerY) {
        this.centerY = centerY;
    }
}
