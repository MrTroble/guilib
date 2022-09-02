package eu.gir.guilib.ecs;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import eu.gir.girsignals.tileentitys.SignalTileEnity;
import eu.gir.guilib.ecs.entitys.UIBox;
import eu.gir.guilib.ecs.entitys.UICheckBox;
import eu.gir.guilib.ecs.entitys.UIEntity;
import eu.gir.guilib.ecs.entitys.UIEnumerable;
import eu.gir.guilib.ecs.entitys.UITextInput;
import eu.gir.guilib.ecs.entitys.input.UIClickable;
import eu.gir.guilib.ecs.entitys.input.UIOnUpdate;
import eu.gir.guilib.ecs.entitys.render.UIButton;
import eu.gir.guilib.ecs.entitys.render.UIColor;
import eu.gir.guilib.ecs.entitys.render.UILabel;
import eu.gir.guilib.ecs.entitys.render.UIToolTip;
import eu.gir.guilib.ecs.interfaces.IIntegerable;
import eu.gir.guilib.ecs.interfaces.UIPagable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class GuiElements {

    private GuiElements() {
    }

    public static final Consumer<UIEntity> NULL_CONSUMER = e -> {
    };

    public static UIEntity createSpacerH(final int space) {
        final UIEntity spacer = new UIEntity();
        spacer.setInheritHeight(true);
        spacer.setWidth(space);
        return spacer;
    }

    public static UIEntity createSpacerV(final int space) {
        final UIEntity spacer = new UIEntity();
        spacer.setInheritWidth(true);
        spacer.setHeight(space);
        return spacer;
    }

    public static UIEntity createInputElement(final IIntegerable<?> property,
            final IntConsumer consumer) {
        final UIEntity middle = new UIEntity();
        middle.setHeight(20);
        middle.setInheritWidth(true);

        final UITextInput textInput = new UITextInput(SignalTileEnity.CUSTOMNAME);
        textInput.setOnTextUpdate(str -> consumer.accept(str.length()));

        middle.add(textInput);
        middle.add(new UIToolTip(property.getDescription()));
        return middle;
    }

    public static UIEntity createBoolElement(final IIntegerable<?> property,
            final IntConsumer consumer) {
        final UIEntity middle = new UIEntity();
        middle.setHeight(20);
        middle.setInheritWidth(true);

        final SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();

        final UICheckBox middleButton = new UICheckBox(property.getName());
        final UIClickable clickable = new UIClickable(e -> {
            middleButton.setChecked(!middleButton.isChecked());
            consumer.accept(middleButton.isChecked() ? 1 : 0);
            handler.playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        });
        middleButton.setOnChange(consumer);
        middleButton.setText(property.getLocalizedName());

        middle.add(middleButton);
        middle.add(clickable);
        middle.add(new UIToolTip(property.getDescription()));
        return middle;
    }

    public static UIEntity createEnumElement(final IIntegerable<?> property,
            final IntConsumer consumer) {
        return createEnumElement(new UIEnumerable(property.count(), property.getName()), property,
                consumer);
    }

    public static UIEntity createEnumElement(final UIEnumerable enumerable,
            final IIntegerable<?> property, final IntConsumer consumer) {
        return createEnumElement(enumerable, property, consumer,
                property.getMaxWidth(Minecraft.getMinecraft().fontRenderer) + 8);
    }

    public static UIEntity createEnumElement(final UIEnumerable enumerable,
            final IIntegerable<?> property, final IntConsumer consumer, final int minWidth) {
        final UIEntity middle = new UIEntity();
        middle.setInheritWidth(true);
        middle.setInheritHeight(true);

        final UIButton leftButton = new UIButton("<");
        final UIButton rightButton = new UIButton(">");

        final SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();

        final UIButton middleButton = new UIButton("");
        final IntConsumer acceptOr = in -> {
            if (in >= property.count())
                return;
            middleButton.setText(property.getNamedObj(in));
            rightButton.setEnabled(true);
            leftButton.setEnabled(true);
            if (in <= enumerable.getMin())
                leftButton.setEnabled(false);
            if (in >= enumerable.getMax() - 1)
                rightButton.setEnabled(false);
        };
        enumerable.setOnChange(consumer.andThen(acceptOr));
        middle.add(new UIOnUpdate(() -> acceptOr.accept(enumerable.getIndex())));
        middle.add(middleButton);
        middle.add(enumerable);

        final UIEntity left = new UIEntity();
        left.setWidth(20);
        left.setHeight(20);

        final UIClickable leftclickable = new UIClickable(e -> {
            final int id = enumerable.getIndex();
            final int min = enumerable.getMin();
            if (id <= min)
                return;
            enumerable.setIndex(id - 1);
            handler.playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        });
        left.add(leftButton);
        left.add(leftclickable);

        final UIEntity right = new UIEntity();
        right.setWidth(20);
        right.setHeight(20);

        final UIClickable rightclickable = new UIClickable(e -> {
            final int id = enumerable.getIndex();
            final int max = enumerable.getMax() - 1;
            if (id >= max)
                return;
            enumerable.setIndex(id + 1);
            handler.playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        });
        right.add(rightButton);
        right.add(rightclickable);

        acceptOr.accept(0);

        final UIEntity hbox = new UIEntity();
        hbox.add(new UIBox(UIBox.HBOX, 1));
        final String desc = property.getDescription();
        if (desc != null)
            hbox.add(new UIToolTip(desc));
        hbox.add(left);
        hbox.add(middle);
        hbox.add(right);
        hbox.setInheritWidth(true);
        hbox.setHeight(20);
        return hbox;
    }

    public static UIEntity createPageSelect(final UIPagable vbox) {
        final UIEntity hbox = new UIEntity();

        final UIEntity middle = new UIEntity();
        middle.setInheritWidth(true);
        middle.setInheritHeight(true);

        final UIButton leftButton = new UIButton("<");
        final UIButton rightButton = new UIButton(">");

        final UILabel middleButton = new UILabel("DDDD");
        middle.add(middleButton);

        final SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();

        final UIEnumerable enumerable = new UIEnumerable(0, "pageselect");
        final IntConsumer acceptOn = in -> {
            middleButton.setText("Page: " + (in + 1) + "/" + vbox.getMaxPages());
            rightButton.setEnabled(true);
            leftButton.setEnabled(true);
            if (in <= enumerable.getMin())
                leftButton.setEnabled(false);
            if (in >= enumerable.getMax() - 1)
                rightButton.setEnabled(false);
            vbox.setPage(in);
        };
        enumerable.setOnChange(acceptOn);

        vbox.getParent().add(new UIOnUpdate(() -> {
            final int max = vbox.getMaxPages();
            if (max < 1) {
                return;
            }
            hbox.setVisible(max != 1);
            enumerable.setMax(max);
            final int current = enumerable.getIndex();
            enumerable.setIndex(current >= max ? max - 1 : current);
            acceptOn.accept(enumerable.getIndex());
        }));
        middle.add(enumerable);

        final UIEntity left = new UIEntity();
        left.setWidth(20);
        left.setHeight(20);

        final UIClickable leftclickable = new UIClickable(e -> {
            final int id = enumerable.getIndex();
            final int min = enumerable.getMin();
            if (id <= min)
                return;
            enumerable.setIndex(id - 1);
            handler.playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        });
        left.add(leftButton);
        left.add(leftclickable);

        final UIEntity right = new UIEntity();
        right.setWidth(20);
        right.setHeight(20);

        final UIClickable rightclickable = new UIClickable(e -> {
            final int id = enumerable.getIndex();
            final int max = enumerable.getMax() - 1;
            if (id >= max)
                return;
            enumerable.setIndex(id + 1);
            handler.playSound(
                    PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        });
        right.add(rightButton);
        right.add(rightclickable);

        hbox.add(new UIBox(UIBox.HBOX, 1));
        hbox.add(left);
        hbox.add(middle);
        hbox.add(right);
        hbox.setInheritWidth(true);
        hbox.setHeight(20);
        return hbox;
    }

    public static UIEntity createButton(final String name) {
        return createButton(name, NULL_CONSUMER);
    }

    public static UIEntity createButton(final String name, final Consumer<UIEntity> consumer) {
        final UIEntity entity = createButton(name, 0, consumer);
        entity.setInheritWidth(true);
        return entity;
    }

    public static UIEntity createButton(final String name, final int width) {
        return createButton(name, width, NULL_CONSUMER);
    }

    public static UIEntity createButton(final String name, final int width,
            final Consumer<UIEntity> consumer) {
        final SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
        final UIEntity settingsEnt = new UIEntity();
        settingsEnt.setHeight(20);
        settingsEnt.setWidth(width);
        settingsEnt.add(new UIButton(name));
        settingsEnt.add(new UIClickable(consumer.andThen(e -> handler.playSound(
                PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F)))));
        return settingsEnt;
    }

    public static UIEntity createLabel(final String name, final int color, final int background) {
        final UIEntity entity = new UIEntity();
        entity.add(new UIColor(background));
        final UILabel label = new UILabel(name);
        entity.setHeight(15);
        entity.setInheritWidth(true);
        label.setTextColor(color);
        entity.add(label);
        return entity;
    }

    public static UIEntity createLabel(final String name, final int color) {
        final UIEntity entity = new UIEntity();
        final UILabel label = new UILabel(name);
        entity.setHeight(15);
        entity.setInheritWidth(true);
        label.setTextColor(color);
        entity.add(label);
        return entity;
    }

    public static UIEntity createLabel(final String name) {
        return createLabel(name, UILabel.DEFAULT_STRING_COLOR);
    }
}