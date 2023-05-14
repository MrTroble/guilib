package com.troblecodings.guilib.ecs.entitys.transform;

import com.mojang.blaze3d.platform.GlStateManager;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.renderer.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIRotate extends UIComponent {

    private float rotateX, rotateY, rotateZ;

    public static Quaternion fromXYZ(final float rotateX, final float rotateY,
            final float rotateZ) {
        final Quaternion quaternion = new Quaternion();
        quaternion.mul(new Quaternion((float) Math.sin(rotateX / 2.0F), 0.0F, 0.0F,
                (float) Math.cos(rotateX / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, (float) Math.sin(rotateY / 2.0F), 0.0F,
                (float) Math.cos(rotateY / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, 0.0F, (float) Math.sin(rotateZ / 2.0F),
                (float) Math.cos(rotateZ / 2.0F)));
        return quaternion;
    }

    @Override
    public void draw(final DrawInfo info) {
        GlStateManager.rotatef(this.rotateX, 1, 0, 0);
        GlStateManager.rotatef(this.rotateY, 0, 1, 0);
        GlStateManager.rotatef(this.rotateZ, 0, 0, 1);
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
