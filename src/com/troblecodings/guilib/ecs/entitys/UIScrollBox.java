package com.troblecodings.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import com.troblecodings.guilib.ecs.interfaces.IBoxMode;

public class UIScrollBox extends UIComponent {

    private final IBoxMode mode;
    private final int spacer;
    private int wholeBounds = 0;
    private IntConsumer consumer;

    public UIScrollBox(final IBoxMode mode, final int spacer) {
        super();
        this.mode = mode;
        this.spacer = spacer;
    }

    public IntConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(final IntConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void update() {
        this.wholeBounds = 0;
        for (final UIEntity child : this.parent.children) {
            mode.setPos(child, this.wholeBounds);
            this.wholeBounds += mode.getBounds(child) + spacer;
            mode.post(child);
        }
        consumer.accept(wholeBounds);
    }

    public IBoxMode getMode() {
        return mode;
    }

    public int getWholeBounds() {
        return wholeBounds;
    }

    @Override
    public void draw(final DrawInfo info) {
    }

}
