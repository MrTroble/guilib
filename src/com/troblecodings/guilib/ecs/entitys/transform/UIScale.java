package com.troblecodings.guilib.ecs.entitys.transform;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIScale extends UIComponent {

    private float scaleX, scaleY, scaleZ;

    public UIScale() {
        this.scaleX = 1;
        this.scaleY = 1;
        this.scaleZ = 1;
    }

    public UIScale(final float scaleX, final float scaleY, final float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    @Override
    public void draw(final DrawInfo info) {
    	info.stack.scale(this.scaleX, this.scaleY, this.scaleZ);
    }

    @Override
    public void update() {

    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(final float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(final float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(final float scaleZ) {
        this.scaleZ = scaleZ;
    }

}
