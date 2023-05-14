package com.troblecodings.guilib.ecs;

import java.util.Stack;

import org.lwjgl.opengl.GL11;

import com.troblecodings.guilib.ecs.entitys.BufferWrapper;
import com.troblecodings.guilib.ecs.entitys.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GuiBase extends ContainerScreen<ContainerBase> {

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
    protected Minecraft mc;

    private int lastButton = -1;

    public GuiBase(final GuiInfo info) {
        super(info.base, info.inventory, info.component);
        this.entityStack.add(new UIEntity());
        this.entity = entityStack.lastElement();
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void resize(final Minecraft mc, final int width, final int height) {
        this.mc = mc;
        this.width = width;
        this.height = height;
        this.init();
    }

    @Override
    public boolean isPauseScreen() {
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
    public void render(final int mx, final int my, final float tick) {
        this.renderBackground();
        final DrawInfo info = new DrawInfo(mx, my, tick);
        drawBack(info, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);
        this.entityStack.forEach(entity -> entity.draw(info));
        this.entityStack.lastElement().postDraw(info);
    }

    public void updateFromContainer() {

    }

    private void updateSingle(final UIEntity entity) {
        entity.setWidth(GUI_MIN_WIDTH);
        entity.setHeight(this.ySize - GUI_INSET);
        entity.setX(this.guiLeft + GUI_INSET);
        entity.setY(this.guiTop + GUI_INSET);
        entity.updateEvent(
                new UpdateEvent(width, height, this.minecraft.window.getGuiScale(), 1, this));
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
    protected void init() {
        this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET * 4);
        this.xSize = GUI_MIN_WIDTH + GUI_INSET;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.entityStack.forEach(this::updateSingle);
    }

    @Override
    public boolean keyPressed(final int typedChar, final int keyCode, final int time) {
        if (keyCode == 1) {
            this.mc.player.closeContainer();
        }
        this.entityStack.lastElement().keyEvent(new KeyEvent(typedChar, keyCode, time));
        return super.keyPressed(typedChar, keyCode, time);
    }

    @Override
    public boolean charTyped(final char character, final int typedChar) {
        this.entityStack.lastElement().keyEvent(new KeyEvent(typedChar, 0, 0, character));
        return super.charTyped(character, typedChar);
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int mouseButton) {
        lastButton = mouseButton;
        this.entityStack.lastElement()
                .mouseEvent(new MouseEvent(mouseX, mouseY, mouseButton, EnumMouseState.CLICKED));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        this.entityStack.lastElement()
                .mouseEvent(new MouseEvent(mouseX, mouseY, lastButton, EnumMouseState.RELEASE));
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY,
            final int clickedMouseButton, final double p_94702_, final double p_94703_) {
        this.entityStack.lastElement().mouseEvent(
                new MouseEvent(mouseX, mouseY, clickedMouseButton, EnumMouseState.CLICKED));
        return super.mouseDragged(mouseX, mouseY, clickedMouseButton, p_94702_, p_94703_);
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double scroll) {
        if (scroll != 0) {
            this.entityStack.lastElement()
                    .mouseEvent(new MouseEvent(scroll, scroll, -1, EnumMouseState.SCROLL));
        }
        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    protected void renderBg(final float deltaTime, final int mouseX, final int mouseY) {
    }

    @Override
    public IGuiEventListener getFocused() {
        final IGuiEventListener eventListener = new IGuiEventListener() {
            @Override
            public boolean keyPressed(final int typedChar, final int keyCode, final int time) {
                return true;
            }
        };
        return eventListener;
    }

}
