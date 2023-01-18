package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
            final String desc = (Screen.hasShiftDown() || this.descripton.length() < 200) ? this.descripton
                    : I18n.get("gui.keyprompt");
            final UpdateEvent base = parent.getLastUpdateEvent();
            if (base != null) {
                base.base.renderTooltip(info.stack, new TextComponent(desc), info.mouseX,
                        info.mouseY);
            }
        }
    }

    @Override
    public void keyEvent(KeyEvent event) {
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
    }
}
