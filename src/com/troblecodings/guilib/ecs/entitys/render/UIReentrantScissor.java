package com.troblecodings.guilib.ecs.entitys.render;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIReentrantScissor extends UIComponent {

    private int x;
    private int y;
    private int width;
    private int height;
    
    private int[] last = null;

    @Override
    public void draw(final DrawInfo info) {
    	last = info.getScissorState();
    	if(last == null) {
        	info.scissorOn(x, y, width, height);
    	} else {
    		int x1 = Math.max(x, last[0]);
    		int y1 = Math.max(y, last[1]);
    		int w1 = Math.max(0, Math.min(width, last[2]-x1+last[0]));
    		int h1 = Math.max(0, Math.min(height, last[3]-y1+last[1]));
        	info.scissorOn(x1, y1, w1, h1);
    	}
    }

    @Override
    public void exitDraw(final DrawInfo info) {
    	if(last != null) info.scissorOn(last[0], last[1], last[2], last[3]);
    	else info.scissorOff();
    }

    @Override
    public void updateEvent(final UpdateEvent lastUpdateEvent) {
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
