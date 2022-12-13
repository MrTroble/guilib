package com.troblecodings.guilib.ecs.entitys.input;

import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIOnUpdate extends UIComponent {

    private Runnable onUpdate;

    public UIOnUpdate(final Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public Runnable getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(final Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
        onUpdate.run();
    }

}
