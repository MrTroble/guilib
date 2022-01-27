package eu.gir.girsignals.guis.guilib.entitys;

import eu.gir.girsignals.guis.guilib.BiIntConsumer;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIDrag extends UIComponent {
	
	private boolean drag = false;
	private int oldX = 0;
	private int oldY = 0;
	
	private BiIntConsumer consumer;
	
	public UIDrag(BiIntConsumer consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void mouseEvent(MouseEvent event) {
		if (drag) {
			this.consumer.accept(event.x - oldX, event.y - oldY);
		}
		if (event.state.equals(EnumMouseState.CLICKED) && this.parent.isHovered())
			drag = true;
		if (event.state.equals(EnumMouseState.RELEASE))
			drag = false;
		oldX = event.x;
		oldY = event.y;
	}
}
