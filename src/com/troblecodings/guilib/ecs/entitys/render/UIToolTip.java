package com.troblecodings.guilib.ecs.entitys.render;

import java.util.ArrayList;
import java.util.List;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

    public boolean showDescDirectly() {
        return showDescDirectly;
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
            if (Screen.hasControlDown()) {
                return;
            } else if (Screen.hasShiftDown() || showDescDirectly) {
                desc = this.descripton;
            } else {
                desc = I18n.get("gui.keyprompt");
            }
            final UpdateEvent base = parent.getLastUpdateEvent();
            if (base != null) {
                final List<ITextComponent> listComp = new ArrayList<>();
                listComp.add(new StringTextComponent(desc));
                base.base.renderComponentTooltip(info.stack, listComp, info.mouseX, info.mouseY);
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
