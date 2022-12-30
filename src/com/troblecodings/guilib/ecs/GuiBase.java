package com.troblecodings.guilib.ecs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.troblecodings.guilib.ecs.entitys.UIComponent.DrawInfo;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.UpdateEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)

public class GuiBase extends Screen {

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
    protected CompoundTag compound;
    protected Minecraft mc;
    protected AbstractTexture creativeTabTexture;

    private int lastButton = -1;

    public GuiBase() {
    	super(new TranslatableComponent("gui_base"));
        this.entity = new UIEntity();
        this.compound = new CompoundTag();
        this.creativeTabTexture = mc.getTextureManager().getTexture(CREATIVE_TAB);
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
    
    private void drawBack(final DrawInfo info, final int xLeft, final int xRight,
            final int yTop, final int yBottom) {
        this.creativeTabTexture.bind();
        
        // TODO Rewrite back renderer without standard functions
    }
    
    @Override
    public void render(PoseStack stack, int mx, int my, float tick) {
        this.renderBackground(stack);
        DrawInfo info = new DrawInfo(mx, my, stack, tick);
        drawBack(info, guiLeft, guiLeft + xSize, guiTop, guiTop + ySize);
        this.entity.draw(info);
        this.draw(info);
        this.entity.postDraw(info);
    }

    public void draw(final DrawInfo info) {

    }

    @Override
    protected void init() {
        this.ySize = Math.min(GUI_MAX_HEIGHT, this.height - GUI_INSET * 4);
        this.xSize = GUI_MIN_WIDTH + GUI_INSET;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.entity.setWidth(GUI_MIN_WIDTH);
        this.entity.setHeight(this.ySize - GUI_INSET);
        this.entity.setX(this.guiLeft + GUI_INSET);
        this.entity.setY(this.guiTop + GUI_INSET);
        this.entity.updateEvent(new UpdateEvent(width, height, mc.options.guiScale,
                Math.max(this.width / this.height, this.height / this.width)));
    }

    @Override
    public void onClose() {
        this.entity.write(this.compound);
    }
    
    @Override
    public boolean keyPressed(int typedChar, int keyCode, int test) {
        if (keyCode == 1) {
            this.mc.player.closeContainer();
        }
        this.entity.keyEvent(new KeyEvent(keyCode, (char) typedChar));
        return super.keyPressed(typedChar, keyCode, test);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        lastButton = mouseButton;
        this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, mouseButton, EnumMouseState.CLICKED));
    	return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.entity.mouseEvent(new MouseEvent(mouseX, mouseY, lastButton, EnumMouseState.RELEASE));
    	return super.mouseReleased(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int clickedMouseButton, double p_94702_, double p_94703_) {
        this.entity.mouseEvent(
                new MouseEvent(mouseX, mouseY, clickedMouseButton, EnumMouseState.CLICKED));
    	return super.mouseDragged(mouseX, mouseY, clickedMouseButton, p_94702_, p_94703_);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (scroll != 0) {
            this.entity.mouseEvent(new MouseEvent(scroll, scroll, -1, EnumMouseState.SCROLL));
        }
    	return super.mouseScrolled(mouseX, mouseY, scroll);
    }
    
}
