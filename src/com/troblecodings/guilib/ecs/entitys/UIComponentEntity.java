package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

public class UIComponentEntity extends UIComponent {

    protected final UIEntity entity;

    public UIComponentEntity(final UIEntity entity) {
        super();
        this.entity = entity;
    }

    @Override
    public void draw(final DrawInfo info) {
        this.entity.draw(info);
    }

    @Override
    public void exitDraw(final DrawInfo info) {
        this.entity.exitDraw(info);
    }

    @Override
    public void update() {
        this.entity.update();
        this.entity.setWidth(this.parent.getWidth());
        this.entity.setHeight(this.parent.getHeight());
    }

    @Override
    public void onClosed() {
        this.entity.onClosed();
    }

    @Override
    public void postDraw(final DrawInfo info) {
        this.entity.postDraw(info);
    }

    @Override
    public void keyEvent(final KeyEvent event) {
        this.entity.keyEvent(event);
    }

    @Override
    public void mouseEvent(final MouseEvent event) {
        this.entity.mouseEvent(event);
    }

    @Override
    public void updateEvent(final UpdateEvent event) {
        this.entity.updateEvent(event);
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        this.parent.setVisible(visible);
    }

}
