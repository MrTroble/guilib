package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Quaternion;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class UIMultiBlockRender extends UIComponent {

    private final List<UIBlockRenderInfo> models = new ArrayList<>();
    private final Quaternion quaternion = Quaternion.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    private final float scale;
    private final float height;
    private Vec3i previous = Vec3i.ZERO;

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
                final Vec3i current = renderInfo.vector.subtract(previous);
                info.applyState(renderInfo.model, renderInfo.state, renderInfo.wrapper,
                        current.getX(), current.getY(), current.getZ());
                previous = renderInfo.vector;
            });
            previous = Vec3i.ZERO;
        }

    }

    public void updateRotation(final Quaternion quaternion) {
        this.quaternion.mul(quaternion);
    }

    @Override
    public void update() {
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper) {
        setBlockState(state, wrapper, 0, 0, 0);
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper,
            final int x, final int y, final int z) {
        final BlockModelShaper shaper = Minecraft.getInstance().getModelManager()
                .getBlockModelShaper();
        final UIBlockRenderInfo info = new UIBlockRenderInfo(shaper.getBlockModel(state), state,
                wrapper, x, y, z);
        if (!models.contains(info))
            models.add(info);
    }
}