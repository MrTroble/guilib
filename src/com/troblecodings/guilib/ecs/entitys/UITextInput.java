package com.troblecodings.guilib.ecs.entitys;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.troblecodings.guilib.ecs.entitys.UIEntity.EnumMouseState;
import com.troblecodings.guilib.ecs.entitys.UIEntity.KeyEvent;
import com.troblecodings.guilib.ecs.entitys.UIEntity.MouseEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UITextInput extends UIComponent {

    private final TextFieldWidget textInput;
    private Consumer<String> onTextUpdate;
    private Predicate<String> validator = string -> true;

    public UITextInput(final String id) {
        final Minecraft mc = Minecraft.getInstance();
        this.textInput = new TextFieldWidget(mc.font, 0, 0, 0, 0, new TranslationTextComponent(id));
        this.textInput.setCanLoseFocus(false);
        this.textInput.setFocus(false);
        this.textInput.setVisible(true);
        this.textInput.setEditable(true);
        this.textInput.setMaxLength(60);
        this.textInput.setValue(id);
    }

    @Override
    public void draw(final DrawInfo info) {
        textInput.render(info.stack, info.mouseX, info.mouseY, info.tick);
    }

    @Override
    public void update() {
        textInput.setWidth((int) this.parent.getWidth());
        textInput.setHeight((int) this.parent.getHeight());
        textInput.setMessage(textInput.getMessage());
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
        if (event.character == 0)
            this.textInput.keyPressed(event.typedChar, event.keyCode, event.time);
        if (event.character != 0)
            this.textInput.charTyped(event.character, event.typedChar);
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
        return this.textInput.getValue();
    }

    public void setText(final String text) {
        this.textInput.setValue(text);

    }
}
