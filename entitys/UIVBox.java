package eu.gir.girsignals.guis.guilib.entitys;

import java.util.ArrayList;

public class UIVBox extends UIComponent {

	private int vGap = 0;
	private int page = 0;
	private int maxPages = 0;
	private ArrayList<UIEntity> boundsUpdate = new ArrayList<>();

	public UIVBox(final int vGap) {
		this.vGap = vGap;
	}

	private void calculateRest(int fixedSizes) {
		if (boundsUpdate.isEmpty())
			return;
		float rest = Math.max(parent.height - fixedSizes, 0);
		int sizePer = Math.round(rest / (float) boundsUpdate.size()) - this.vGap * 2;
		boundsUpdate.forEach(e -> e.height = sizePer);
		boundsUpdate.clear();
	}

	@Override
	public void update() {
		int y = 0;
		for (final UIEntity entity : parent.children) {
			if (entity.inheritsBounds()) {
				boundsUpdate.add(entity);
			} else {
				y += entity.height + vGap;
			}
			if (y >= parent.height) {
				calculateRest(y);
			}
		}
		calculateRest(y);
		y = 0;
		int cPage = 0;
		for (final UIEntity entity : parent.children) {
			entity.y = y;
			y += entity.height + vGap;
			if (y >= parent.height) {
				entity.y = 0;
				y = entity.height + vGap;
				cPage++;
			}
			entity.setVisible(cPage == page);
			entity.update();
		}
		maxPages = cPage + 1;
	}

	@Override
	public void onAdd(UIEntity entity) {
		super.onAdd(entity);
		update();
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		this.update();
	}

	public int getMaxPages() {
		return maxPages;
	}

}
