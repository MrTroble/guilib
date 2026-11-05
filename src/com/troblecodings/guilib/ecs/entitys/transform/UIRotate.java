package com.troblecodings.guilib.ecs.entitys.transform;

import org.joml.Quaternionf;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIRotate extends UIComponent {

	public static float PERPENDICULAR_ANGLE = (float) (Math.PI / 2);
	
    private float rotateX, rotateY, rotateZ;

    @Override
    public void draw(final DrawInfo info) {
        info.stack.mulPose(new Quaternionf().rotationXYZ(rotateX, rotateY, rotateZ));
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