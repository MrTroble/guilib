package com.troblecodings.guilib.ecs.entitys.render;

import java.util.Arrays;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

public class UILines extends UIComponent {

    private final double[] lines;
    private final double[] lineCache;
    private final float width;
    private int color;

    public UILines(final double[] lines, final float width) {
        super();
        this.lines = Arrays.copyOf(lines, lines.length);
        this.lineCache = new double[lines.length];
        this.width = width;
        this.color = 0xFFFFFFFF;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.color(this.color);
        info.lines(this.width, this.lineCache);
    }

    @Override
    public void update() {
        for (int i = 0; i < lines.length; i += 2) {
            this.lineCache[i] = this.lines[i] * parent.getWidth();
            this.lineCache[i + 1] = this.lines[i + 1] * parent.getHeight();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }
}
