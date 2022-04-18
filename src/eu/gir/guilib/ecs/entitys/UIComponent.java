package eu.gir.guilib.ecs.entitys;

import eu.gir.guilib.ecs.entitys.UIEntity.KeyEvent;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import eu.gir.guilib.ecs.entitys.UIEntity.UpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class UIComponent {

    protected UIEntity parent = null;
    protected boolean visible = true;

    public abstract void draw(final int mouseX, final int mouseY);

    public void exitDraw(final int mouseX, final int mouseY) {
    }

    public abstract void update();

    public void onAdd(final UIEntity entity) {
        this.parent = entity;
    }

    public void onRemove(final UIEntity entity) {
        this.parent = null;
    }

    public boolean hasParent() {
        return this.parent == null;
    }

    public void onClosed() {
    }

    public void postDraw(final int mouseX, final int mouseY) {
    }

    public void keyEvent(final KeyEvent event) {
    }

    public void mouseEvent(final MouseEvent event) {
    }

    public void updateEvent(final UpdateEvent event) {
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public UIEntity getParent() {
        return parent;
    }
}
