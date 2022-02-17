package eu.gir.guilib.ecs.entitys;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import eu.gir.guilib.ecs.entitys.UIEntity.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIToolTip extends UIComponent {
	
	private String descripton;
	
	public UIToolTip(final String descripton) {
		this.descripton = descripton;
	}
	
	public String getDescripton() {
		return descripton;
	}
	
	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}
	
	@Override
	public void postDraw(final int mouseX, final int mouseY) {
		if (this.parent.isHovered()) {
			final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			final String desc = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || this.descripton.length() < 200) ? this.descripton : I18n.format("gui.keyprompt");
			final UpdateEvent base = parent.getLastUpdateEvent();
			if (base != null)
				GuiUtils.drawHoveringText(Arrays.asList(desc.split(System.lineSeparator())), mouseX, mouseY, base.width, base.height, -1, font);
		}
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
	}
	
	@Override
	public void update() {
	}
}
