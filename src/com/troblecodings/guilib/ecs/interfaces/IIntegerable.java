package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.core.I18Wrapper;

import net.minecraft.client.gui.Font;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IIntegerable<T> {

    T getObjFromID(int obj);

    int count();

    String getName();

    @OnlyIn(Dist.CLIENT)
    default String getLocalizedName() {
        return I18Wrapper.format("property." + this.getName() + ".name");
    }

    @OnlyIn(Dist.CLIENT)
    default String getNamedObj(final int obj) {
        return getLocalizedName() + ": " + I18Wrapper.format("property.value." + getObjFromID(obj));
    }

    @OnlyIn(Dist.CLIENT)
    default String getDescriptionForName() {
        return I18Wrapper.format("property." + this.getName() + ".desc");
    }

    @OnlyIn(Dist.CLIENT)
    default int getMaxWidth(final Font render) {
        int ret = 0;
        for (int i = 0; i < this.count(); i++) {
            final int newVal = render.width(getNamedObj(i));
            if (ret < newVal)
                ret = newVal;
        }
        return ret;
    }

}