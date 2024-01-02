package com.troblecodings.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import com.troblecodings.guilib.ecs.DrawUtil;
import com.troblecodings.guilib.ecs.entitys.render.UIButton;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ScreenUtils;

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
        
        ScreenUtils.blitWithBorder(info.stack, UIButton.BUTTON_TEXTURES, 0, 0, 0, 46,
                BOX_WIDTH, BOX_WIDTH, 200, 20, 2, 3, 2, 2, 0);
        final int color = this.enabled ? UIButton.DEFAULT_COLOR : UIButton.DEFAULT_DISABLED_COLOR;
        if (this.isChecked())
            DrawUtil.drawCenteredString(info, mc.font, "x", BOX_WIDTH / 2 + 1, 1, 14737632);
        mc.font.drawShadow(info.stack, text, BOX_WIDTH + 2.0f, 2.0f, color);
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
