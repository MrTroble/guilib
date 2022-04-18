package eu.gir.guilib.ecs.interfaces;

import eu.gir.guilib.ecs.entitys.UIEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface UIPagable {

    public int getPage();

    public void setPage(int page);

    public int getMaxPages();

    public UIEntity getParent();
}
