package eu.gir.girsignals.guis.guilib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiElements {

	public static final int STRING_COLOR = 14737632;
	public static final int IDENTIFIER = -2212321;
	public static final int OFFSET = 2;
	public static final int BUTTON_SIZE = 20;

	public static enum EnumMouseState {
		CLICKED, RELEASE, MOVE
	}

	public static class UIToolTip extends UIComponent {

		public final String descripton;
		private int width = 0;
		private int height = 0;

		public UIToolTip(final String descripton) {
			this.descripton = descripton;
		}

		@Override
		public void postDraw(final int mouseX, final int mouseY) {
			final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			final String desc = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.descripton
					: I18n.format("gui.keyprompt");
			GuiUtils.drawHoveringText(Arrays.asList(desc.split(System.lineSeparator())), mouseX, mouseY, width, height,
					-1, font);
		}

		@Override
		public void updateEvent(final UpdateEvent event) {
			this.width = event.width;
			this.height = event.height;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
		}

		@Override
		public void update() {
		}
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

	public static interface UIAutoSync {

		public void write(final NBTTagCompound compound);

		public void read(final NBTTagCompound compound);
	}

	public static abstract class UIComponent {

		protected UIEntity parent = null;
		protected boolean visible = true;

		public abstract void draw(final int mouseX, final int mouseY);

		public abstract void update();

		public void onAdd(final UIEntity entity) {
			this.parent = entity;
		}

		public void onRemove(final UIEntity entity) {
			this.parent = null;
		}

		public boolean hasParent() {
			return this.parent == null;
		}

		public void onClosed() {
		}

		public void postDraw(final int mouseX, final int mouseY) {
		}

		public void keyEvent(final KeyEvent event) {
		}

		public void mouseEvent(final MouseEvent event) {
		}

		public void updateEvent(final UpdateEvent event) {
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(final boolean visible) {
			this.visible = visible;
		}

	}

	public static final class UIEntity extends UIComponent implements UIAutoSync {

		private int x;
		private int y;
		private int scaleX;
		private int scaleY;
		private int width;
		private int height;
		private int worldY;
		private int worldX;
		private boolean hovered;
		private boolean inheritBounds;
		private int scale;

		private ArrayList<UIEntity> children = new ArrayList<>();
		private ArrayList<UIComponent> components = new ArrayList<>();

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
			if (isVisible())
				children.forEach(c -> c.postDraw(mouseX, mouseY));
		}

		@Override
		public void update() {
			components.forEach(c -> c.update());
			children.forEach(c -> c.update());
			if (this.parent != null) {
				this.worldX = (this.x * scale) + parent.getWorldX();
				this.worldY = (this.y * scale) + parent.getWorldY();
			} else {
				this.worldX = (this.x * scale);
				this.worldY = (this.y * scale);
			}
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
			component.onAdd(this);
			component.scale = this.scale;
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

	}

	public static void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
		fontRendererIn.drawStringWithShadow(text, (float) (x - fontRendererIn.getStringWidth(text) / 2), (float) y,
				color);
	}

	public static class UIButton extends UIComponent {

		public static final int DEFAULT_COLOR = 14737632;
		public static final int DEFAULT_DISABLED_COLOR = 10526880;
		public static final int DEFAULT_HOVER_COLOR = 16777120;

		protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

		private String text;
		private boolean enabled;

		public UIButton(final String text) {
			this.setVisible(true);
			this.setEnabled(true);
			this.text = text;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
			if (this.visible) {
				final Minecraft mc = Minecraft.getMinecraft();
				final FontRenderer fontrenderer = mc.fontRenderer;
				mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				final int offsetV = enabled ? (parent.isHovered() ? 2 : 1) : 0;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GuiUtils.drawTexturedModalRect(0, 0, 0, 46 + offsetV * 20, parent.width / 2, parent.height, 0);
				GuiUtils.drawTexturedModalRect(parent.width / 2, 0, 200 - parent.width / 2, 46 + offsetV * 20,
						parent.width / 2, parent.height, 0);
				final int colorUsed = enabled ? (parent.isHovered() ? DEFAULT_HOVER_COLOR : DEFAULT_COLOR)
						: DEFAULT_DISABLED_COLOR;
				drawCenteredString(fontrenderer, this.text, parent.width / 2, (parent.height - 8) / 2, colorUsed);
			}
		}

		@Override
		public void update() {
		}

		@Override
		public void mouseEvent(MouseEvent event) {
		}

		@Override
		public void onAdd(UIEntity entity) {
			super.onAdd(entity);
			update();
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

	}

	public static class UILabel extends UIComponent {

		public static final int DEFAULT_STRING_COLOR = 4210752;
		private String string;
		private int stringColor;
		private FontRenderer renderer;

		public UILabel(String text) {
			this.string = text;
			this.stringColor = DEFAULT_STRING_COLOR;
			this.renderer = Minecraft.getMinecraft().fontRenderer;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
			if (this.visible) {
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				renderer.drawString(string, 0, 0, stringColor);
			}
		}

		@Override
		public void update() {
			this.parent.height = renderer.FONT_HEIGHT;
			this.parent.width = renderer.getStringWidth(string);
		}

		public String getText() {
			return string;
		}

		public void setText(String string) {
			this.string = string;
		}

		public int getTextColor() {
			return stringColor;
		}

		public void setTextColor(int stringColor) {
			this.stringColor = stringColor;
		}

	}

	public static class UIHBox extends UIComponent {

		private int hGap = 0;

		public UIHBox(final int hGap) {
			this.hGap = hGap;
		}

		@Override
		public void update() {
			int x = 0;
			for (final UIEntity entity : parent.children) {
				entity.x = x;
				x += entity.width + hGap;
			}
		}

		@Override
		public void onAdd(UIEntity entity) {
			super.onAdd(entity);
			update();
		}

		@Override
		public void draw(int mouseX, int mouseY) {
		}

	}

	public static class UIVBox extends UIComponent {

		private int vGap = 0;
		private int page = 0;
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
					y = 0;
					cPage++;
				}
				entity.setVisible(cPage == page);
				entity.update();
			}
		}

		@Override
		public void onAdd(UIEntity entity) {
			super.onAdd(entity);
			update();
		}

		@Override
		public void draw(int mouseX, int mouseY) {
		}

	}

	public static class UIGrid extends UIComponent {

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

	public static class UIClickable extends UIComponent {

		private Consumer<UIEntity> callback;

		public UIClickable(final Consumer<UIEntity> callback) {
			this.callback = callback;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
		}

		@Override
		public void update() {
		}

		@Override
		public void mouseEvent(MouseEvent event) {
			if (event.state == EnumMouseState.RELEASE && this.parent.isVisible()) {
				if (this.parent.isHovered()) {
					callback.accept(this.parent);
				}
			}
		}

	}

	public static class UICheckBox extends UIComponent implements UIAutoSync {

		public static final int BOX_WIDTH = 11;

		private String id;
		private String text;
		private IntConsumer onChange;
		private boolean enabled;
		private boolean checked;

		public UICheckBox(final String id) {
			this.id = id;
			this.setVisible(true);
			this.setEnabled(true);
		}

		@Override
		public void update() {
			Minecraft mc = Minecraft.getMinecraft();
			this.parent.width = BOX_WIDTH + 4 + mc.fontRenderer.getStringWidth(text);
			this.parent.height = Math.max(BOX_WIDTH, mc.fontRenderer.FONT_HEIGHT) + 2;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
			Minecraft mc = Minecraft.getMinecraft();
			GuiUtils.drawContinuousTexturedBox(UIButton.BUTTON_TEXTURES, 0, 0, 0, 46, BOX_WIDTH, BOX_WIDTH, 200, 20, 2,
					3, 2, 2, 0);
			int color = this.enabled ? UIButton.DEFAULT_COLOR : UIButton.DEFAULT_DISABLED_COLOR;
			if (this.isChecked())
				drawCenteredString(mc.fontRenderer, "x", BOX_WIDTH / 2 + 1, 1, 14737632);
			mc.fontRenderer.drawStringWithShadow(text, BOX_WIDTH + 2.0f, 2.0f, color);
		}

		@Override
		public void write(NBTTagCompound compound) {
			compound.setBoolean(id, isChecked());
		}

		@Override
		public void read(NBTTagCompound compound) {
			this.setChecked(compound.getBoolean(id));
		}

		public IntConsumer getOnChange() {
			return onChange;
		}

		public void setOnChange(IntConsumer onChange) {
			this.onChange = onChange;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

	}

	public static class UIEnumerable extends UIComponent implements UIAutoSync {

		private IntConsumer onChange;
		private int index;
		private int max;
		private int min;
		private String id;

		public UIEnumerable(IntConsumer onChange, int max, String id) {
			this.onChange = onChange;
			this.max = max;
			this.id = id;
			this.min = 0;
		}

		@Override
		public void draw(int mouseX, int mouseY) {
		}

		@Override
		public void update() {
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
			this.onChange.accept(index);
		}

		@Override
		public void write(NBTTagCompound compound) {
			compound.setInteger(id, index);
		}

		@Override
		public void read(NBTTagCompound compound) {
			this.setIndex(compound.getInteger(id));
		}

		public int getMax() {
			return max;
		}

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public void setMax(int max) {
			this.max = max;
		}

		public IntConsumer getOnChange() {
			return onChange;
		}

		public void setOnChange(IntConsumer onChange) {
			this.onChange = onChange;
		}

	}

	public static UIEntity createBoolElement(IIntegerable<?> property, IntConsumer consumer) {
		final UIEntity middle = new UIEntity();
		middle.setBounds(Minecraft.getMinecraft().fontRenderer.getStringWidth(property.getLocalizedName()) + 20, 20);

		final UICheckBox middleButton = new UICheckBox(property.getName());
		middleButton.setOnChange(consumer);
		middleButton.setText(property.getLocalizedName());
		middle.add(middleButton);
		return middle;
	}

	public static UIEntity createEnumElement(IIntegerable<?> property, IntConsumer consumer) {
		final UIEntity middle = new UIEntity();
		middle.setBounds(property.getMaxWidth(Minecraft.getMinecraft().fontRenderer), 20);

		final UIButton middleButton = new UIButton(property.getNamedObj(0));
		final UIEnumerable enumerable = new UIEnumerable(consumer, property.count(), property.getName());
		enumerable.setOnChange(consumer.andThen(in -> middleButton.setText(property.getNamedObj(in))));
		middle.add(middleButton);
		middle.add(enumerable);

		final UIEntity left = new UIEntity();
		left.setBounds(20, 20);

		final UIButton leftButton = new UIButton("<");
		final UIClickable leftclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int min = enumerable.getMin();
			if (id == min)
				return;
			enumerable.setIndex(id - 1);
		});
		left.add(leftButton);
		left.add(leftclickable);

		final UIEntity right = new UIEntity();
		right.setBounds(20, 20);

		final UIButton rightButton = new UIButton(">");
		final UIClickable rightclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int min = enumerable.getMin();
			if (id == min)
				return;
			enumerable.setIndex(id - 1);
		});
		right.add(rightButton);
		right.add(rightclickable);

		final UIEntity hbox = new UIEntity();
		hbox.add(new UIHBox(1));
		hbox.add(left);
		hbox.add(middle);
		hbox.add(right);
		hbox.setBounds(left.getWidth() + middle.getWidth() + right.getWidth(), 20);
		return hbox;
	}
}