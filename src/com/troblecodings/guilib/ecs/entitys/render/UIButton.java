package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIButton extends UIComponent {

    public static final int DEFAULT_COLOR = 14737632;
    public static final int DEFAULT_DISABLED_COLOR = 10526880;
    public static final int DEFAULT_HOVER_COLOR = 16777120;

    private static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted"));

    private String text;
    private boolean enabled;
    private final Font fontrenderer;

    public UIButton(final String text) {
        final Minecraft mc = Minecraft.getInstance();
        this.setVisible(true);
        this.setEnabled(true);
        this.text = text;
        this.fontrenderer = mc.font;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (this.visible) {
            final GuiGraphics graphics = info.graphics();
            info.color();
            info.blendOn();
            graphics.blitSprite(SPRITES.get(enabled, parent.isHovered()), 0, 0,
                    (int) parent.getWidth(), (int) parent.getHeight());
            final int colorUsed = enabled
                    ? (parent.isHovered() ? DEFAULT_HOVER_COLOR : DEFAULT_COLOR)
                    : DEFAULT_DISABLED_COLOR;
            graphics.drawCenteredString(fontrenderer, this.text, (int) parent.getWidth() / 2,
                    ((int) parent.getHeight() - 8) / 2, colorUsed);
            graphics.flush();
            info.depthOff();
        }
    }

    @Override
    public void onAdd(final UIEntity entity) {
        super.onAdd(entity);
        update();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}