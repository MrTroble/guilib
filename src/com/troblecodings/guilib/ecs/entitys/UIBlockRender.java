package com.troblecodings.guilib.ecs.entitys;

import com.mojang.math.Quaternion;

public class UIBlockRender extends UIComponent {

    private UIBlockRenderInfo renderInfo;
    protected final Quaternion quaternion = Quaternion.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    protected final float scale;
    protected final float height;

    public UIBlockRender(final float scale, final float height) {
        this.scale = scale;
        this.height = height;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (renderInfo != null) {
            info.scale(scale, -scale, scale);
            info.translate(1.5, 0, 1.5);
            info.rotate(this.quaternion);
            info.translate(-0.5, this.height, -0.5);
            info.applyState(renderInfo);
        }
    }

    public void updateRotation(final Quaternion quaternion) {
        this.quaternion.mul(quaternion);
    }

    @Override
    public void update() {
    }

    public void setBlockState(final UIBlockRenderInfo info) {
        this.renderInfo = info;
    }
}