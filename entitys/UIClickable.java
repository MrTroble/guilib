package eu.gir.girsignals.guis.guilib.entitys;

import java.util.function.Consumer;

import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

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
