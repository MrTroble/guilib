package eu.gir.girsignals.guis.guilib.entitys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.gir.girsignals.guis.guilib.UIAutoSync;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class UIEntity extends UIComponent implements UIAutoSync, Iterable<UIEntity> {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private int worldY;
	private int worldX;
	private boolean hovered;
	private boolean inheritHeight;
	private boolean inheritWidth;
	private UpdateEvent lastUpdateEvent;
	
	protected ArrayList<UIEntity> children = new ArrayList<>();
	protected ArrayList<UIComponent> components = new ArrayList<>();
	
	public UIEntity() {
		this.setVisible(true);
		this.setInheritHeight(false);
		this.setInheritWidth(false);
	}
	
	public void setX(final int x) {
		this.x = x;
	}
	
	public void setY(final int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWorldY() {
		return worldY;
	}
	
	public int getWorldX() {
		return worldX;
	}
	
	@Override
	public synchronized void postDraw(final int mouseX, final int mouseY) {
		if (isVisible()) {
			children.forEach(c -> c.postDraw(mouseX, mouseY));
			components.forEach(c -> c.postDraw(mouseX, mouseY));
		}
	}
	
	@Override
	public synchronized void update() {
		if (lastUpdateEvent != null) {
			if (this.parent != null) {
				this.worldX = (this.x * lastUpdateEvent.guiScale) + parent.getWorldX();
				this.worldY = (this.y * lastUpdateEvent.guiScale) + parent.getWorldY();
			} else {
				this.worldX = (this.x * lastUpdateEvent.guiScale);
				this.worldY = (this.y * lastUpdateEvent.guiScale);
			}
			components.forEach(c -> c.update());
			children.forEach(c -> c.update());
		}
	}
	
	@Override
	public synchronized void draw(final int mouseX, final int mouseY) {
		if (isVisible()) {
			final int wX = this.getWorldX();
			final int wY = this.getWorldY();
			this.hovered = mouseX >= wX && mouseY >= wY && mouseX < wX + this.width && mouseY < wY + this.height;
			GlStateManager.pushMatrix();
			GlStateManager.translate(this.x, this.y, 0);
			components.forEach(c -> c.draw(mouseX, mouseY));
			children.forEach(c -> c.draw(mouseX, mouseY));
			children.forEach(c -> c.exitDraw(mouseX, mouseY));
			components.forEach(c -> c.exitDraw(mouseX, mouseY));
			GlStateManager.popMatrix();
		}
	}
	
	public synchronized void add(final UIComponent component) {
		if (!this.components.contains(component)) {
			this.components.add(component);
			component.onAdd(this);
			this.updateEvent(lastUpdateEvent);
		}
	}
	
	public synchronized void remove(final UIComponent component) {
		if (components.contains(component)) {
			this.components.remove(component);
			component.onRemove(this);
			update();
		}
	}
	
	public synchronized void add(final UIEntity component) {
		if (!this.children.contains(component) && component != this) {
			this.children.add(component);
			component.onAdd(this);
			this.updateEvent(lastUpdateEvent);
		}
	}
	
	public synchronized void remove(final UIEntity component) {
		if (children.contains(component)) {
			this.children.remove(component);
			component.onRemove(this);
			this.update();
		}
	}
	
	@Override
	public synchronized void onClosed() {
		children.forEach(c -> c.onClosed());
	}
	
	@Override
	public synchronized void mouseEvent(final MouseEvent event) {
		if (isVisible()) {
			this.children.forEach(c -> c.mouseEvent(event));
			this.components.forEach(c -> c.mouseEvent(event));
		}
	}
	
	@Override
	public synchronized void keyEvent(final KeyEvent event) {
		if (isVisible()) {
			this.children.forEach(c -> c.keyEvent(event));
			this.components.forEach(c -> c.keyEvent(event));
		}
	}
	
	@Override
	public synchronized void updateEvent(final UpdateEvent event) {
		this.lastUpdateEvent = event;
		this.children.forEach(c -> c.updateEvent(event));
		this.components.forEach(c -> c.updateEvent(event));
		update();
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public synchronized void read(NBTTagCompound compound) {
		children.forEach(e -> e.read(compound));
		components.forEach(c -> {
			if (c instanceof UIAutoSync)
				((UIAutoSync) c).read(compound);
		});
	}
	
	@Override
	public synchronized void write(NBTTagCompound compound) {
		children.forEach(e -> e.write(compound));
		components.forEach(c -> {
			if (c instanceof UIAutoSync)
				((UIAutoSync) c).write(compound);
		});
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	public synchronized void clearChildren() {
		synchronized (this) {
			this.children.forEach(entity -> entity.onRemove(this));
			this.children.clear();
		}
	}
	
	public synchronized void clearComponents() {
		synchronized (this) {
			this.components.forEach(entity -> entity.onRemove(this));
			this.components.clear();
		}
	}
	
	public synchronized void clear() {
		clearComponents();
		clearChildren();
	}
	
	public synchronized <T extends UIComponent> List<T> findRecursive(Class<T> c) {
		return findRecursive(this, c);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends UIComponent> List<T> findRecursive(UIEntity uiEntity, Class<T> c) {
		final ArrayList<T> components = new ArrayList<>();
		uiEntity.components.stream().filter(u -> u.getClass().equals(c)).forEach(f -> components.add((T) f));
		for (UIEntity nextEntity : uiEntity.children) {
			components.addAll(findRecursive(nextEntity, c));
		}
		return components;
	}
	
	public boolean inheritHeight() {
		return inheritHeight;
	}
	
	public void setInheritHeight(boolean inheritHeight) {
		this.inheritHeight = inheritHeight;
	}
	
	public boolean inheritWidth() {
		return inheritWidth;
	}
	
	public void setInheritWidth(boolean inheritWidth) {
		this.inheritWidth = inheritWidth;
	}
	
	public UpdateEvent getLastUpdateEvent() {
		return lastUpdateEvent;
	}
	
	public static enum EnumMouseState {
		CLICKED,
		RELEASE,
		MOVE,
		SCROLL
	}
	
	public static final class UpdateEvent {
		
		public final int width;
		public final int height;
		public final int scaleFactor;
		public final int guiScale;
		
		public UpdateEvent(final int width, final int height, final int scaleFactor, int guiScale) {
			this.width = width;
			this.height = height;
			this.scaleFactor = scaleFactor;
			this.guiScale = guiScale;
		}
	}
	
	public static final class KeyEvent {
		
		public final int keyCode;
		public final char typed;
		
		public KeyEvent(final int keyCode, char typed) {
			this.keyCode = keyCode;
			this.typed = typed;
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
	
	@Override
	public Iterator<UIEntity> iterator() {
		return this.children.iterator();
	}
	
	@Override
	public String getID() {
		return null;
	}
	
	@Override
	public void setID(String id) {
	}
	
}
