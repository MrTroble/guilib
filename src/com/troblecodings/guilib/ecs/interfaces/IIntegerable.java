package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.core.I18Wrapper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IIntegerable<T> {

    public T getObjFromID(int obj);

    public int count();

    public String getName();

    @OnlyIn(Dist.CLIENT)
    default public String getLocalizedName() {
        return I18Wrapper.format("property." + this.getName() + ".name");
    }

    @OnlyIn(Dist.CLIENT)
    default public String getNamedObj(final int obj) {
        return getLocalizedName() + ": " + I18Wrapper.format("property.value." + getObjFromID(obj));
    }

    @OnlyIn(Dist.CLIENT)
    default public String getDescriptionForName() {
        return I18Wrapper.format("property." + this.getName() + ".desc");
    }

    @OnlyIn(Dist.CLIENT)
    default public int getMaxWidth(final FontRenderer render) {
        int ret = 0;
        for (int i = 0; i < this.count(); i++) {
            final int newVal = render.width(getNamedObj(i));
            if (ret < newVal)
                ret = newVal;
        }
        return ret;
    }

}
