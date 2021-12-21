package eu.gir.girsignals.guis.guilib;

import eu.gir.girsignals.guis.guilib.UIEntity.MouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class UIButton extends UIComponent {

	public static final int DEFAULT_COLOR = 14737632;
	public static final int DEFAULT_DISABLED_COLOR = 10526880;
	public static final int DEFAULT_HOVER_COLOR = 16777120;

	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

	private String text;
	private boolean enabled;

	public UIButton(final String text) {
		this.setVisible(true);
		this.setEnabled(true);
		this.text = text;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		if (this.visible) {
			final Minecraft mc = Minecraft.getMinecraft();
			final FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			final int offsetV = enabled ? (parent.isHovered() ? 2 : 1) : 0;
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GuiUtils.drawTexturedModalRect(0, 0, 0, 46 + offsetV * 20, parent.width / 2, parent.height, 0);
			GuiUtils.drawTexturedModalRect(parent.width / 2, 0, 200 - parent.width / 2, 46 + offsetV * 20,
					parent.width / 2, parent.height, 0);
			final int colorUsed = enabled ? (parent.isHovered() ? DEFAULT_HOVER_COLOR : DEFAULT_COLOR)
					: DEFAULT_DISABLED_COLOR;
			DrawUtil.drawCenteredString(fontrenderer, this.text, parent.width / 2, (parent.height - 8) / 2, colorUsed);
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void mouseEvent(MouseEvent event) {
	}

	@Override
	public void onAdd(UIEntity entity) {
		super.onAdd(entity);
		update();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

}