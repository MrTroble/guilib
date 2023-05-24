package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.DrawUtil;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIButton extends UIComponent {

    public static final int DEFAULT_COLOR = 14737632;
    public static final int DEFAULT_DISABLED_COLOR = 10526880;
    public static final int DEFAULT_HOVER_COLOR = 16777120;

    public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(
            "textures/gui/widgets.png");

    private String text;
    private boolean enabled;
    private final FontRenderer fontrenderer;

    public UIButton(final String text) {
        final Minecraft mc = Minecraft.getMinecraft();
        this.setVisible(true);
        this.setEnabled(true);
        this.text = text;
        this.fontrenderer = mc.fontRenderer;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (this.visible) {
            info.applyTexture(BUTTON_TEXTURES);
            info.color();
            info.depthOff();
            final int offsetV = enabled ? (parent.isHovered() ? 2 : 1) : 0;
            GuiUtils.drawTexturedModalRect(0, 0, 0, 46 + offsetV * 20, (int) parent.getWidth() / 2,
                    (int) parent.getHeight(), 0);
            GuiUtils.drawTexturedModalRect((int) parent.getWidth() / 2, 0,
                    200 - (int) parent.getWidth() / 2, 46 + offsetV * 20,
                    (int) parent.getWidth() / 2, (int) parent.getHeight(), 0);
            final int colorUsed = enabled
                    ? (parent.isHovered() ? DEFAULT_HOVER_COLOR : DEFAULT_COLOR)
                    : DEFAULT_DISABLED_COLOR;
            DrawUtil.drawCenteredString(info, fontrenderer, this.text, (int) parent.getWidth() / 2,
                    ((int) parent.getHeight() - 8) / 2, colorUsed);
        }
    }

    @Override
    public void update() {
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