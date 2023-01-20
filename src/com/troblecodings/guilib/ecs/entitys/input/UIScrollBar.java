package com.troblecodings.guilib.ecs.entitys.input;

import java.util.function.DoubleConsumer;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIScrollBox;
import com.troblecodings.guilib.ecs.interfaces.IBoxMode;

public class UIScrollBar extends UIComponent {

    private final UIScrollBox box;
    private final int inset;
    private final DoubleConsumer consumer;
    private double internalValue;
    private boolean wasClicked = false;
    private final UIScroll scroll;

    public UIScrollBar(UIScrollBox box, int inset, DoubleConsumer consumer, UIScroll scroll) {
        this.box = box;
        this.inset = inset;
        this.consumer = consumer;
        this.scroll = scroll;
        this.scroll.consumer = this::onScroll;
        this.internalValue = 0;
    }

    private void onScroll(double in) {
        internalValue = clamp(internalValue - (in * 0.05));
        consumer.accept(internalValue);
    }

    private double clamp(double in) {
        return Math.min(1.0f, Math.max(0.0f, in));
    }

    private void updateValue(double data) {
        final IBoxMode mode = box.getMode();
        final double pos = data - mode.getWorldPos(parent);
        final double localValue = pos / (mode.getBounds(parent) - inset);
        internalValue = clamp(localValue);
        consumer.accept(internalValue);
    }

    public double value() {
        return internalValue;
    }

    @Override
    public void mouseEvent(MouseEvent event) {
        if (parent.isHovered()) {
            if (event.state.equals(EnumMouseState.CLICKED)) {
                wasClicked = true;
            }
        }
        if (event.state.equals(EnumMouseState.RELEASE)) {
            wasClicked = false;
        }
        if (wasClicked) {
            updateValue(box.getMode().choose(event.x, event.y));
        }
    }

    @Override
    public void draw(DrawInfo info) {
    }

    @Override
    public void update() {
    }
}
