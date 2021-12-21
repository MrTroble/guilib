package eu.gir.girsignals.guis.guilib;

import java.util.function.Consumer;

import eu.gir.girsignals.guis.guilib.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.UIEntity.MouseEvent;

public class UIClickable extends UIComponent {

	private Consumer<UIEntity> callback;

	public UIClickable(final Consumer<UIEntity> callback) {
		this.callback = callback;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

	@Override
	public void update() {
	}

	@Override
	public void mouseEvent(MouseEvent event) {
		if (event.state == EnumMouseState.RELEASE && this.parent.isVisible()) {
			if (this.parent.isHovered()) {
				callback.accept(this.parent);
			}
		}
	}

}

