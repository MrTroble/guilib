package eu.gir.girsignals.guis.guilib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

import org.lwjgl.input.Keyboard;

import eu.gir.girsignals.guis.guilib.DrawUtil.IntegerHolder;
import eu.gir.girsignals.guis.guilib.DrawUtil.SizeIntegerables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiElements {

	public static final int STRING_COLOR = 14737632;
	public static final int IDENTIFIER = -2212321;
	public static final int OFFSET = 2;
	public static final int BUTTON_SIZE = 20;

	@SideOnly(Side.CLIENT)
	public static abstract class GuiBasicWidget extends GuiButton {

		private final boolean autoSync;

		public GuiBasicWidget(final boolean autoSync) {
			super(IDENTIFIER, 0, 0, null);
			this.autoSync = autoSync;
		}

		public abstract void read(final NBTTagCompound tag);

		public abstract void write(final NBTTagCompound tag);

		public boolean isAutoSync() {
			return autoSync;
		}

		public boolean keyTyped(char typedChar, int keyCode) {
			return false;
		}

		public void updatePos(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		public void setVisible(final boolean visible) {
			this.visible = visible;
		}

		public boolean isVisible() {
			return this.visible;
		}

		public void update() {

		}

		public String getDescription() {
			return "";
		}

		public boolean drawHoverText(final int mouseX, final int mouseY, final FontRenderer font, final int width,
				final int height) {
			if (!this.isMouseOver())
				return false;
			final String str = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.getDescription()
					: I18n.format("gui.keyprompt");
			GuiUtils.drawHoveringText(Arrays.asList(str.split(System.lineSeparator())), mouseX, mouseY, width, height,
					-1, font);
			return true;
		}

	}

	public static class GuiSelectionArrows {

		protected boolean pressed = false, lor = false, lock = true;
		protected GuiButton leftButton;
		protected GuiButton rightButton;

		public void update(final int x, final int y, final int value, final int max, final int width) {
			update(x, y, value, max, width, 0);
		}

		public void update(final int x, final int y, final int value, final int max, final int width, final int min) {
			this.leftButton = new GuiButton(IDENTIFIER, x, y, "<");
			this.rightButton = new GuiButton(IDENTIFIER, x + width + BUTTON_SIZE + OFFSET * 2, y, ">");
			this.rightButton.setWidth(BUTTON_SIZE);
			this.leftButton.setWidth(BUTTON_SIZE);
			if (value <= min) {
				this.leftButton.enabled = false;
			}
			if (value >= max) {
				this.rightButton.enabled = false;
			}
		}

		public int drawSelection(Minecraft mc, int mouseX, int mouseY, float partialTicks, final int value,
				final int max) {
			return this.drawSelection(mc, mouseX, mouseY, partialTicks, value, max, 0);
		}

		public int drawSelection(Minecraft mc, int mouseX, int mouseY, float partialTicks, final int value,
				final int max, final int min) {
			this.rightButton.drawButton(mc, mouseX, mouseY, partialTicks);
			this.leftButton.drawButton(mc, mouseX, mouseY, partialTicks);
			if (lock && pressed) {
				lock = false;
				if (lor) {
					if (value - 1 == 0)
						this.leftButton.enabled = false;
					if (!this.rightButton.enabled)
						this.rightButton.enabled = true;
					return -1;
				} else {
					if (value + 1 == max)
						this.rightButton.enabled = false;
					if (!this.leftButton.enabled)
						this.leftButton.enabled = true;
					return 1;
				}
			}
			return 0;
		}

		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			if (!pressed && this.leftButton != null && this.rightButton != null) {
				lor = this.leftButton.mousePressed(mc, mouseX, mouseY);
				pressed = lor || this.rightButton.mousePressed(mc, mouseX, mouseY);
				return pressed;
			}
			return false;
		}

		public void mouseReleased(int mouseX, int mouseY) {
			lock = true;
			pressed = false;
		}

	}

	@SideOnly(Side.CLIENT)
	public static class GuiEnumerableSetting extends GuiBasicWidget {

		protected final GuiSelectionArrows arrows = new GuiSelectionArrows();
		public final IIntegerable<?> property;

		public IntConsumer consumer;
		public int value = 0;

		public GuiEnumerableSetting(final IIntegerable<?> property, final IntConsumer consumer) {
			this(property, consumer, false);
		}

		public GuiEnumerableSetting(final IIntegerable<?> property, final boolean autoSync) {
			this(property, (i) -> {
			}, autoSync);
		}

		public GuiEnumerableSetting(final IIntegerable<?> property, final IntConsumer consumer,
				final boolean autoSync) {
			super(autoSync);
			this.property = Objects.requireNonNull(property);
			this.width = 150;
			this.consumer = Objects.requireNonNull(consumer);
			this.displayString = this.property.getNamedObj(this.value);
		}

		public int getValue() {
			return this.value;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (visible) {
				final int result = arrows.drawSelection(mc, mouseX, mouseY, partialTicks, this.value,
						this.property.count() - 1);
				if(result != 0) {
					this.value += result;
					this.displayString = this.property.getNamedObj(this.value);
				}
				super.drawButton(mc, mouseX, mouseY, partialTicks);
			}
		}

		public int getMaxWidth(FontRenderer render) {
			return property.getMaxWidth(render);
		}

		@Override
		public void update() {
			this.arrows.update(x, y, value, this.property.count() - 1, this.width);
			x += BUTTON_SIZE + OFFSET;
		}

		public boolean keyTyped(char typedChar, int keyCode) {
			return false;
		}

		@Override
		public void read(final NBTTagCompound tag) {
			value = tag.getInteger(this.property.getName());
			this.displayString = this.property.getNamedObj(value);
		}

		@Override
		public void write(final NBTTagCompound tag) {
			tag.setInteger(this.property.getName(), value);
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			return arrows.mousePressed(mc, mouseX, mouseY);
		}

		@Override
		public void mouseReleased(int mouseX, int mouseY) {
			arrows.mouseReleased(mouseX, mouseY);
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiPageSelect extends GuiEnumerableSetting {

		protected final IntegerHolder holder;

		public GuiPageSelect(final ArrayList<ArrayList<GuiBasicWidget>> pageList) {
			super(new SizeIntegerables<String>("page", pageList.size(), idx -> null) {

				@Override
				public int count() {
					return pageList.size();
				}

				@Override
				public String getNamedObj(int obj) {
					return I18n.format("property." + this.getName() + ".name") + " " + (obj + 1) + "/"
							+ pageList.size();
				}
			}, true);
			this.holder = new IntegerHolder(0);
			this.consumer = inp -> {
				pageList.get(holder.getObj()).forEach(t -> t.setVisible(false));
				pageList.get(inp).forEach(t -> t.setVisible(true));
				holder.setObj(inp);
			};
			final FontRenderer render = Minecraft.getMinecraft().fontRenderer;
			this.setWidth(this.property.getMaxWidth(render) + OFFSET * 2);
		}

		@Override
		public void update() {
			super.update();
			this.displayString = this.property.getNamedObj(this.value);
		}

		@Override
		public void updatePos(int x, int y) {
			super.updatePos(x - (this.width / 2) + BUTTON_SIZE, y);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (visible) {
				this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2,
						this.y + (this.height - 8) / 2, STRING_COLOR);
				final int result = this.arrows.drawSelection(mc, mouseX, mouseY, partialTicks, this.value,
						this.property.count() - 1);
				if (result != 0) {
					this.value += result;
					this.displayString = this.property.getNamedObj(this.value);
					this.consumer.accept(value);
				}
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public static class GuiSettingCheckBox extends GuiEnumerableSetting {

		private final GuiCheckBox checkBox;

		public GuiSettingCheckBox(IIntegerable<?> property, IntConsumer consumer) {
			super(property, consumer);
			this.checkBox = new GuiCheckBox(-34720, 0, 0, property.getLocalizedName(), false);
			this.height = this.checkBox.height;
		}

		@Override
		public int getMaxWidth(FontRenderer render) {
			return this.checkBox.width - (BUTTON_SIZE * 2 + OFFSET * 2);
		}

		@Override
		public int getValue() {
			return isChecked() ? 1 : 0;
		}

		@Override
		public void update() {
		}

		@Override
		public boolean isMouseOver() {
			return this.checkBox.isMouseOver();
		}

		@Override
		public void updatePos(int x, int y) {
			this.checkBox.x = x;
			this.checkBox.y = y;
		}

		@Override
		public void setWidth(int width) {
			this.checkBox.width = width;
		}

		@Override
		public void setVisible(boolean visible) {
			this.checkBox.visible = visible;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			checkBox.drawButton(mc, mouseX, mouseY, partialTicks);
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			boolean flag = checkBox.mousePressed(mc, mouseX, mouseY);
			if (flag)
				consumer.accept(checkBox.isChecked() ? 0 : 1);
			return flag;
		}

		public boolean isChecked() {
			return this.checkBox.isChecked();
		}

		@Override
		public void read(NBTTagCompound tag) {
			super.read(tag);
			this.checkBox.setIsChecked(this.value == 1);
		}

		public void setIsChecked(boolean isChecked) {
			this.checkBox.setIsChecked(isChecked);
		}

	}

	@SideOnly(Side.CLIENT)
	public static class GuiSettingTextbox extends GuiBasicWidget {

		protected final GuiTextField textfield = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 100,
				20);
		private final Consumer<String> consumer;
		private final String name;

		public GuiSettingTextbox(final String name, final boolean autoSync) {
			this(name, autoSync, (s) -> {
			});
		}

		public GuiSettingTextbox(final String name, final boolean autoSync, final Consumer<String> consumer) {
			super(autoSync);
			this.consumer = consumer;
			this.name = name;
		}

		public String getText() {
			return textfield.getText();
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			textfield.drawTextBox();
		}

		@Override
		public void updatePos(int x, int y) {
			textfield.x = x;
			textfield.y = y;
		}

		@Override
		public void setWidth(int width) {
			textfield.width = width;
		}

		@Override
		public boolean keyTyped(char typedChar, int keyCode) {
			boolean flag = textfield.textboxKeyTyped(typedChar, keyCode);
			if (flag)
				consumer.accept(textfield.getText());
			return flag;
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			boolean b1 = super.mousePressed(mc, mouseX, mouseY);
			boolean b2 = textfield.mouseClicked(mouseX, mouseY, 0);
			return b1 || b2;
		}

		@Override
		public void read(NBTTagCompound tag) {
			this.textfield.setText(tag.getString(name));
		}

		@Override
		public void write(NBTTagCompound tag) {
			tag.setString(name, this.textfield.getText());
		}

		@Override
		public boolean isVisible() {
			return visible;
		}
	}

	public static class GuiDoubleSelect extends GuiSettingTextbox {

		public double select;
		private final DoubleConsumer selectcon;
		private int ogY = 0;
		public final String dname;
		public GuiSelectionArrows arrows = new GuiSelectionArrows();

		public GuiDoubleSelect(final String name, final boolean sync, final DoubleConsumer consumer) {
			super(name, sync);
			this.selectcon = consumer;
			this.dname = name;
		}

		@Override
		public void update() {
			final int fheight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 4;
			x = this.textfield.x;
			y = this.textfield.y;
			ogY = y;
			height += fheight;
			y += fheight;
			arrows.update(x, y, (int) Math.round(this.select * 100), -100, 100);
			x += BUTTON_SIZE + OFFSET;
			updatePos(x, y);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (!this.isVisible())
				return;
			mc.fontRenderer.drawStringWithShadow(dname, x, ogY, STRING_COLOR);
			super.drawButton(mc, mouseX, mouseY, partialTicks);
			final float mul = arrows.drawSelection(mc, mouseX, mouseY, partialTicks,
					(int) Math.round(this.select * 100.0f), 100, -100);
			if (mul != 0) {
				this.selectcon.accept(this.select);
				this.select = mul * 0.01f;
			}
		}

		@Override
		public boolean keyTyped(char typedChar, int keyCode) {
			boolean flag = super.keyTyped(typedChar, keyCode);
			if (flag) {
				try {
					final String possibleValue = this.textfield.getText();
					this.select = Double.parseDouble(possibleValue);
					this.selectcon.accept(this.select);
				} catch (Exception e) {
				}
			}
			return flag;
		}

	}

}