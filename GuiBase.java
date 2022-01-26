package eu.gir.girsignals.guis.guilib;

import java.io.IOException;

import eu.gir.girsignals.guis.guilib.entitys.UIEntity;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.KeyEvent;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class GuiBase extends GuiScreen {
	
	private static final int STRING_COLOR = 4210752;
	private static final int TOP_STRING_OFFSET = 15;
	private static final float STRING_SCALE = 1.5f;
	private static final int LEFT_OFFSET = 20;
	private static final int GUI_MIN_WIDTH = 150;
	private static final int GUI_MAX_HEIGHT = 320;
	private static final int RIGHT_INSET = 20;
	private static final int GUI_INSET = 40;
	private static final int SIGNAL_RENDER_WIDTH_AND_INSET = 180;
	private static final int TOP_OFFSET = GUI_INSET;
	
	private static final ResourceLocation CREATIVE_TAB = new ResourceLocation("textures/gui/container/creative_inventory/tab_inventory.png");
	
	protected int guiLeft;
	protected int guiTop;
	protected int xSize = 340;
	protected int ySize = 230;
	protected UIEntity entity;
	protected NBTTagCompound compound;
	private final String name;
	
	public GuiBase(final String name) {
		this.name = name;
		this.entity = new UIEntity();
		this.compound = new NBTTagCompound();
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRenderer = mc.fontRenderer;
		this.width = width;
		this.height = height;
		if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			this.initGui();
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public String getTitle() {
		return this.name;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		mc.getTextureManager().bindTexture(CREATIVE_TAB);
		
		DrawUtil.drawBack(this, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);
		
		entity.draw(mouseX, mouseY);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft + LEFT_OFFSET, this.guiTop + TOP_STRING_OFFSET, 0);
		GlStateManager.scale(STRING_SCALE, STRING_SCALE, STRING_SCALE);
		this.fontRenderer.drawString(this.getTitle(), 0, 0, STRING_COLOR);
		GlStateManager.popMatrix();
		
		this.draw(mouseX, mouseY, partialTicks);
		
		entity.postDraw(mouseX, mouseY);
	}
	
	public void draw(int mouseX, int mouseY, float partialTicks) {
		
	}
	
	@Override
	public void initGui() {
		this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET);
		this.xSize = GUI_MIN_WIDTH + 20 + SIGNAL_RENDER_WIDTH_AND_INSET + RIGHT_INSET;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.entity.setWidth(this.xSize - RIGHT_INSET * 2);
		this.entity.setHeight(this.ySize - GUI_INSET);
		this.entity.setX(this.guiLeft + RIGHT_INSET);
		this.entity.setY(this.guiTop + TOP_OFFSET);
		final ScaledResolution res = new ScaledResolution(mc);
		this.entity.updateEvent(new UpdateEvent(width, height, res.getScaleFactor(), Math.max(this.width / this.height, this.height / this.width)));
	}
	
	@Override
	public void onGuiClosed() {
		this.entity.write(this.compound);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		this.entity.keyEvent(new KeyEvent(keyCode));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, mouseButton, EnumMouseState.CLICKED));
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, 0, EnumMouseState.RELEASE));
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, clickedMouseButton, EnumMouseState.CLICKED));
	}
}
