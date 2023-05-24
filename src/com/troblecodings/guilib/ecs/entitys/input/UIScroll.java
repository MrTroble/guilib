package com.troblecodings.guilib.ecs.entitys.input;

import java.util.function.DoubleConsumer;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIScroll extends UIComponent {

    protected DoubleConsumer consumer;

    public UIScroll() {
        this.consumer = d -> {
        };
    }

    public UIScroll(final DoubleConsumer consumer) {
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
        if (event.state.equals(EnumMouseState.SCROLL) && parent.isHovered()) {
            consumer.accept(event.x);
        }
    }

}
