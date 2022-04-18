package eu.gir.guilib.ecs.entitys.render;

import org.lwjgl.opengl.GL11;

import eu.gir.guilib.ecs.entitys.UIComponent;
import eu.gir.guilib.ecs.entitys.UIEntity.UpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIScissor extends UIComponent {

    private int x;
    private int y;
    private int width;
    private int height;

    @Override
    public void draw(final int mouseX, final int mouseY) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
    }

    @Override
    public void exitDraw(final int mouseX, final int mouseY) {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void update() {
        final UpdateEvent lastUpdateEvent = parent.getLastUpdateEvent();
        this.height = parent.getHeight() * lastUpdateEvent.scaleFactor;
        this.width = parent.getWidth() * lastUpdateEvent.scaleFactor;
        if (this.height < 0)
            this.height = 0;
        if (this.width < 0)
            this.width = 0;
        this.x = parent.getWorldX() * lastUpdateEvent.scaleFactor / lastUpdateEvent.guiScale;
        this.y = (lastUpdateEvent.height - parent.getWorldY() - parent.getHeight())
                * lastUpdateEvent.scaleFactor;
    }

}
