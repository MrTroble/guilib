package eu.gir.guilib.ecs.entitys.transform;

import eu.gir.guilib.ecs.entitys.UIComponent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIIndependentTranslate extends UIComponent {

    private double x, y, z;

    public UIIndependentTranslate() {
    }

    public UIIndependentTranslate(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
        GlStateManager.translate(x, y, z);
    }

    @Override
    public void update() {
    }

}
