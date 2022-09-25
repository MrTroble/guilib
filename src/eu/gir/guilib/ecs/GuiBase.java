package eu.gir.guilib.ecs;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import eu.gir.guilib.ecs.entitys.UIEntity;
import eu.gir.guilib.ecs.entitys.UIEntity.EnumMouseState;
import eu.gir.guilib.ecs.entitys.UIEntity.KeyEvent;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import eu.gir.guilib.ecs.entitys.UIEntity.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class GuiBase extends GuiScreen {

    private static final int GUI_MIN_WIDTH = 350;
    private static final int GUI_MAX_HEIGHT = 300;
    private static final int GUI_INSET = 4;

    private static final ResourceLocation CREATIVE_TAB = new ResourceLocation(
            "textures/gui/container/creative_inventory/tab_inventory.png");

    protected int guiLeft;
    protected int guiTop;
    protected int xSize = 340;
    protected int ySize = 230;
    protected UIEntity entity;
    protected NBTTagCompound compound;

    private int lastButton = -1;

    public GuiBase() {
        this.entity = new UIEntity();
        this.compound = new NBTTagCompound();
    }

    @Override
    public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRenderer = mc.fontRenderer;
        this.width = width;
        this.height = height;
        if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Pre(this,
                        this.buttonList))) {
            this.initGui();
        }
        net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(new net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post(this,
                        this.buttonList));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        drawDefaultBackground();
        mc.getTextureManager().bindTexture(CREATIVE_TAB);
        DrawUtil.drawBack(this, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);
        this.entity.draw(mouseX, mouseY);
        this.draw(mouseX, mouseY, partialTicks);
        this.entity.postDraw(mouseX, mouseY);
    }

    public void draw(final int mouseX, final int mouseY, final float partialTicks) {

    }

    @Override
    public void initGui() {
        this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET * 4);
        this.xSize = GUI_MIN_WIDTH + GUI_INSET;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.entity.setWidth(GUI_MIN_WIDTH);
        this.entity.setHeight(this.ySize - GUI_INSET);
        this.entity.setX(this.guiLeft + GUI_INSET);
        this.entity.setY(this.guiTop + GUI_INSET);
        final ScaledResolution res = new ScaledResolution(mc);
        this.entity.updateEvent(new UpdateEvent(width, height, res.getScaleFactor(),
                Math.max(this.width / this.height, this.height / this.width)));
    }

    @Override
    public void onGuiClosed() {
        this.entity.write(this.compound);
    }

    public void preClose() {
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            preClose();
            this.mc.player.closeScreen();
        }
        this.entity.keyEvent(new KeyEvent(keyCode, typedChar));
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton)
            throws IOException {
        lastButton = mouseButton;
        this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, mouseButton, EnumMouseState.CLICKED));
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, lastButton, EnumMouseState.RELEASE));
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton,
            final long timeSinceLastClick) {
        this.entity.mouseEvent(
                new MouseEvent(mouseX, mouseY, clickedMouseButton, EnumMouseState.CLICKED));
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.entity.mouseEvent(new MouseEvent(scroll, scroll, -1, EnumMouseState.SCROLL));
        }
    }
}
