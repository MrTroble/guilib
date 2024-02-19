package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Quaternion;
import com.troblecodings.core.VectorWrapper;

public class UIMultiBlockRender extends UIComponent {

    private final List<UIBlockRenderInfo> models = new ArrayList<>();
    private final Quaternion quaternion = Quaternion.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    private final float scale;
    private final float height;
    private VectorWrapper previous = VectorWrapper.ZERO;

    public UIMultiBlockRender(final float scale, final float height) {
        this.scale = scale;
        this.height = height;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (!models.isEmpty()) {
            info.scale(scale, -scale, scale);
            info.translate(1.5, 0, 1.5);
            info.rotate(this.quaternion);
            info.translate(-0.5, this.height, -0.5);
            models.forEach(renderInfo -> {
                final VectorWrapper current = renderInfo.vector.subtract(previous);
                info.applyState(renderInfo, current);
                previous = renderInfo.vector;
            });
            previous = VectorWrapper.ZERO;
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

    public void setBlockState(final UIBlockRenderInfo info) {
        if (!models.contains(info))
            models.add(info);
    }
}