package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class UISprite extends UIComponent {

    private final ResourceLocation sprite;

    public UISprite(final ResourceLocation sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.color();
        final GuiGraphics graphics = info.graphics();
        graphics.blitSprite(sprite, 0, 0, (int) parent.getWidth(), (int) parent.getHeight());
        graphics.flush();
    }

}
