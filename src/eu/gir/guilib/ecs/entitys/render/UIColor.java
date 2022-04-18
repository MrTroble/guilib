package eu.gir.guilib.ecs.entitys.render;

import eu.gir.guilib.ecs.entitys.UIComponent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIColor extends UIComponent {

    private int color;

    public UIColor(final int color) {
        this.setColor(color);
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
        GuiUtils.drawGradientRect(0, 0, 0, parent.getWidth(), parent.getHeight(), this.color,
                this.color);
    }

    @Override
    public void update() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(final int color) {
        this.color = color;
    }

}
