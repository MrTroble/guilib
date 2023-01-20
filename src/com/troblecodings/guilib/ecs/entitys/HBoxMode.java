package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.interfaces.IBoxMode;

public class HBoxMode implements IBoxMode {

    @Override
    public int getBounds(final UIEntity entity) {
        return (int)entity.getWidth();
    }

    @Override
    public void setBounds(final UIEntity entity, final int bounds) {
        entity.setWidth(bounds);
    }

    @Override
    public void setPos(final UIEntity entity, final int pos) {
        entity.setX(pos);
    }

    @Override
    public boolean inheritsBounds(final UIEntity entity) {
        return entity.inheritWidth();
    }

    @Override
    public void post(final UIEntity entity) {
        if (entity.inheritHeight())
            entity.setHeight(entity.getParent().getHeight());
    }

    @Override
    public int getMin(final UIEntity entity) {
        return (int)entity.getMinWidth();
    }

    @Override
    public double choose(double x, double y) {
        return x;
    }

    @Override
    public int getPos(UIEntity entity) {
        return (int) entity.getX();
    }

    @Override
    public void inheritsBounds(UIEntity entity, boolean inherit) {
        entity.setInheritWidth(inherit);
    }

    @Override
    public IBoxMode getOrthogonal() {
        return UIBox.VBOX;
    }

    @Override
    public double getWorldPos(UIEntity entity) {
        return entity.getWorldX();
    }
}