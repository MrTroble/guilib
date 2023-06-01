package com.troblecodings.guilib.ecs;

import java.io.IOException;
import java.util.Stack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.entitys.BufferWrapper;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBase extends GuiScreen {

    private static final int GUI_MIN_WIDTH = 350;
    private static final int GUI_MAX_HEIGHT = 300;
    private static final int GUI_INSET = 4;

    public static final ResourceLocation CREATIVE_TAB = new ResourceLocation(
            "textures/gui/container/creative_inventory/tabs.png");

    protected int guiLeft;
    protected int guiTop;
    protected int xSize = 340;
    protected int ySize = 230;
    protected Stack<UIEntity> entityStack = new Stack<>();
    protected UIEntity entity;

    private int lastButton = -1;

    public GuiBase(final GuiInfo info) {
        info.base = getNewGuiContainer(info);
        this.entityStack.add(new UIEntity());
        this.entity = entityStack.lastElement();
        this.mc = Minecraft.getMinecraft();
    }

    public ContainerBase getNewGuiContainer(final GuiInfo info) {
        return null;
    }

    @Override
    public void setWorldAndResolution(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
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

    private void drawBack(final DrawInfo info, final int xLeft, final int xRight, final int yTop,
            final int yBottom) {
        info.applyTexture(CREATIVE_TAB);
        info.color();
        info.blendOn();
        info.depthOn();
        final BufferWrapper builder = info.builder(GL11.GL_QUADS,
                DefaultVertexFormats.POSITION_TEX);
        final int inset = 8;
        final int topOffset = 32;
        final int leftOffset = 20;
        final int bottomOffset = 120;
        builder.quadNonNormalized(xLeft, xLeft + inset, yTop, yTop + inset, 0, inset, topOffset,
                topOffset + inset);
        builder.quadNonNormalized(xLeft, xLeft + inset, yBottom - inset, yBottom, 0, inset,
                bottomOffset, bottomOffset + inset);
        builder.quadNonNormalized(xRight - inset, xRight, yTop, yTop + inset, leftOffset,
                leftOffset + inset, topOffset, topOffset + inset);
        builder.quadNonNormalized(xRight - inset, xRight, yBottom - inset, yBottom, leftOffset,
                leftOffset + inset, bottomOffset, bottomOffset + inset);

        builder.quadNonNormalized(xLeft, xLeft + inset, yTop + inset, yBottom - inset, 0, inset,
                topOffset + inset, topOffset + inset + 1);
        builder.quadNonNormalized(xRight - inset, xRight, yTop + inset, yBottom - inset, leftOffset,
                leftOffset + inset, topOffset + inset, topOffset + inset + 1);
        builder.quadNonNormalized(xLeft + inset, xRight - inset, yTop, yTop + inset, inset,
                inset + 1, topOffset, topOffset + inset);
        builder.quadNonNormalized(xLeft + inset, xRight - inset, yBottom - inset, yBottom, inset,
                inset + 1, bottomOffset, bottomOffset + inset);
        builder.quadNonNormalized(xLeft + inset, xRight - inset, yTop + inset, yBottom - inset,
                inset, inset + 1, topOffset + inset + 1, topOffset + inset + 2);
        info.end();
        info.depthOff();
        info.blendOff();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        drawDefaultBackground();
        final DrawInfo info = new DrawInfo(mouseX, mouseY, partialTicks);
        drawBack(info, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);
        this.entityStack.forEach(entity -> entity.draw(info));
        this.entityStack.lastElement().postDraw(info);

    }

    public void updateFromContainer() {
    }

    public void preClose() {
    }

    private void updateSingle(final UIEntity entity) {
        entity.setWidth(GUI_MIN_WIDTH);
        entity.setHeight(this.ySize - GUI_INSET);
        entity.setX(this.guiLeft + GUI_INSET);
        entity.setY(this.guiTop + GUI_INSET);
        final ScaledResolution rs = new ScaledResolution(mc);
        entity.updateEvent(new UpdateEvent(width, height, rs.getScaleFactor(), 1, this));
    }

    public void push(final UIEntity entity) {
        this.entityStack.lastElement().setHoveringEnabled(false);
        entity.setHoveringEnabled(true);
        this.entityStack.push(entity);
        updateSingle(entity);
    }

    public UIEntity pop() {
        final UIEntity old = this.entityStack.pop();
        this.entityStack.lastElement().setHoveringEnabled(true);
        return old;
    }

    @Override
    public void initGui() {
        this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET * 4);
        this.xSize = GUI_MIN_WIDTH + GUI_INSET;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.entityStack.forEach(this::updateSingle);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            preClose();
            this.mc.player.closeScreen();
        }
        this.entityStack.lastElement().keyEvent(new KeyEvent(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        lastButton = mouseButton;
        this.entityStack.lastElement()
                .mouseEvent(new MouseEvent(mouseX, mouseY, mouseButton, EnumMouseState.CLICKED));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.entityStack.lastElement()
                .mouseEvent(new MouseEvent(mouseX, mouseY, lastButton, EnumMouseState.RELEASE));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int scroll = Mouse.getDWheel();
        if (scroll != 0) {
            this.entityStack.lastElement()
                    .mouseEvent(new MouseEvent(scroll, scroll, -1, EnumMouseState.SCROLL));
        }
    }
}