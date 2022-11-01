package com.troblecodings.guilib.ecs.entitys.input;

import java.util.function.Consumer;

import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIClickable extends UIComponent {

    private Consumer<UIEntity> callback;
    private final int button;

    public UIClickable(final Consumer<UIEntity> callback) {
        this(callback, 0);
    }

    public UIClickable(final Consumer<UIEntity> callback, final int button) {
        this.button = button;
        this.callback = callback;
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
    }

    @Override
    public void update() {
    }

    @Override
    public void mouseEvent(final MouseEvent event) {
        if (!this.visible)
            return;
        if (event.state == EnumMouseState.RELEASE && event.key == this.button
                && this.parent.isVisible()) {
            if (this.parent.isHovered()) {
                callback.accept(this.parent);
            }
        }
    }

    public Consumer<UIEntity> getCallback() {
        return callback;
    }

    public void setCallback(final Consumer<UIEntity> callback) {
        this.callback = callback;
    }

    public int getButton() {
        return button;
    }

}
