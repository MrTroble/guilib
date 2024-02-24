package com.troblecodings.guilib.ecs.entitys;

import java.util.Objects;

import com.troblecodings.core.VectorWrapper;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class UIBlockRenderInfo {

    public final IBakedModel model;
    public final IBlockState state;
    public final BlockModelDataWrapper wrapper;
    public VectorWrapper vector;

    public UIBlockRenderInfo(final IBlockState state, final BlockModelDataWrapper wrapper) {
        this(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                .getModelForState(state instanceof IExtendedBlockState
                        ? ((IExtendedBlockState) state).getClean()
                        : state),
                state, wrapper);
    }

    public UIBlockRenderInfo(final IBakedModel model, final IBlockState state,
            final BlockModelDataWrapper wrapper) {
        this(model, state, wrapper, new VectorWrapper(0, 0, 0));
    }

    public UIBlockRenderInfo(final IBlockState state, final BlockModelDataWrapper wrapper,
            final VectorWrapper vector) {
        this(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                .getModelForState(state instanceof IExtendedBlockState
                        ? ((IExtendedBlockState) state).getClean()
                        : state),
                state, wrapper, vector);
    }

    public UIBlockRenderInfo(final IBakedModel model, final IBlockState state,
            final BlockModelDataWrapper wrapper, final VectorWrapper vector) {
        this.model = model;
        this.state = state;
        this.wrapper = wrapper;
        this.vector = vector;
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