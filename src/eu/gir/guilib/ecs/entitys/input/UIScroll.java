package eu.gir.guilib.ecs.entitys.input;

import java.util.function.IntConsumer;

import eu.gir.guilib.ecs.entitys.UIComponent;
import eu.gir.guilib.ecs.entitys.UIEntity.EnumMouseState;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIScroll extends UIComponent {

    private final IntConsumer consumer;

    public UIScroll(final IntConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
    }

    @Override
    public void update() {

    }

    @Override
    public void mouseEvent(final MouseEvent event) {
        if (event.state.equals(EnumMouseState.SCROLL) && parent.isHovered()) {
            consumer.accept(event.x);
        }
    }

}
