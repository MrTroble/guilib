package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.guilib.ecs.entitys.UIEntity;

public interface IBoxMode {

    int getBounds(UIEntity entity);
    
    int getPos(UIEntity entity);

    double getWorldPos(UIEntity entity);

    void setBounds(UIEntity entity, int bounds);

    void setPos(UIEntity entity, int pos);

    boolean inheritsBounds(UIEntity entity);

    void inheritsBounds(UIEntity entity, boolean inherit);

    void post(UIEntity entity);

    int getMin(UIEntity entity);
    
    double choose(double x, double y);
    
    IBoxMode getOrthogonal();
}