package com.troblecodings.guilib.ecs.entitys.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIBorder extends UILines {

    private static final float[] BORDER_COORDINATES = new float[] {
            0, 1, 1, 1, //
            0, 0, 1, 0, //
            1, 1, 1, 0, //
            0, 1, 0, 0
    };

    public UIBorder(final int color) {
        this(color, 1);
    }

    public UIBorder(final int color, final float lineWidth) {
        super(BORDER_COORDINATES, lineWidth);
        setColor(color);
    }

}
