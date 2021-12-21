package eu.gir.girsignals.guis.guilib.entitys;

public class UIGrid extends UIComponent {

	private int vSize = 0;
	private int vGap = 0;
	private int hGap = 0;

	public UIGrid(final int vSize, final int vGap, final int hGap) {
		this.vGap = vGap;
		this.hGap = hGap;
		this.vSize = vSize;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

	@Override
	public void update() {
		int pX = 0;
		int pY = 0;
		int lastMax = 0;
		for (int i = 0; i < this.parent.children.size(); i++) {
			final int rest = i % (this.vSize);
			if (rest == 0) {
				pY += lastMax + this.hGap;
				lastMax = 0;
				pX = 0;
			}
			final UIEntity entity = this.parent.children.get(i);
			final int cheight = entity.getHeight();
			if (lastMax < cheight)
				lastMax = cheight;
			entity.setPos(pX, pY);
			pX += entity.getWidth() + this.vGap;
		}
	}

	public int getvSize() {
		return vSize;
	}

	public void setvSize(int vSize) {
		this.vSize = vSize;
	}

	public int getvGap() {
		return vGap;
	}

	public void setvGap(int vGap) {
		this.vGap = vGap;
	}

	public int gethGap() {
		return hGap;
	}

	public void sethGap(int hGap) {
		this.hGap = hGap;
	}

}
