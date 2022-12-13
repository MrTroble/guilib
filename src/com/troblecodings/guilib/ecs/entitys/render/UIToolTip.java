package com.troblecodings.guilib.ecs.entitys.render;

import com.mojang.blaze3d.platform.InputConstants;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIToolTip extends UIComponent {

    private String descripton;
    private boolean tooltip = false;

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
        	Minecraft mc = Minecraft.getInstance();
            final Font font = mc.font;
            final String desc = (tooltip
                    || this.descripton.length() < 200) ? this.descripton
                            : I18n.get("gui.keyprompt");
            final UpdateEvent base = parent.getLastUpdateEvent();
            if (base != null) {
                font.drawWordWrap(new TextComponent(desc), info.mouseX,
                		info.mouseY, base.width, base.height);
            }
        }
    }
    
    @Override
    public void keyEvent(KeyEvent event) {
    	this.tooltip = event.keyCode == InputConstants.KEY_LCONTROL;
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
    }
}
