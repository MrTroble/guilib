package com.troblecodings.guilib.ecs.entitys;

public class UIStack extends UIComponent {

    public UIStack() {
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
        parent.children.forEach(e -> {
            if (e.inheritWidth())
                e.setWidth(parent.getWidth());
            if (e.inheritHeight())
                e.setHeight(parent.getHeight());
        });
    }

}
