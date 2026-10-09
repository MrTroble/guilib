package com.troblecodings.core.interfaces;

import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public interface BlockModelDataWrapper {
    ModelData toModelData();
    boolean hasProperty(ModelProperty<?> property);
    <T> T getData(ModelProperty<T> property);
    <T> T setData(ModelProperty<T> property, T value);
}