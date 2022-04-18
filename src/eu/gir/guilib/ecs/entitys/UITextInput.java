package eu.gir.guilib.ecs.entitys;

import java.util.function.Consumer;
import java.util.function.Predicate;

import eu.gir.guilib.ecs.entitys.UIEntity.EnumMouseState;
import eu.gir.guilib.ecs.entitys.UIEntity.KeyEvent;
import eu.gir.guilib.ecs.entitys.UIEntity.MouseEvent;
import eu.gir.guilib.ecs.interfaces.UIAutoSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UITextInput extends UIComponent implements UIAutoSync {

    private final GuiTextField textInput;
    private String id;
    private Consumer<String> onTextUpdate;
    private Predicate<String> validator = string -> true;

    public UITextInput(final String id) {
        this.id = id;
        this.textInput = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 0, 0);
        this.textInput.setCanLoseFocus(false);
        this.textInput.setFocused(true);
        this.textInput.setEnabled(true);
        this.textInput.setMaxStringLength(60);
    }

    @Override
    public void draw(final int mouseX, final int mouseY) {
        textInput.drawTextBox();
    }

    @Override
    public void update() {
        textInput.width = this.parent.getWidth();
        textInput.height = this.parent.getHeight();
        textInput.setText(textInput.getText());
    }

    @Override
    public void write(final NBTTagCompound compound) {
        compound.setString(id, this.getText());
    }

    @Override
    public void read(final NBTTagCompound compound) {
        this.setText(compound.getString(id));
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        this.textInput.setVisible(visible);
        this.textInput.setFocused(visible);
        this.textInput.setEnabled(visible);
    }

    @Override
    public void keyEvent(final KeyEvent event) {
        this.textInput.textboxKeyTyped(event.typed, event.keyCode);
    }

    @Override
    public void mouseEvent(final MouseEvent event) {
        if (event.state.equals(EnumMouseState.CLICKED) && this.parent.isHovered())
            this.textInput.mouseClicked(event.x, event.y, event.key);
    }

    public Consumer<String> getOnTextUpdate() {
        return onTextUpdate;
    }

    public void setOnTextUpdate(final Consumer<String> onTextUpdate) {
        this.onTextUpdate = onTextUpdate;
    }

    public Predicate<String> getValidator() {
        return validator;
    }

    public void setValidator(final Predicate<String> validator) {
        this.validator = validator;
        this.textInput.setValidator(validator::test);
    }

    public String getText() {
        return this.textInput.getText();
    }

    public void setText(final String text) {
        this.textInput.setText(text);
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setID(final String id) {
        this.id = id;
    }
}
