package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIBorder extends UIComponent {

    private int color;
    private float lineWidth;

    public UIBorder(final int color) {
        this(color, 1);
    }

    public UIBorder(final int color, final float lineWidth) {
        this.color = color;
        if (lineWidth <= 0)
            throw new IllegalArgumentException(
                    String.format("Line width (%f) can not be less or equal to 0", lineWidth));
        this.lineWidth = lineWidth;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.blendOn();
        info.color(this.color);
        info.lines(lineWidth, new double[] {
                0, parent.getHeight(), //
                parent.getWidth(), parent.getHeight(), //
                0, 0, //
                parent.getWidth(), 0, //
        });
        info.lines(lineWidth, new double[] {
                parent.getWidth(), 0, //
                parent.getWidth(), parent.getHeight(), //
                0, 0, //
                0, parent.getHeight(), //
        });
        info.blendOff();
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

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(final float lineWidth) {
        this.lineWidth = lineWidth;
    }

}
