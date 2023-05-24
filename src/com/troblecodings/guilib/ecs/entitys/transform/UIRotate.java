package com.troblecodings.guilib.ecs.entitys.transform;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIRotate extends UIComponent {

    private float rotateX, rotateY, rotateZ;

    @Override
    public void draw(final DrawInfo info) {
        GlStateManager.rotate(this.rotateX, 1, 0, 0);
        GlStateManager.rotate(this.rotateY, 0, 1, 0);
        GlStateManager.rotate(this.rotateZ, 0, 0, 1);
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
