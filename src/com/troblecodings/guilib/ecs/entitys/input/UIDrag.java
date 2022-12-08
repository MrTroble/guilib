package com.troblecodings.guilib.ecs.entitys.input;

import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.interfaces.BiIntConsumer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIDrag extends UIComponent {

    private final int button;
    private boolean drag = false;
    private int oldX = 0;
    private int oldY = 0;

    private final BiIntConsumer consumer;

    public UIDrag(final BiIntConsumer consumer) {
        this(consumer, 0);
    }

    public UIDrag(final BiIntConsumer consumer, final int button) {
        this.button = button;
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
        if (drag) {
            this.consumer.accept(event.x - oldX, event.y - oldY);
        }
        if (event.state.equals(EnumMouseState.CLICKED) && event.key == this.button
                && this.parent.isHovered())
            drag = true;
        if (event.state.equals(EnumMouseState.RELEASE))
            drag = false;
        oldX = event.x;
        oldY = event.y;
    }
}
