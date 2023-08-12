package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class UIBlockRender extends UIComponent {

    private BakedModel model;
    private BlockState state;
    private BlockModelDataWrapper wrapper;

    @Override
    public void draw(final DrawInfo info) {
        if (model != null) {
            info.applyState(model, state, wrapper);
        }
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
