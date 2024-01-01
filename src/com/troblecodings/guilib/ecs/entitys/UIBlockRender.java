package com.troblecodings.guilib.ecs.entitys;

import org.joml.Quaternionf;

import com.troblecodings.core.QuaternionWrapper;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class UIBlockRender extends UIComponent {

    private BakedModel model;
    private BlockState state;
    private BlockModelDataWrapper wrapper;
    private final Quaternionf quaternion = QuaternionWrapper.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    private final float scale;
    private final float height;

    public UIBlockRender(final float scale, final float height) {
        this.scale = scale;
        this.height = height;
    }

    @Override
    public void draw(final DrawInfo info) {
        if (model != null) {
            info.scale(scale, -scale, scale);
            info.translate(1.5, 0, 1.5);
            info.rotate(this.quaternion);
            info.translate(-0.5, this.height, -0.5);
            info.applyState(model, state, wrapper);
        }
    }

    public void updateRotation(final Quaternionf quaternion) {
        this.quaternion.mul(quaternion);
    }

    @Override
    public void update() {
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper) {
        this.setBlockState(state, wrapper, 0, 0, 0);
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper,
            final double x, final double y, final double z) {
        final BlockModelShaper shaper = Minecraft.getInstance().getModelManager()
                .getBlockModelShaper();
        this.model = shaper.getBlockModel(state);
        this.wrapper = wrapper;
        this.state = state;
    }

}
