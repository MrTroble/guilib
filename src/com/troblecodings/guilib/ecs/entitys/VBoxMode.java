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

    @Override
    public double choose(double x, double y) {
        return y;
    }

    @Override
    public int getPos(UIEntity entity) {
        return (int) entity.getY();
    }

    @Override
    public void inheritsBounds(UIEntity entity, boolean inherit) {
        entity.setInheritHeight(inherit);
    }
    
    @Override
    public IBoxMode getOrthogonal() {
        return UIBox.HBOX;
    }

    @Override
    public double getWorldPos(UIEntity entity) {
        return entity.getWorldY();
    }

}