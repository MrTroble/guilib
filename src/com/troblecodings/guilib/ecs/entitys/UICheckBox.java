package com.troblecodings.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import com.troblecodings.guilib.ecs.entitys.render.UIButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UICheckBox extends UIComponent {

    public static final int BOX_WIDTH = 11;

    private String text;
    private IntConsumer onChange;
    private boolean enabled;
    private boolean checked;

    public UICheckBox(final String id) {
        this.setVisible(true);
        this.setEnabled(true);
    }

    @Override
    public void update() {
        final Minecraft mc = Minecraft.getInstance();
        this.parent.setWidth(BOX_WIDTH + 4 + mc.font.width(text));
        this.parent.setHeight(Math.max(BOX_WIDTH, mc.font.lineHeight) + 2);
    }

    @Override
    public void draw(final DrawInfo info) {
        final Minecraft mc = Minecraft.getInstance();
        final GuiGraphics graphics = info.graphics();
        final boolean hovered = enabled && parent.isHovered();
        final String sprite = checked
                ? (hovered ? "widget/checkbox_selected_highlighted" : "widget/checkbox_selected")
                : (hovered ? "widget/checkbox_highlighted" : "widget/checkbox");
        info.color();
        info.blendOn();
        graphics.blitSprite(ResourceLocation.withDefaultNamespace(sprite), 0, 0,
                BOX_WIDTH, BOX_WIDTH);
        final int color = this.enabled ? UIButton.DEFAULT_COLOR : UIButton.DEFAULT_DISABLED_COLOR;
        graphics.drawString(mc.font, text, BOX_WIDTH + 2, 2, color);
        graphics.flush();
        info.depthOff();
    }

    public IntConsumer getOnChange() {
        return onChange;
    }

    public void setOnChange(final IntConsumer onChange) {
        this.onChange = onChange;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(final boolean checked) {
        this.checked = checked;
    }

}