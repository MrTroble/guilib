package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Quaternion;

public class UIMultiBlockRender extends UIBlockRender {

    private final List<UIBlockRenderInfo> models = new ArrayList<>();

    public UIMultiBlockRender(final float scale, final float height) {
        super(scale, height);
    }

    @Override
    public void draw(final DrawInfo info) {
        if (!models.isEmpty()) {
            info.scale(scale, -scale, scale);
            info.translate(1.5, 0, 1.5);
            info.rotate(this.quaternion);
            info.translate(-0.5, this.height, -0.5);
            models.forEach(renderInfo -> {
                if (!renderInfo.test())
                    return;
                info.applyState(renderInfo);
            });
        }

    }

    public void updateRotation(final Quaternion quaternion) {
        this.quaternion.mul(quaternion);
    }

    public void clear() {
        models.clear();
    }

    @Override
    public void update() {
    }

    @Override
    public void setBlockState(final UIBlockRenderInfo info) {
        if (!models.contains(info))
            models.add(info);
    }
}