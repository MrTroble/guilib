package eu.gir.girsignals.guis.guilib.entitys;

import java.util.ArrayList;

import eu.gir.girsignals.guis.guilib.UIPagable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UIBox extends UIComponent implements UIPagable {
	
	private int page = 0;
	private int maxPages = 0;
	
	public static interface IBoxMode {
		
		int getBounds(UIEntity entity);
		
		void setBounds(UIEntity entity, int bounds);
		
		void setPos(UIEntity entity, int pos);
		
		boolean inheritsBounds(UIEntity entity);
		
		void post(UIEntity entity);
		
		default int getMin(UIEntity entity) {
			return 20;
		}
	}
	
	public static class VBoxMode implements IBoxMode {
		
		public static VBoxMode INSTANCE = new VBoxMode();
		
		@Override
		public int getBounds(UIEntity entity) {
			return entity.getHeight();
		}
		
		@Override
		public void setBounds(UIEntity entity, int bounds) {
			entity.setHeight(bounds);
		}
		
		@Override
		public void setPos(UIEntity entity, int pos) {
			entity.setY(pos);
		}
		
		@Override
		public boolean inheritsBounds(UIEntity entity) {
			return entity.inheritHeight();
		}
		
		@Override
		public void post(UIEntity entity) {
			if (entity.inheritWidth())
				entity.setWidth(entity.parent.getWidth());
		}
	}
	
	public static class HBoxMode implements IBoxMode {
		
		public static HBoxMode INSTANCE = new HBoxMode();
		
		@Override
		public int getBounds(UIEntity entity) {
			return entity.getWidth();
		}
		
		@Override
		public void setBounds(UIEntity entity, int bounds) {
			entity.setWidth(bounds);
		}
		
		@Override
		public void setPos(UIEntity entity, int pos) {
			entity.setX(pos);
		}
		
		@Override
		public boolean inheritsBounds(UIEntity entity) {
			return entity.inheritWidth();
		}
		
		@Override
		public void post(UIEntity entity) {
			if (entity.inheritHeight())
				entity.setHeight(entity.parent.getHeight());
		}
	}
	
	private final IBoxMode mode;
	private final int gap;
	private final ArrayList<UIEntity> boundsUpdate = new ArrayList<>();
	
	public UIBox(final IBoxMode mode, final int gap) {
		this.mode = mode;
		this.gap = gap;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
	}
	
	@Override
	public void update() {
		final int parentBounds = mode.getBounds(parent);
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
		final int sizePer = Math.round(rest / (float) boundsUpdate.size()) - gap * 2;
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
	public void onAdd(UIEntity entity) {
		super.onAdd(entity);
		update();
	}
	
	@Override
	public int getPage() {
		return page;
	}
	
	@Override
	public void setPage(int page) {
		this.page = page;
		update();
	}
	
	@Override
	public int getMaxPages() {
		return maxPages;
	}
}
