package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import com.troblecodings.core.QuaternionWrapper;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3i;

public class UIMultiBlockRender extends UIComponent {

    private final List<UIBlockRenderInfo> models = new ArrayList<>();
    private final Quaternion quaternion = QuaternionWrapper.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    private final float scale;
    private final float height;
    private Vector3i previous = Vector3i.ZERO;

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
                final Vector3i current = new Vector3i(previous.getX() - renderInfo.vector.getX(),
                        previous.getY() - renderInfo.vector.getY(),
                        previous.getZ() - renderInfo.vector.getZ());
                info.applyState(renderInfo, current);
                previous = renderInfo.vector;
            });
            previous = Vector3i.ZERO;
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