package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.GuiConfigHandler;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class UIComponent {

    protected UIEntity parent = null;
    protected boolean visible = true;
    protected int basicTextColor = GuiConfigHandler.basicTextColor;
    protected int infoTextColor = GuiConfigHandler.infoTextColor;
    protected int errorTextColor = GuiConfigHandler.errorTextColor;

    public abstract void draw(final DrawInfo info);

    public void exitDraw(final DrawInfo info) {
    }

    public abstract void update();

    public void onAdd(final UIEntity entity) {
        this.parent = entity;
    }

    public void onRemove(final UIEntity entity) {
        this.parent = null;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public void onClosed() {
    }

    public void postDraw(final DrawInfo info) {
    }

    public void keyEvent(final KeyEvent event) {
    }

    public void mouseEvent(final MouseEvent event) {
    }

    public void updateEvent(final UpdateEvent event) {
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public UIEntity getParent() {
        return parent;
    }

    public int getBasicTextColor() {
        return basicTextColor;
    }

    public int getInfoTextColor() {
        return infoTextColor;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }
}
