package com.troblecodings.guilib.ecs.entitys.render;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIToolTip extends UIComponent {

    private final boolean showDescDirectly;
    private String descripton;

    public UIToolTip(final String descripton) {
        this(descripton, false);
    }

    public UIToolTip(final String descripton, final boolean showDescDirectly) {
        this.descripton = descripton;
        this.showDescDirectly = showDescDirectly;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(final String descripton) {
        this.descripton = descripton;
    }

    @Override
    public void postDraw(final DrawInfo info) {
        if (this.parent.isHovered()) {
            final String desc;
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                return;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || showDescDirectly) {
                desc = this.descripton;
            } else {
                desc = I18n.format("gui.keyprompt");
            }
            final UpdateEvent base = parent.getLastUpdateEvent();
            if (base != null) {
                final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
                GuiUtils.drawHoveringText(Arrays.asList(desc.split(System.lineSeparator())),
                        info.mouseX, info.mouseY, base.width, base.height, -1, font);
            }
        }
    }

    @Override
    public void keyEvent(final KeyEvent event) {
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
    }
}
