package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.core.I18Wrapper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIntegerable<T> {

    public T getObjFromID(int obj);

    public int count();

    public String getName();

    @SideOnly(Side.CLIENT)
    default public String getLocalizedName() {
        return I18Wrapper.format("property." + this.getName() + ".name");
    }

    @SideOnly(Side.CLIENT)
    default public String getNamedObj(final int obj) {
        return getLocalizedName() + ": " + I18Wrapper.format("property.value." + getObjFromID(obj));
    }

    @SideOnly(Side.CLIENT)
    default public String getDescriptionForName() {
        return I18Wrapper.format("property." + this.getName() + ".desc");
    }

    @SideOnly(Side.CLIENT)
    default public int getMaxWidth(final FontRenderer render) {
        int ret = 0;
        for (int i = 0; i < this.count(); i++) {
            final int newVal = render.getStringWidth(getNamedObj(i));
            if (ret < newVal)
                ret = newVal;
        }
        return ret;
    }

}
