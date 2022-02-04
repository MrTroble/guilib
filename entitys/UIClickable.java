package eu.gir.girsignals.guis.guilib.entitys;

import java.util.function.Consumer;

import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIClickable extends UIComponent {
	
	private Consumer<UIEntity> callback;
	private final int button;
	
	public UIClickable(final Consumer<UIEntity> callback) {
		this(callback, 0);
	}
	
	public UIClickable(final Consumer<UIEntity> callback, int button) {
		this.button = button;
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
		if (event.state == EnumMouseState.RELEASE && event.key == this.button && this.parent.isVisible()) {
			if (this.parent.isHovered()) {
				callback.accept(this.parent);
			}
		}
	}
	
	public Consumer<UIEntity> getCallback() {
		return callback;
	}
	
	public void setCallback(Consumer<UIEntity> callback) {
		this.callback = callback;
	}
	
	public int getButton() {
		return button;
	}
	
}
