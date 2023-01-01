package com.troblecodings.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import com.troblecodings.core.NBTWrapper;
import com.troblecodings.guilib.ecs.DrawUtil;
import com.troblecodings.guilib.ecs.entitys.render.UIButton;
import com.troblecodings.guilib.ecs.interfaces.UIAutoSync;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.GuiUtils;

@OnlyIn(Dist.CLIENT)
public class UICheckBox extends UIComponent implements UIAutoSync {

    public static final int BOX_WIDTH = 11;

    private String id;
    private String text;
    private IntConsumer onChange;
    private boolean enabled;
    private boolean checked;

    public UICheckBox(final String id) {
        this.id = id;
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
        GuiUtils.drawContinuousTexturedBox(info.stack, UIButton.BUTTON_TEXTURES, 0, 0, 0, 46, BOX_WIDTH,
                BOX_WIDTH, 200, 20, 2, 3, 2, 2, 0);
        final int color = this.enabled ? UIButton.DEFAULT_COLOR : UIButton.DEFAULT_DISABLED_COLOR;
        if (this.isChecked())
            DrawUtil.drawCenteredString(info, mc.font, "x", BOX_WIDTH / 2 + 1, 1, 14737632);
        mc.font.drawShadow(info.stack, text, BOX_WIDTH + 2.0f, 2.0f, color);
    }

    @Override
    public void write(final NBTWrapper compound) {
        compound.putBoolean(id, isChecked());
    }

    @Override
    public void read(final NBTWrapper compound) {
        this.setChecked(compound.getBoolean(id));
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

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(final String id) {
        this.id = id;
    }

}
