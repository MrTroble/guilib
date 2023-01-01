package com.troblecodings.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

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
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
    }

    @Override
    public void exitDraw(final DrawInfo info) {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void update() {
        final UpdateEvent lastUpdateEvent = parent.getLastUpdateEvent();
        this.height = (int)parent.getHeight() * lastUpdateEvent.scaleFactor;
        this.width = (int)parent.getWidth() * lastUpdateEvent.scaleFactor;
        if (this.height < 0)
            this.height = 0;
        if (this.width < 0)
            this.width = 0;
        this.x = (int)parent.getLevelX() * lastUpdateEvent.scaleFactor / lastUpdateEvent.guiScale;
        this.y = (lastUpdateEvent.height - (int)parent.getLevelY() - (int)parent.getHeight())
                * lastUpdateEvent.scaleFactor;
    }

}
