package com.troblecodings.guilib.ecs.entitys.render;

import java.util.Arrays;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiUtils;

@OnlyIn(Dist.CLIENT)
public class UIToolTip extends UIComponent {

    private String descripton;

    public UIToolTip(final String descripton) {
        this.descripton = descripton;
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
            @SuppressWarnings("resource")
            final FontRenderer font = Minecraft.getInstance().font;
            final String desc = (Screen.hasShiftDown() || this.descripton.length() < 200)
                    ? this.descripton
                    : I18n.get("gui.keyprompt");
            final UpdateEvent base = parent.getLastUpdateEvent();
            if (base != null) {
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
