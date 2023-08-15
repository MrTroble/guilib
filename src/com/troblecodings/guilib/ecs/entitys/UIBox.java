package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;

import com.troblecodings.guilib.ecs.interfaces.IBoxMode;
import com.troblecodings.guilib.ecs.interfaces.UIPagable;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIBox extends UIComponent implements UIPagable {

    private int page = 0;
    private int maxPages = 0;

    public static final VBoxMode VBOX = new VBoxMode();
    public static final HBoxMode HBOX = new HBoxMode();

    private final IBoxMode mode;
    private final int gap;
    private final ArrayList<UIEntity> boundsUpdate = new ArrayList<>();
    private boolean pageable = true;

    public UIBox(final IBoxMode mode, final int gap) {
        this.mode = mode;
        this.gap = gap;
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
        final int parentBounds = pageable ? mode.getBounds(parent) : Integer.MAX_VALUE;
        if (parentBounds < 1)
            return;
        updateBounds(parentBounds);
        updatePositions(parentBounds);
        parent.children.forEach(mode::post);
    }

    protected void updateBounds(final int parentValue) {
        int y = 0;
        for (final UIEntity entity : parent.children) {
            if (mode.inheritsBounds(entity)) {
                boundsUpdate.add(entity);
            } else {
                y += mode.getBounds(entity) + gap;
            }
            if (y > parentValue) {
                if (!boundsUpdate.isEmpty()) {
                    boundsUpdate.forEach(e -> mode.setBounds(e, this.mode.getMin(e)));
                    boundsUpdate.clear();
                }
                y = 0;
            }
        }
        if (boundsUpdate.isEmpty())
            return;
        final float rest = Math.max(parentValue - y, 0);
        final int sizePer = Math.round(rest / boundsUpdate.size()) - gap * 2;
        boundsUpdate.forEach(e -> mode.setBounds(e, sizePer));
        boundsUpdate.clear();
    }

    protected void updatePositions(final int parentValue) {
        int cPage = 0;
        int y = 0;
        for (final UIEntity entity : parent.children) {
            mode.setPos(entity, y);
            final int oBounds = mode.getBounds(entity);
            y += oBounds;
            if (y > parentValue) {
                mode.setPos(entity, 0);
                y = oBounds;
                cPage++;
            }
            y += gap;
            entity.setVisible(cPage == page);
        }
        maxPages = cPage + 1;
    }

    @Override
    public void onAdd(final UIEntity entity) {
        super.onAdd(entity);
        update();
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(final int page) {
        this.page = page;
        update();
    }

    @Override
    public int getMaxPages() {
        return maxPages;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(final boolean pageable) {
        this.pageable = pageable;
    }

}
