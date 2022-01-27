package eu.gir.girsignals.guis.guilib.entitys;

import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.gir.girsignals.guis.guilib.UIAutoSync;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.EnumMouseState;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.KeyEvent;
import eu.gir.girsignals.guis.guilib.entitys.UIEntity.MouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UITextInput extends UIComponent implements UIAutoSync {

	private final GuiTextField textInput;
	private final String id;
	private Consumer<String> onTextUpdate;
	private Predicate<String> validator;
	
	public UITextInput(String id) {
		this.id = id;
		this.textInput = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 0, 0);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		textInput.drawTextBox();
	}

	@Override
	public void update() {
		textInput.width = this.parent.getWidth();
		textInput.height = this.parent.getHeight();
	}

	@Override
	public void write(NBTTagCompound compound) {
		compound.setString(id, this.textInput.getText());
	}

	@Override
	public void read(NBTTagCompound compound) {
		if(compound.hasKey(id))
			this.textInput.setText(compound.getString(id));
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.textInput.setVisible(visible);
	}
	
	@Override
	public void keyEvent(KeyEvent event) {
		this.textInput.textboxKeyTyped(event.typed, event.keyCode);
	}
	
	@Override
	public void mouseEvent(MouseEvent event) {
		if(event.state.equals(EnumMouseState.CLICKED) && this.parent.isHovered())
			this.textInput.mouseClicked(event.x, event.y, event.key);
	}
	
	public Consumer<String> getOnTextUpdate() {
		return onTextUpdate;
	}

	public void setOnTextUpdate(Consumer<String> onTextUpdate) {
		this.onTextUpdate = onTextUpdate;
	}

	public Predicate<String> getValidator() {
		return validator;
	}

	public void setValidator(Predicate<String> validator) {
		this.validator = validator;
		this.textInput.setValidator(validator::test);
	}
}
