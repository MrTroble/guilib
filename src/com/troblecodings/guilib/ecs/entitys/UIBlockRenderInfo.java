package com.troblecodings.guilib.ecs.entitys;

import java.util.Objects;

import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.vector.Vector3i;

public class UIBlockRenderInfo {

    public final IBakedModel model;
    public final BlockState state;
    public final BlockModelDataWrapper wrapper;
    public Vector3i vector;

    public UIBlockRenderInfo(final BlockState state, final BlockModelDataWrapper wrapper) {
        this(Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state),
                state, wrapper);
    }

    public UIBlockRenderInfo(final IBakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper) {
        this(model, state, wrapper, new Vector3i(0, 0, 0));
    }

    public UIBlockRenderInfo(final BlockState state, final BlockModelDataWrapper wrapper,
            final Vector3i vector) {
        this(Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state),
                state, wrapper, vector);
    }

    public UIBlockRenderInfo(final IBakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper, final Vector3i vector) {
        this.model = model;
        this.state = state;
        this.wrapper = wrapper;
        this.vector = vector;
    }

    public UIBlockRenderInfo with(final Vector3i vector) {
        this.vector = vector;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, state, wrapper, vector);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UIBlockRenderInfo other = (UIBlockRenderInfo) obj;
        return Objects.equals(model, other.model) && Objects.equals(state, other.state)
                && Objects.equals(wrapper, other.wrapper) && vector.equals(other.vector);
    }
}