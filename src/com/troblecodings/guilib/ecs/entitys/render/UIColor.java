package com.troblecodings.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.entitys.BufferWrapper;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIColor extends UIComponent {

    private int color;
    private final int insets;

    public UIColor(final int color) {
        this(color, 0);
    }

    public UIColor(final int color, final int insets) {
        this.color = color;
        this.insets = insets;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (this.visible) {
            info.applyColor();
            info.depthOff();
            info.blendOn();
            final BufferWrapper wrapper = info.builder(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            wrapper.quad(-insets, (int) parent.getWidth() + insets, -insets,
                    (int) parent.getHeight() + insets, this.color);
            info.end();
            info.blendOff();
        }
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
    
    public static int alpha(final int color) {
        return color >>> 24;
    }
    
    public static int red(final int color) {
        return color >> 16 & 255;
    }
    
    public static int green(final int color) {
        return color >> 8 & 255;
    }
    
    public static int blue(final int color) {
        return color & 255;
    }

}
