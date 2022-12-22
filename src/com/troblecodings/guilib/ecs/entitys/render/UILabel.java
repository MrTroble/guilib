package com.troblecodings.guilib.ecs.entitys.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UILabel extends UIComponent {

    public static final int DEFAULT_STRING_COLOR = 4210752;
    private String string;
    private int stringColor;
    private final Font renderer;
    private int restHeight = 0;
    private int restWidth = 0;
    private boolean centerX = true;
    private boolean centerY = true;

	public UILabel(final String text) {
        this.string = text;
        this.stringColor = DEFAULT_STRING_COLOR;
        Minecraft mc = Minecraft.getInstance();
        this.renderer = mc.font;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (this.visible) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            renderer.draw(info.stack, string, restWidth, restHeight, stringColor);
            RenderSystem.setShaderColor(1, 1, 1, 1);
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
        return renderer.width(string);
    }

    public int getTextHeight() {
        return renderer.lineHeight;
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