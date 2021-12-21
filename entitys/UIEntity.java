package eu.gir.girsignals.guis.guilib.entitys;

import java.util.ArrayList;
import java.util.List;

import eu.gir.girsignals.guis.guilib.GuiBase;
import eu.gir.girsignals.guis.guilib.UIAutoSync;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

public final class UIEntity extends UIComponent implements UIAutoSync {

	protected int x;
	protected int y;
	private int scaleX;
	private int scaleY;
	protected int width;
	protected int height;
	private int worldY;
	private int worldX;
	private boolean hovered;
	private boolean inheritBounds;
	private int scale;
	protected GuiBase base;

	protected ArrayList<UIEntity> children = new ArrayList<>();
	protected ArrayList<UIComponent> components = new ArrayList<>();

	public UIEntity() {
		this.setPos(0, 0);
		this.setScale(1, 1);
		this.setVisible(true);
		this.setInheritBounds(false);
	}

	public UIEntity(final int x, final int y, final int scaleX, final int scaleY) {
		this.setPos(x, y);
		this.setScale(scaleX, scaleY);
	}

	public void setPos(final int x, final int y) {
		this.x = x;
		this.y = y;
		update();
	}

	public void setScale(final int scaleX, final int scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		update();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getScaleX() {
		return scaleX;
	}

	public int getScaleY() {
		return scaleY;
	}

	public int getWorldY() {
		return worldY;
	}

	public int getWorldX() {
		return worldX;
	}

	@Override
	public void postDraw(final int mouseX, final int mouseY) {
		if (isVisible()) {
			children.forEach(c -> c.postDraw(mouseX, mouseY));
			components.forEach(c -> c.postDraw(mouseX, mouseY));
		}
	}

	@Override
	public void update() {
		components.forEach(c -> c.update());
		if (this.parent != null) {
			this.worldX = (this.x * scale) + parent.getWorldX();
			this.worldY = (this.y * scale) + parent.getWorldY();
		} else {
			this.worldX = (this.x * scale);
			this.worldY = (this.y * scale);
		}
		children.forEach(c -> c.update());
	}

	@Override
	public void draw(final int mouseX, final int mouseY) {
		if (isVisible()) {
			final int wX = this.getWorldX();
			final int wY = this.getWorldY();
			this.hovered = mouseX >= wX && mouseY >= wY && mouseX < wX + this.width && mouseY < wY + this.height;
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.x, this.y, 0);
			GlStateManager.scale(this.scaleX, this.scaleY, 1);
			children.forEach(c -> c.draw(mouseX, mouseY));
			components.forEach(c -> c.draw(mouseX, mouseY));
			GlStateManager.popMatrix();
		}
	}

	public void add(final UIComponent component) {
		this.components.add(component);
		component.onAdd(this);
		update();
	}

	public void remove(final UIComponent component) {
		this.components.remove(component);
		component.onRemove(this);
		update();
	}

	public void add(final UIEntity component) {
		this.children.add(component);
		component.setBase(base);
		component.onAdd(this);
		component.internalUpdateScale(this.scale);
		update();
	}

	public void remove(final UIEntity component) {
		this.children.remove(component);
		component.onRemove(this);
		update();
	}

	@Override
	public void onClosed() {
		children.forEach(c -> c.onClosed());
	}

	@Override
	public void mouseEvent(final MouseEvent event) {
		if (isVisible()) {
			this.children.forEach(c -> c.mouseEvent(event));
			this.components.forEach(c -> c.mouseEvent(event));
		}
	}

	@Override
	public void keyEvent(final KeyEvent event) {
		if (isVisible()) {
			this.children.forEach(c -> c.keyEvent(event));
			this.components.forEach(c -> c.keyEvent(event));
		}
	}

	@Override
	public void updateEvent(final UpdateEvent event) {
		this.children.forEach(c -> c.updateEvent(event));
		this.components.forEach(c -> c.updateEvent(event));
		this.scale = Math.max(event.width / event.height, event.height / event.width);
		update();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setBounds(int width, int height) {
		this.height = height;
		this.width = width;
		this.update();
	}

	public boolean inheritsBounds() {
		return inheritBounds;
	}

	public void setInheritBounds(boolean inheritBounds) {
		this.inheritBounds = inheritBounds;
		this.update();
	}

	@Override
	public void read(NBTTagCompound compound) {
		children.forEach(e -> e.read(compound));
		components.forEach(c -> {
			if (c instanceof UIAutoSync)
				((UIAutoSync) c).read(compound);
		});
	}

	@Override
	public void write(NBTTagCompound compound) {
		children.forEach(e -> e.write(compound));
		components.forEach(c -> {
			if (c instanceof UIAutoSync)
				((UIAutoSync) c).write(compound);
		});
	}

	public boolean isHovered() {
		return hovered;
	}

	public void clearChildren() {
		@SuppressWarnings("unchecked")
		final ArrayList<UIEntity> tmpChildren = (ArrayList<UIEntity>) this.children.clone();
		this.children.clear();
		tmpChildren.forEach(entity -> entity.onRemove(this));
		this.update();
	}

	public void setBase(GuiBase base) {
		this.base = base;
		children.forEach(e -> e.setBase(base));
	}

	public <T extends UIComponent> List<T> findRecursive(Class<T> c) {
		return findRecursive(this, c);
	}

	@SuppressWarnings("unchecked")
	public <T extends UIComponent> List<T> findRecursive(UIEntity uiEntity, Class<T> c) {
		final ArrayList<T> components = new ArrayList<>();
		uiEntity.components.stream().filter(u -> u.getClass().equals(c)).forEach(f -> components.add((T) f));
		for (UIEntity nextEntity : uiEntity.children) {
			components.addAll(findRecursive(nextEntity, c));
		}
		return components;
	}

	private void internalUpdateScale(int nScale) {
		this.scale = nScale;
		this.children.forEach(e -> e.internalUpdateScale(nScale));
	}

	public GuiBase getBase() {
		return base;
	}

	public static enum EnumMouseState {
		CLICKED, RELEASE, MOVE
	}

	public static final class UpdateEvent {
		public final int width;
		public final int height;
		public final int scaleFactor;

		public UpdateEvent(final int width, final int height, final int scaleFactor) {
			this.width = width;
			this.height = height;
			this.scaleFactor = scaleFactor;
		}
	}

	public static final class KeyEvent {
		public final int keyCode;

		public KeyEvent(final int keyCode) {
			this.keyCode = keyCode;
		}
	}

	public static final class MouseEvent {
		public final int x;
		public final int y;
		public final int key;
		public final EnumMouseState state;

		public MouseEvent(final int x, final int y, final int key, final EnumMouseState enumState) {
			this.x = x;
			this.y = y;
			this.key = key;
			this.state = enumState;
		}
	}

}
