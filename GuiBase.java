package eu.gir.girsignals.guis.guilib;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import eu.gir.girsignals.SEProperty;
import eu.gir.girsignals.SEProperty.ChangeableStage;
import eu.gir.girsignals.guis.guilib.GuiElements.GuiEnumerableSetting;
import eu.gir.girsignals.guis.guilib.GuiElements.GuiSettingCheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBase extends GuiScreen {
	
	private static final int TOP_STRING_OFFSET = 15;
	private static final float STRING_SCALE = 1.5f;
	private static final int STRING_COLOR = 4210752;
	private static final int LEFT_OFFSET = 20;
	private static final int GUI_MIN_WIDTH = 150;
	private static final int GUI_MAX_HEIGHT = 320;
	private static final int RIGHT_INSET = 20;
	private static final int GUI_INSET = 40;
	private static final int SIGNAL_RENDER_WIDTH_AND_INSET = 180;
	private static final int TOP_OFFSET = GUI_INSET;
	private static final int ELEMENT_SPACING = 10;
	private static final int BOTTOM_OFFSET = 30;

	private static final ResourceLocation CREATIVE_TAB = new ResourceLocation(
			"textures/gui/container/creative_inventory/tab_inventory.png");

	protected int guiLeft;
	protected int guiTop;
	protected int xSize = 340;
	protected int ySize = 230;
	private ArrayList<ArrayList<GuiEnumerableSetting>> pageList = new ArrayList<>();
	protected GuiElements.GuiPageSelect pageselect = new GuiElements.GuiPageSelect(pageList);

	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRenderer = mc.fontRenderer;
		this.width = width;
		this.height = height;
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			this.initGui();
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public String getTitle() {
		return "PLS EDIT";
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		mc.getTextureManager().bindTexture(CREATIVE_TAB);

		DrawUtil.drawBack(this, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);

		synchronized (buttonList) {
			super.drawScreen(mouseX, mouseY, partialTicks);

			GlStateManager.pushMatrix();
			GlStateManager.translate(this.guiLeft + LEFT_OFFSET, this.guiTop + TOP_STRING_OFFSET, 0);
			GlStateManager.scale(STRING_SCALE, STRING_SCALE, STRING_SCALE);
			this.fontRenderer.drawString(this.getTitle(), 0, 0, STRING_COLOR);
			GlStateManager.popMatrix();
			
			for (GuiButton guiButton : buttonList) {
				if (guiButton instanceof GuiEnumerableSetting) {
					if (((GuiEnumerableSetting) guiButton).drawHoverText(mouseX, mouseY, fontRenderer, this.xSize,
							this.ySize)) {
						break;
					}
				}
			}
		}
	}

	public void initButtons() {
		synchronized (buttonList) {
			this.buttonList.clear();
			this.buttonList.add(pageselect);
		}
	}

	@Override
	public void initGui() {
		if (buttonList.isEmpty())
			this.initButtons();
		int maxWidth = 0;
		for (GuiButton guiButton : buttonList) {
			if (guiButton instanceof GuiEnumerableSetting) {
				final GuiEnumerableSetting setting = (GuiEnumerableSetting) guiButton;
				final int width = setting.getMaxWidth(fontRenderer);
				if (maxWidth < width) {
					maxWidth = width;
				}
			}
		}
		maxWidth = Math.max(GUI_MIN_WIDTH, maxWidth) + 20;
		this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET);
		this.xSize = maxWidth + SIGNAL_RENDER_WIDTH_AND_INSET + RIGHT_INSET;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		int index = 0;
		boolean visible = pageselect.getValue() == index;
		pageList.clear();
		pageList.add(Lists.newArrayList());
		final int xPos = this.guiLeft + LEFT_OFFSET;
		int yPos = this.guiTop + TOP_OFFSET;
		for (GuiButton guiButton : buttonList) {
			if (guiButton instanceof GuiEnumerableSetting) {
				final GuiEnumerableSetting setting = (GuiEnumerableSetting) guiButton;
				setting.setWidth(maxWidth);
				if (setting.equals(this.pageselect))
					continue;
				if ((yPos + ELEMENT_SPACING + setting.height) >= (this.guiTop + this.ySize - BOTTOM_OFFSET)) {
					pageList.add(Lists.newArrayList());
					yPos = this.guiTop + TOP_OFFSET;
					index++;
					visible = pageselect.getValue() == index;
				}
				pageList.get(index).add(setting);
				setting.updatePos(xPos, yPos);
				setting.update();
				setting.setVisible(visible);
				yPos += ELEMENT_SPACING + setting.height;
			}
		}
		
		if (this.pageList.size() > 1) {
			this.pageselect.updatePos(this.guiLeft + maxWidth / 2, this.guiTop + this.ySize - BOTTOM_OFFSET);
			this.pageselect.update();
			this.pageselect.visible = true;
		} else {
			this.pageselect.visible = false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static Optional<GuiEnumerableSetting> of(SEProperty<?> property, int initialValue,
			Consumer<Integer> consumer, ChangeableStage stage) {
		if (property == null)
			return Optional.empty();
		if (ChangeableStage.GUISTAGE == stage) {
			if (property.isChangabelAtStage(ChangeableStage.GUISTAGE)) {
				if (property.getType().equals(Boolean.class))
					return Optional.of(new GuiSettingCheckBox(property, initialValue, consumer));
				return Optional.of(new GuiEnumerableSetting(property, initialValue, consumer));
			} else if (property.isChangabelAtStage(ChangeableStage.APISTAGE)) {
				return Optional.of(new GuiSettingCheckBox(property, initialValue, consumer));
			}
		} else if (ChangeableStage.APISTAGE == stage) {
			if (property.isChangabelAtStage(ChangeableStage.APISTAGE)
					|| property.isChangabelAtStage(ChangeableStage.APISTAGE_NONE_CONFIG)) {
				return Optional.of(new GuiEnumerableSetting(property, initialValue, consumer));
			}
		}
		return Optional.empty();
	}
}
