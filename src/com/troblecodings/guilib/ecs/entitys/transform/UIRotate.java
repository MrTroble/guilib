package com.troblecodings.guilib.ecs.entitys.transform;

import com.troblecodings.core.QuaternionWrapper;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIRotate extends UIComponent {

    private float rotateX, rotateY, rotateZ;

    @Override
    public void draw(final DrawInfo info) {
        info.stack.mulPose(QuaternionWrapper.fromXYZ(rotateX, rotateY, rotateZ));
    }

    @Override
    public void update() {
    }

    public float getRotateX() {
        return rotateX;
    }

    public void setRotateX(final float rotateX) {
        this.rotateX = rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public void setRotateY(final float rotateY) {
        this.rotateY = rotateY;
    }

    public float getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(final float rotateZ) {
        this.rotateZ = rotateZ;
    }

}
