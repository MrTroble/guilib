package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.guilib.ecs.entitys.UIEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface UIPagable {

    public int getPage();

    public void setPage(int page);

    public int getMaxPages();

    public UIEntity getParent();
}
