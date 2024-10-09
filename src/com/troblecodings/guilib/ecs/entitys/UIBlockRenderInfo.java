package com.troblecodings.guilib.ecs.entitys;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.troblecodings.core.VectorWrapper;
import com.troblecodings.core.interfaces.BlockModelDataWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class UIBlockRenderInfo {

    public final BakedModel model;
    public final BlockState state;
    public final BlockModelDataWrapper wrapper;
    public VectorWrapper vector;
    public Predicate<BlockModelDataWrapper> predicate = t -> true;
    public Consumer<DrawInfo> consumer = t -> {
    };

    public UIBlockRenderInfo(final BlockState state, final BlockModelDataWrapper wrapper) {
        this(Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state),
                state, wrapper);
    }

    public UIBlockRenderInfo(final BakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper) {
        this(model, state, wrapper, new VectorWrapper(0, 0, 0));
    }

    public UIBlockRenderInfo(final BlockState state, final BlockModelDataWrapper wrapper,
            final VectorWrapper vector) {
        this(Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state),
                state, wrapper, vector);
    }

    public UIBlockRenderInfo(final BakedModel model, final BlockState state,
            final BlockModelDataWrapper wrapper, final VectorWrapper vector) {
        this.model = model;
        this.state = state;
        this.wrapper = wrapper;
        this.vector = vector;
    }

    public boolean test() {
        return predicate.test(wrapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, predicate, state, vector, wrapper);
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
        return Objects.equals(model, other.model) && Objects.equals(predicate, other.predicate)
                && Objects.equals(state, other.state) && Objects.equals(vector, other.vector)
                && Objects.equals(wrapper, other.wrapper);
    }

}