package com.troblecodings.guilib.ecs.entitys;

import com.mojang.math.Quaternion;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;

public class UIBlockRender extends UIComponent {

    private UIBlockRenderInfo renderInfo;
    private final Quaternion quaternion = Quaternion.fromXYZ(0.0f, (float) Math.PI, 0.0f);
    private final float scale;
    private final float height;

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
            info.applyState(renderInfo.model, renderInfo.state, renderInfo.wrapper,
                    renderInfo.vector.getX(), renderInfo.vector.getY(), renderInfo.vector.getZ());
        }
    }

    public void updateRotation(final Quaternion quaternion) {
        this.quaternion.mul(quaternion);
    }

    @Override
    public void update() {
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper) {
        this.setBlockState(state, wrapper, 0, 0, 0);
    }

    public void setBlockState(final BlockState state, final BlockModelDataWrapper wrapper,
            final int x, final int y, final int z) {
        final BlockModelShaper shaper = Minecraft.getInstance().getModelManager()
                .getBlockModelShaper();
        this.renderInfo = new UIBlockRenderInfo(shaper.getBlockModel(state), state, wrapper, x, y,
                z);
    }
}