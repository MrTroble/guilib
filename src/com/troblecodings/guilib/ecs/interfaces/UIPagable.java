package com.troblecodings.guilib.ecs.interfaces;

import com.troblecodings.guilib.ecs.entitys.UIEntity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface UIPagable {

    int getPage();

    void setPage(int page);

    int getMaxPages();

    UIEntity getParent();
}