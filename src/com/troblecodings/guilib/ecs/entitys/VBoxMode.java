package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.interfaces.IBoxMode;

public class VBoxMode implements IBoxMode {

    @Override
    public int getBounds(final UIEntity entity) {
        return (int)entity.getHeight();
    }

    @Override
    public void setBounds(final UIEntity entity, final int bounds) {
        entity.setHeight(bounds);
    }

    @Override
    public void setPos(final UIEntity entity, final int pos) {
        entity.setY(pos);
    }

    @Override
    public boolean inheritsBounds(final UIEntity entity) {
        return entity.inheritHeight();
    }

    @Override
    public void post(final UIEntity entity) {
        if (entity.inheritWidth())
            entity.setWidth(entity.getParent().getWidth());
    }

    @Override
    public int getMin(final UIEntity entity) {
        return (int)entity.getMinHeight();
    }
}