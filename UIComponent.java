package eu.gir.girsignals.guis.guilib;

import eu.gir.girsignals.guis.guilib.UIEntity.KeyEvent;
import eu.gir.girsignals.guis.guilib.UIEntity.MouseEvent;
import eu.gir.girsignals.guis.guilib.UIEntity.UpdateEvent;

public abstract class UIComponent {

	protected UIEntity parent = null;
	protected boolean visible = true;

	public abstract void draw(final int mouseX, final int mouseY);

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

	public void postDraw(final int mouseX, final int mouseY) {
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

}

