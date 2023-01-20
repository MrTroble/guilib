package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIScissor extends UIComponent {

    private int x;
    private int y;
    private int width;
    private int height;

    @Override
    public void draw(final DrawInfo info) {
        info.scissorOn(x, y, width, height);
    }

    @Override
    public void exitDraw(final DrawInfo info) {
        info.scissorOff();
    }

    @Override
    public void updateEvent(UpdateEvent lastUpdateEvent) {
        this.height = (int) (parent.getHeight() * lastUpdateEvent.scaleFactor);
        this.width = (int) (parent.getWidth() * lastUpdateEvent.scaleFactor);
        if (this.height < 0)
            this.height = 0;
        if (this.width < 0)
            this.width = 0;
        this.x = (int) (parent.getLevelX() * lastUpdateEvent.scaleFactor
                / lastUpdateEvent.guiScale);
        this.y = (int) ((lastUpdateEvent.height - (int) parent.getLevelY()
                - (int) parent.getHeight()) * lastUpdateEvent.scaleFactor);
    }

    @Override
    public void update() {
        final UpdateEvent lastUpdateEvent = parent.getLastUpdateEvent();
        if (lastUpdateEvent == null)
            return;
        updateEvent(lastUpdateEvent);
    }

}
