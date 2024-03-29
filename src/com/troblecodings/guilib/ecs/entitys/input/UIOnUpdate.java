package com.troblecodings.guilib.ecs.entitys.input;

import com.troblecodings.guilib.ecs.entitys.UIComponent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
    public void draw(final int mouseX, final int mouseY) {
    }

    @Override
    public void update() {
        onUpdate.run();
    }

}
