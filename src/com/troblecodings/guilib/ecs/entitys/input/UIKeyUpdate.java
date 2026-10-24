package com.troblecodings.guilib.ecs.entitys.input;

import java.util.function.Consumer;

import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIComponent;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;

public class UIKeyUpdate extends UIComponent {

    private Consumer<UIEntity> consumer;
    private final int typedChar;
    private final int keyCode;

    /**
     * This can be used to add a key update call when the Enter Button is sued.
     */
    public UIKeyUpdate(final Consumer<UIEntity> consumer) {
        this(consumer, 257, 28);
    }

    public UIKeyUpdate(final Consumer<UIEntity> consumer, final int typedChar, final int keyCode) {
        this.consumer = consumer;
        this.typedChar = typedChar;
        this.keyCode = keyCode;
    }

    public Consumer<UIEntity> getConsumer() {
        return consumer;
    }

    public void setConsumer(final Consumer<UIEntity> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void keyEvent(final KeyEvent event) {
        if (event.typedChar == typedChar && event.keyCode == keyCode) {
            consumer.accept(parent);
        }
    }

}
