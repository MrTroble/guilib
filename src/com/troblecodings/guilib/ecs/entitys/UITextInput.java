package com.troblecodings.guilib.ecs.entitys;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;
import com.troblecodings.guilib.ecs.interfaces.UIAutoSync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UITextInput extends UIComponent implements UIAutoSync {

    private final EditBox textInput;
    private String id;
    private Consumer<String> onTextUpdate;
    private Predicate<String> validator = string -> true;

    public UITextInput(final String id) {
        this.id = id;
        Minecraft mc = Minecraft.getInstance();
        this.textInput = new EditBox(mc.font, 0, 0, 0, 0, new TranslatableComponent(id));
        this.textInput.setCanLoseFocus(false);
        this.textInput.setFocus(false);
        this.textInput.setVisible(true);
        this.textInput.setEditable(true);
        this.textInput.setMaxLength(60);
    }

    @Override
    public void draw(final DrawInfo info) {
        textInput.render(info.stack, info.mouseX, info.mouseY, info.tick);
    }

    @Override
    public void update() {
        textInput.setWidth(this.parent.getWidth());
        textInput.setWidth(this.parent.getHeight());
        textInput.setMessage(textInput.getMessage());
    }

    @Override
    public void write(final CompoundTag compound) {
        compound.putString(id, this.getText());
    }

    @Override
    public void read(final CompoundTag compound) {
        this.setText(compound.getString(id));
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        this.textInput.setVisible(visible);
        this.textInput.setFocus(visible);
        this.textInput.setEditable(visible);
    }

    @Override
    public void keyEvent(final KeyEvent event) {
        this.textInput.charTyped(event.typed, event.keyCode);
    }

    @Override
    public void mouseEvent(final MouseEvent event) {
        if (event.state.equals(EnumMouseState.CLICKED)) {
            if (this.parent.isHovered()) {
                this.textInput.setFocus(true);
                this.textInput.mouseClicked(event.x, event.y, event.key);
            } else {
                this.textInput.setFocus(false);
            }
        }
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
        this.textInput.setFilter(validator);
    }

    public String getText() {
        return this.textInput.getMessage().getContents();
    }

    public void setText(final String text) {
        this.textInput.setMessage(new TextComponent(text));;
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
