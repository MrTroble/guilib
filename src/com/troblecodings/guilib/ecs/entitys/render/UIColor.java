package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.GuiUtils;

@OnlyIn(Dist.CLIENT)
public class UIColor extends UIComponent {

    private int color;

    public UIColor(final int color) {
        this.setColor(color);
    }

    @Override
    public void draw(final DrawInfo info) {
        GuiUtils.drawGradientRect(info.stack.last().pose(), 0, 0, 0, parent.getWidth(), parent.getHeight(), this.color,
                this.color);
    }

    @Override
    public void update() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }

}
