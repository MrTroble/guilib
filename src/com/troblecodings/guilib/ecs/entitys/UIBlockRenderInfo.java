package com.troblecodings.guilib.ecs.entitys;

import java.util.Objects;

import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

public class UIBlockRenderInfo {

    public final BakedModel model;
    public final BlockState state;
    public final BlockModelDataWrapper wrapper;
    public Vec3i vector;

    public UIBlockRenderInfo(final BakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper) {
        this(model, state, wrapper, 0, 0, 0);
    }

    public UIBlockRenderInfo(final BakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper, final int x, final int y, final int z) {
        this.model = model;
        this.state = state;
        this.wrapper = wrapper;
        this.vector = new Vec3i(x, y, z);
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