package com.troblecodings.guilib.ecs.entitys.render;

import java.util.Arrays;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

public class UILines extends UIComponent {

    private final float[] lines;
    private final float[] lineCache;
    private final float width;
    private int color;
    private double oldWidth = -1;
    private double oldHeight = -1;

    public UILines(final float[] lines, final float width) {
        super();
        this.lines = Arrays.copyOf(lines, lines.length);
        this.lineCache = new float[lines.length];
        this.width = width;
        this.color = 0xFF000000;
    }

    @Override
    public void draw(final DrawInfo info) {
        info.depthOn();
        info.disableTexture();
        info.alphaOff();
        info.lines(this.color, this.width, this.lineCache);
        info.depthOff();
    }

    @Override
    public void update() {
        if (!hasParent())
            return;
        if (oldWidth != parent.getWidth() || oldHeight != parent.getHeight()) {
            oldWidth = parent.getWidth();
            oldHeight = parent.getHeight();
            for (int i = 0; i < lines.length; i += 2) {
                this.lineCache[i] = this.lines[i] * (float) oldWidth;
                this.lineCache[i + 1] = this.lines[i + 1] * (float) oldHeight;
            }
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }
}
