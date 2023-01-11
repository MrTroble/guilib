package com.troblecodings.guilib.ecs.entitys;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.troblecodings.core.NBTWrapper;
import com.troblecodings.signals.signalbox.entrys.INetworkSavable;
import com.troblecodings.signals.signalbox.entrys.ISaveable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class UIEntity extends UIComponent implements Iterable<UIEntity> {

    private double x, y;
    private double worldY, worldX;
    private double minWidth, minHeight;
    private double width, height;
    private double worldWidth, worldHeight;
    private float scaleX, scaleY;
    private float worldScaleX, worldScaleY;
    private boolean hovered;
    private boolean inheritHeight;
    private boolean inheritWidth;
    private UpdateEvent lastUpdateEvent;

    protected ArrayList<UIEntity> children = new ArrayList<>();
    protected ArrayList<UIComponent> components = new ArrayList<>();

    public UIEntity() {
        this.setVisible(true);
        this.setInheritHeight(false);
        this.setInheritWidth(false);
        this.scaleX = 1;
        this.scaleY = 1;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLevelY() {
        return worldY;
    }

    public double getLevelX() {
        return worldX;
    }

    @Override
    public synchronized void postDraw(final DrawInfo info) {
        if (isVisible()) {
            children.forEach(c -> c.postDraw(info));
            components.forEach(c -> c.postDraw(info));
        }
    }

    @Override
    public synchronized void update() {
        if (lastUpdateEvent != null) {
            final double cX = this.x * lastUpdateEvent.guiScale;
            final double cY = this.y * lastUpdateEvent.guiScale;
            if (this.parent != null) {
                this.worldScaleX = this.scaleX * parent.getLevelScaleX();
                this.worldScaleY = this.scaleY * parent.getLevelScaleY();
                this.worldX = (int) (cX * parent.worldScaleX + parent.getLevelX());
                this.worldY = (int) (cY * parent.worldScaleY + parent.getLevelY());
            } else {
                this.worldScaleX = this.scaleX;
                this.worldScaleY = this.scaleY;
                this.worldX = (int) (cX * this.scaleX);
                this.worldY = (int) (cY * this.scaleY);
            }
            this.worldWidth = (int) (this.worldScaleX * this.width);
            this.worldHeight = (int) (this.worldScaleY * this.height);
            components.forEach(c -> c.update());
            children.forEach(c -> c.update());
        }
    }

    @Override
    public synchronized void draw(final DrawInfo info) {
        if (isVisible()) {
            final double wX = this.getLevelX();
            final double wY = this.getLevelY();
            this.hovered = info.mouseX >= wX && info.mouseY >= wY && info.mouseX < wX + worldWidth
                    && info.mouseY < wY + worldHeight;
            info.stack.pushPose();
            info.stack.translate(this.x, this.y, 0);
            info.stack.scale(scaleX, scaleY, 1);
            components.forEach(c -> c.draw(info));
            children.forEach(c -> c.draw(info));
            children.forEach(c -> c.exitDraw(info));
            components.forEach(c -> c.exitDraw(info));
            info.stack.popPose();
        }
    }

    public synchronized void add(final UIComponent component) {
        if (!this.components.contains(component)) {
            this.components.add(component);
            component.onAdd(this);
            this.updateEvent(lastUpdateEvent);
        }
    }

    public synchronized void remove(final UIComponent component) {
        if (components.contains(component)) {
            this.components.remove(component);
            component.onRemove(this);
            update();
        }
    }

    public synchronized void add(final UIEntity component) {
        if (!this.children.contains(component) && component != this) {
            this.children.add(component);
            component.onAdd(this);
            this.updateEvent(lastUpdateEvent);
        }
    }

    public synchronized void remove(final UIEntity component) {
        if (children.contains(component)) {
            this.children.remove(component);
            component.onRemove(this);
            this.update();
        }
    }

    @Override
    public synchronized void onClosed() {
        children.forEach(c -> c.onClosed());
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized void mouseEvent(final MouseEvent event) {
        if (isVisible()) {
            ((Iterable<UIComponent>) this.components.clone()).forEach(c -> c.mouseEvent(event));
            ((Iterable<UIEntity>) this.children.clone()).forEach(c -> c.mouseEvent(event));
        }
    }

    @Override
    public synchronized void keyEvent(final KeyEvent event) {
        if (isVisible()) {
            this.children.forEach(c -> c.keyEvent(event));
            this.components.forEach(c -> c.keyEvent(event));
        }
    }

    @Override
    public synchronized void updateEvent(final UpdateEvent event) {
        this.lastUpdateEvent = event;
        this.children.forEach(c -> c.updateEvent(event));
        this.components.forEach(c -> c.updateEvent(event));
        update();
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(final double height) {
        this.height = height;
    }

    public void setWidth(final double width) {
        this.width = width;
    }

    public boolean isHovered() {
        return hovered;
    }

    public synchronized void clearChildren() {
        synchronized (this) {
            this.children.forEach(entity -> entity.onRemove(this));
            this.children.clear();
        }
    }

    public synchronized void clearComponents() {
        synchronized (this) {
            this.components.forEach(entity -> entity.onRemove(this));
            this.components.clear();
        }
    }

    public synchronized void clear() {
        clearComponents();
        clearChildren();
    }

    public synchronized <T extends UIComponent> List<T> findRecursive(final Class<T> c) {
        return findRecursive(this, c);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends UIComponent> List<T> findRecursive(final UIEntity uiEntity,
            final Class<T> c) {
        final ArrayList<T> components = new ArrayList<>();
        uiEntity.components.stream().filter(u -> u.getClass().equals(c))
                .forEach(f -> components.add((T) f));
        for (final UIEntity nextEntity : uiEntity.children) {
            components.addAll(findRecursive(nextEntity, c));
        }
        return components;
    }

    public boolean inheritHeight() {
        return inheritHeight;
    }

    public void setInheritHeight(final boolean inheritHeight) {
        this.inheritHeight = inheritHeight;
    }

    public boolean inheritWidth() {
        return inheritWidth;
    }

    public void setInheritWidth(final boolean inheritWidth) {
        this.inheritWidth = inheritWidth;
    }

    public UpdateEvent getLastUpdateEvent() {
        return lastUpdateEvent;
    }

    public static enum EnumMouseState {
        CLICKED, RELEASE, MOVE, SCROLL
    }

    public static final class UpdateEvent {

        public final int width;
        public final int height;
        public final int scaleFactor;
        public final int guiScale;

        public UpdateEvent(final int width, final int height, final int scaleFactor,
                final int guiScale) {
            this.width = width;
            this.height = height;
            this.scaleFactor = scaleFactor;
            this.guiScale = guiScale;
        }
    }

    public static final class KeyEvent {

        public final int keyCode;
        public final char typed;

        public KeyEvent(final int keyCode, final char typed) {
            this.keyCode = keyCode;
            this.typed = typed;
        }
    }

    public static final class MouseEvent {

        public final double x;
        public final double y;
        public final int key;
        public final EnumMouseState state;

        public MouseEvent(final double x, final double y, final int key, final EnumMouseState enumState) {
            this.x = x;
            this.y = y;
            this.key = key;
            this.state = enumState;
        }
    }

    @Override
    public Iterator<UIEntity> iterator() {
        return this.children.iterator();
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(final float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(final float scaleY) {
        this.scaleY = scaleY;
    }

    public float getLevelScaleX() {
        return worldScaleX;
    }

    public float getLevelScaleY() {
        return worldScaleY;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(final double minWidth) {
        this.minWidth = minWidth;
    }

    public double getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(final double minHeight) {
        this.minHeight = minHeight;
    }

}
