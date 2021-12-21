package eu.gir.girsignals.guis.guilib.entitys;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import eu.gir.girsignals.guis.guilib.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;

public class UIToolTip extends UIComponent {

	public final String descripton;

	public UIToolTip(final String descripton) {
		this.descripton = descripton;
	}

	@Override
	public void postDraw(final int mouseX, final int mouseY) {
		if (this.parent.isHovered()) {
			final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			final String desc = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? this.descripton
					: I18n.format("gui.keyprompt");
			final GuiBase base = parent.getBase();
			GuiUtils.drawHoveringText(Arrays.asList(desc.split(System.lineSeparator())), mouseX, mouseY, base.width, base.height,
					-1, font);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

	@Override
	public void update() {
	}
}
