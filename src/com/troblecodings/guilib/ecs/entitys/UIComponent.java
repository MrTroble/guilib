package com.troblecodings.guilib.ecs.entitys;

import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class UIComponent {

	protected UIEntity parent = null;
	protected boolean visible = true;

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
		return this.parent == null;
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
}
