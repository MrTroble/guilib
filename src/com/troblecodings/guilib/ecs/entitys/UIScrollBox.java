package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.entitys.render.UIScissor;
import com.troblecodings.guilib.ecs.interfaces.IBoxMode;

public class UIScrollBox extends UIScissor {

    private final IBoxMode mode;
    private final int spacer;

    public UIScrollBox(IBoxMode mode, int spacer) {
        super();
        this.mode = mode;
        this.spacer = spacer;
    }

    @Override
    public void update() {
        super.update();
        int bounds = 0;
        for (final UIEntity child : this.parent.children) {
            mode.setPos(child, bounds);
            bounds += mode.getBounds(child) + spacer;
            mode.post(child);
        }
    }

}
