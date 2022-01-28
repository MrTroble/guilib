package eu.gir.girsignals.guis.guilib.entitys;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class UITexture extends UIComponent {

	private ResourceLocation texture;
	
	public UITexture(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		final Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(this.texture);
		GuiUtils.drawTexturedModalRect(0, 0, 0, 0, this.parent.getWidth(), this.parent.getHeight(), 0);
	}

	@Override
	public void update() {}
	
}
