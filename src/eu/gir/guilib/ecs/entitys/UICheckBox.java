package eu.gir.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import eu.gir.guilib.ecs.DrawUtil;
import eu.gir.guilib.ecs.entitys.render.UIButton;
import eu.gir.guilib.ecs.interfaces.UIAutoSync;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UICheckBox extends UIComponent implements UIAutoSync {
	
	public static final int BOX_WIDTH = 11;
	
	private String id;
	private String text;
	private IntConsumer onChange;
	private boolean enabled;
	private boolean checked;
	
	public UICheckBox(final String id) {
		this.id = id;
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	@Override
	public void update() {
		Minecraft mc = Minecraft.getMinecraft();
		this.parent.setWidth(BOX_WIDTH + 4 + mc.fontRenderer.getStringWidth(text));
		this.parent.setHeight(Math.max(BOX_WIDTH, mc.fontRenderer.FONT_HEIGHT) + 2);
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiUtils.drawContinuousTexturedBox(UIButton.BUTTON_TEXTURES, 0, 0, 0, 46, BOX_WIDTH, BOX_WIDTH, 200, 20, 2, 3, 2, 2, 0);
		int color = this.enabled ? UIButton.DEFAULT_COLOR : UIButton.DEFAULT_DISABLED_COLOR;
		if (this.isChecked())
			DrawUtil.drawCenteredString(mc.fontRenderer, "x", BOX_WIDTH / 2 + 1, 1, 14737632);
		mc.fontRenderer.drawStringWithShadow(text, BOX_WIDTH + 2.0f, 2.0f, color);
	}
	
	@Override
	public void write(NBTTagCompound compound) {
		compound.setBoolean(id, isChecked());
	}
	
	@Override
	public void read(NBTTagCompound compound) {
		this.setChecked(compound.getBoolean(id));
	}
	
	public IntConsumer getOnChange() {
		return onChange;
	}
	
	public void setOnChange(IntConsumer onChange) {
		this.onChange = onChange;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public String getID() {
		return this.id;
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
}
