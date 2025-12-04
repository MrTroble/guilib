package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.entitys.render.UIBorder;

public class UIBorderScissoringDisabled extends UIBorder {

    public UIBorderScissoringDisabled(final int color) {
        super(color, 1);
    }

    public UIBorderScissoringDisabled(int color, float lineWidth) {
        super(color, lineWidth);
    }

    @Override
    public void draw(DrawInfo info) {
        boolean isEnabled = info.isScissorEnabled();
        info.scissorOff();
        super.draw(info);
        if (isEnabled)
            info.scissorOn();
    }

}