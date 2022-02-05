package eu.gir.girsignals.guis.guilib.entitys;

import java.util.function.IntConsumer;

import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;

public class UIScroll extends UIComponent {

	private IntConsumer consumer;
	
	public UIScroll(IntConsumer consumer) {
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
        if (event.state.equals(EnumMouseState.SCROLL) && parent.isHovered())
        {
            consumer.accept(event.x);
        }
	}
	
}
