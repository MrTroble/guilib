package eu.gir.guilib.ecs.entitys.input;

import eu.gir.guilib.ecs.entitys.UIComponent;
import eu.gir.guilib.ecs.entitys.UIEntity.EnumMouseState;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import eu.gir.guilib.ecs.interfaces.BiIntConsumer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIDrag extends UIComponent {
	
	private final int button;
	private boolean drag = false;
	private int oldX = 0;
	private int oldY = 0;
	
	private BiIntConsumer consumer;
	
	public UIDrag(BiIntConsumer consumer) {
		this(consumer, 0);
	}
	
	public UIDrag(BiIntConsumer consumer, int button) {
		this.button = button;
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
		if (event.state.equals(EnumMouseState.CLICKED) && event.key == this.button && this.parent.isHovered())
			drag = true;
		if (event.state.equals(EnumMouseState.RELEASE))
			drag = false;
		oldX = event.x;
		oldY = event.y;
	}
}
