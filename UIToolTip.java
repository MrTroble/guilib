package eu.gir.girsignals.guis.guilib;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import eu.gir.girsignals.guis.guilib.UIEntity.UpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;

public class UIToolTip extends UIComponent {

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

