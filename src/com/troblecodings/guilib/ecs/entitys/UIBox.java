package com.troblecodings.guilib.ecs.entitys;

import java.util.ArrayList;

import com.troblecodings.guilib.ecs.interfaces.UIPagable;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIBox extends UIComponent implements UIPagable {

    private int page = 0;
    private int maxPages = 0;

    private static interface IBoxMode {

        int getBounds(UIEntity entity);

        void setBounds(UIEntity entity, int bounds);

        void setPos(UIEntity entity, int pos);

        boolean inheritsBounds(UIEntity entity);

        void post(UIEntity entity);

        int getMin(UIEntity entity);
    }

    public static final VBoxMode VBOX = new VBoxMode();
    public static final HBoxMode HBOX = new HBoxMode();

    private static class VBoxMode implements IBoxMode {

        @Override
        public int getBounds(final UIEntity entity) {
            return entity.getHeight();
        }

        @Override
        public void setBounds(final UIEntity entity, final int bounds) {
            entity.setHeight(bounds);
        }

        @Override
        public void setPos(final UIEntity entity, final int pos) {
            entity.setY(pos);
        }

        @Override
        public boolean inheritsBounds(final UIEntity entity) {
            return entity.inheritHeight();
        }

        @Override
        public void post(final UIEntity entity) {
            if (entity.inheritWidth())
                entity.setWidth(entity.getParent().getWidth());
        }

        @Override
        public int getMin(final UIEntity entity) {
            return entity.getMinHeight();
        }
    }

    private static class HBoxMode implements IBoxMode {

        @Override
        public int getBounds(final UIEntity entity) {
            return entity.getWidth();
        }

        @Override
        public void setBounds(final UIEntity entity, final int bounds) {
            entity.setWidth(bounds);
        }

        @Override
        public void setPos(final UIEntity entity, final int pos) {
            entity.setX(pos);
        }

        @Override
        public boolean inheritsBounds(final UIEntity entity) {
            return entity.inheritWidth();
        }

        @Override
        public void post(final UIEntity entity) {
            if (entity.inheritHeight())
                entity.setHeight(entity.getParent().getHeight());
        }

        @Override
        public int getMin(final UIEntity entity) {
            return entity.getMinWidth();
        }
    }

    private final IBoxMode mode;
    private final int gap;
    private final ArrayList<UIEntity> boundsUpdate = new ArrayList<>();
    private boolean pageable = true;

    public UIBox(final IBoxMode mode, final int gap) {
        this.mode = mode;
        this.gap = gap;
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
    }

    @Override
    public void update() {
        final int parentBounds = pageable ? mode.getBounds(parent) : Integer.MAX_VALUE;
        if (parentBounds < 1)
            return;
        updateBounds(parentBounds);
        updatePositions(parentBounds);
        parent.children.forEach(e -> mode.post(e));
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
