package com.troblecodings.guilib.ecs.entitys.input;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.interfaces.BiDoubleConsumer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIDrag extends UIComponent {

    private final int button;
    private boolean drag = false;
    private double oldX = 0;
    private double oldY = 0;

    private final BiDoubleConsumer consumer;

    public UIDrag(final BiDoubleConsumer consumer) {
        this(consumer, 0);
    }

    public UIDrag(final BiDoubleConsumer consumer, final int button) {
        this.button = button;
        this.consumer = consumer;
    }

    @Override
    public void draw(final DrawInfo info) {
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
