package com.troblecodings.guilib.ecs.entitys.render;

import java.util.Arrays;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

public class UILines extends UIComponent {

    private final float[] lines;
    private final float[] lineCache;
    private final float width;
    private int color;

    public UILines(final float[] lines, final float width) {
        super();
        this.lines = Arrays.copyOf(lines, lines.length);
        this.lineCache = new float[lines.length];
        this.width = width;
        this.color = 0xFFFFFFFF;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.depthOn();
        info.lines(this.color, this.width, this.lineCache);
        info.depthOff();
    }

    @Override
    public void update() {
        for (int i = 0; i < lines.length; i += 2) {
            this.lineCache[i] = this.lines[i] * (float) parent.getWidth();
            this.lineCache[i + 1] = this.lines[i + 1] * (float) parent.getHeight();
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }
}
