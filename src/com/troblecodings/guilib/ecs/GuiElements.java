package com.troblecodings.guilib.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import com.troblecodings.guilib.ecs.DrawUtil.DisableIntegerable;
import com.troblecodings.guilib.ecs.entitys.UIBox;
import com.troblecodings.guilib.ecs.entitys.UICheckBox;
import com.troblecodings.guilib.ecs.entitys.UIEntity;
import com.troblecodings.guilib.ecs.entitys.UIEnumerable;
import com.troblecodings.guilib.ecs.entitys.UIScrollBox;
import com.troblecodings.guilib.ecs.entitys.UITextInput;
import com.troblecodings.guilib.ecs.entitys.input.UIClickable;
import com.troblecodings.guilib.ecs.entitys.input.UIOnUpdate;
import com.troblecodings.guilib.ecs.entitys.input.UIScroll;
import com.troblecodings.guilib.ecs.entitys.input.UIScrollBar;
import com.troblecodings.guilib.ecs.entitys.render.UIBorder;
import com.troblecodings.guilib.ecs.entitys.render.UIButton;
import com.troblecodings.guilib.ecs.entitys.render.UIColor;
import com.troblecodings.guilib.ecs.entitys.render.UILabel;
import com.troblecodings.guilib.ecs.entitys.render.UIScissor;
import com.troblecodings.guilib.ecs.entitys.render.UITexture;
import com.troblecodings.guilib.ecs.entitys.render.UIToolTip;
import com.troblecodings.guilib.ecs.interfaces.IBoxMode;
import com.troblecodings.guilib.ecs.interfaces.IIntegerable;
import com.troblecodings.guilib.ecs.interfaces.UIPagable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

        final UITextInput textInput = new UITextInput(property.getLocalizedName());
        textInput.setOnTextUpdate(str -> consumer.accept(str.length()));

        middle.add(textInput);
        middle.add(new UIToolTip(property.getDescriptionForName()));
        return middle;
    }

    public static UIEntity createBoolElement(final IIntegerable<?> property,
            final IntConsumer consumer) {
        return createBoolElement(property, consumer, 0);
    }

    public static UIEntity createBoolElement(final IIntegerable<?> property,
            final IntConsumer consumer, final int defaultValue) {
        final UIEntity middle = new UIEntity();
        middle.setHeight(20);
        middle.setInheritWidth(true);

        final SoundManager handler = Minecraft.getInstance().getSoundManager();

        final UICheckBox middleButton = new UICheckBox(property.getName());
        middleButton.setChecked(defaultValue == 0 ? false : true);
        final UIClickable clickable = new UIClickable(e -> {
            middleButton.setChecked(!middleButton.isChecked());
            consumer.accept(middleButton.isChecked() ? 1 : 0);
            handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        });
        middleButton.setOnChange(consumer);
        middleButton.setText(property.getLocalizedName());

        middle.add(middleButton);
        middle.add(clickable);
        middle.add(new UIToolTip(property.getDescriptionForName()));
        return middle;
    }

    public static UIEntity createEnumElement(final IIntegerable<?> property,
            final IntConsumer consumer) {
        return createEnumElement(property, consumer, 0);
    }

    public static UIEntity createEnumElement(final IIntegerable<?> property,
            final IntConsumer consumer, final int value) {
        return createEnumElement(new UIEnumerable(property.count(), property.getName()), property,
                consumer, value);
    }

    public static UIEntity createEnumElement(final UIEnumerable enumerable,
            final IIntegerable<?> property, final IntConsumer consumer) {
        final Minecraft mc = Minecraft.getInstance();
        return createEnumElement(enumerable, property, consumer, property.getMaxWidth(mc.font) + 8,
                0);
    }

    public static UIEntity createEnumElement(final UIEnumerable enumerable,
            final IIntegerable<?> property, final IntConsumer consumer, final int value) {
        final Minecraft mc = Minecraft.getInstance();
        return createEnumElement(enumerable, property, consumer, property.getMaxWidth(mc.font) + 8,
                value);
    }

    public static UIEntity createScrollBar(final UIScrollBox scrollbox, final int insets,
            final UIScroll scroll) {
        final IBoxMode mode = scrollbox.getMode();
        final IBoxMode orthogonal = mode.getOrthogonal();
        final UIEntity entity = new UIEntity();
        final UIEntity button = new UIEntity();
        mode.inheritsBounds(entity, true);
        mode.inheritsBounds(button, true);
        button.setHeight(insets);
        button.setWidth(insets);
        orthogonal.setBounds(entity, insets);
        entity.add(button);

        button.add(new UITexture(GuiBase.CREATIVE_TAB, 232.0f / 256.0f, 0, 244.0f / 256.0f,
                15.0f / 256.0f));
        entity.add(new UIColor(0xFF3d3d3d));

        entity.add(new UIScrollBar(scrollbox, insets, input -> {
            final int position = (int) (Math.floor((mode.getBounds(entity) - insets) * input));
            mode.setPos(button, position);
            final int positionOfList = (int) (Math.floor(
                    (scrollbox.getWholeBounds() - mode.getBounds(scrollbox.getParent())) * input));
            mode.setPos(scrollbox.getParent(), -positionOfList);
            scrollbox.getParent().update();
        }, scroll));
        return entity;
    }

    public static UIEntity createSelectionScreen(final UIEnumerable enumerable,
            final IIntegerable<?> property) {
        return createScreen(searchPanel -> {
            final UIEntity searchBar = new UIEntity();
            searchBar.setInheritWidth(true);
            searchBar.setHeight(20);
            final UITextInput input = new UITextInput("");
            searchBar.add(input);
            searchPanel.add(searchBar);

            final UIEntity listWithScroll = new UIEntity();
            listWithScroll.setInheritHeight(true);
            listWithScroll.setInheritWidth(true);
            listWithScroll.add(new UIBox(UIBox.HBOX, 2));
            listWithScroll.add(new UIScissor());
            listWithScroll.add(new UIBorder(0xFF00FFFF));
            searchPanel.add(listWithScroll);

            final UIEntity list = new UIEntity();
            listWithScroll.add(list);
            list.setInheritHeight(true);
            list.setInheritWidth(true);

            final UIScrollBox scrollbox = new UIScrollBox(UIBox.VBOX, 2);
            list.add(scrollbox);
            final Map<String, UIEntity> nameToUIEntity = new HashMap<>();
            if (property instanceof DisableIntegerable<?>) {
                list.add(createButton(property.getNamedObj(-1), e -> {
                    enumerable.setIndex(-1);
                    e.getLastUpdateEvent().base.pop();
                }));
            }
            for (int i = 0; i < property.count(); i++) {
                final int index = i;
                final String name = property.getNamedObj(i);
                final UIEntity button = createButton(name, e -> {
                    enumerable.setIndex(index);
                    e.getLastUpdateEvent().base.pop();
                });
                nameToUIEntity.put(name.toLowerCase(), button);
                list.add(button);
            }
            final UIScroll scroll = new UIScroll();
            final UIEntity scrollBar = createScrollBar(scrollbox, 10, scroll);
            scrollbox.setConsumer(size -> {
                if (size > list.getHeight()) {
                    listWithScroll.add(scroll);
                    listWithScroll.add(scrollBar);
                } else {
                    listWithScroll.remove(scrollBar);
                    listWithScroll.remove(scroll);
                }
            });
            input.setOnTextUpdate(string -> {
                nameToUIEntity.forEach((name, entity) -> {
                    if (!name.contains(string.toLowerCase())) {
                        list.remove(entity);
                    } else {
                        list.add(entity);
                    }
                });
            });
        });
    }

    public static UIEntity createScreen(final Consumer<UIEntity> entityConsumer) {
        final int insets = 40;

        final UIEntity entity = new UIEntity();
        entity.add(new UIBox(UIBox.HBOX, 0));
        final UIEntity inner = new UIEntity();
        entity.add(createSpacerH(insets));
        entity.add(inner);
        entity.add(createSpacerH(insets));

        inner.add(new UIBox(UIBox.VBOX, 0));
        inner.setInheritHeight(true);
        inner.setInheritWidth(true);
        final UIEntity searchPanel = new UIEntity();
        searchPanel.add(new UIBox(UIBox.VBOX, 3));
        searchPanel.setInheritHeight(true);
        searchPanel.setInheritWidth(true);

        inner.add(createSpacerV(insets));
        inner.add(searchPanel);
        inner.add(createSpacerV(insets));
        searchPanel.add(new UIColor(0x6F000000, 5));

        entityConsumer.accept(searchPanel);
        return entity;
    }

    public static UIEntity createEnumElement(final UIEnumerable enumerable,
            final IIntegerable<?> property, final IntConsumer consumer, final int minWidth,
            final int value) {
        if (property instanceof DisableIntegerable<?>)
            enumerable.setMin(-1);
        enumerable.setIndex(value);
        final UIEntity middle = new UIEntity();
        final UIEntity hbox = new UIEntity();
        middle.setInheritWidth(true);
        middle.setInheritHeight(true);

        final UIButton middleButton = new UIButton("");
        final IntConsumer acceptOr = in -> {
            if (in >= property.count())
                return;
            middleButton.setText(property.getNamedObj(in));
        };
        enumerable.setOnChange(consumer.andThen(acceptOr));
        middle.add(new UIOnUpdate(() -> acceptOr.accept(enumerable.getIndex())));
        middle.add(middleButton);
        middle.add(enumerable);
        middle.add(new UIClickable(entity -> middle.getLastUpdateEvent().base
                .push(createSelectionScreen(enumerable, property))));

        acceptOr.accept(value);

        hbox.add(new UIBox(UIBox.VBOX, 1));
        final String desc = property.getDescriptionForName();
        if (desc != null)
            hbox.add(new UIToolTip(desc));
        hbox.add(middle);
        hbox.setInheritWidth(true);
        hbox.setHeight(22);
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

        final SoundManager handler = Minecraft.getInstance().getSoundManager();

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
            handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
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
            handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
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
        final SoundManager handler = Minecraft.getInstance().getSoundManager();
        final UIEntity settingsEnt = new UIEntity();
        settingsEnt.setHeight(20);
        settingsEnt.setWidth(width);
        settingsEnt.add(new UIButton(name));
        settingsEnt.add(new UIClickable(consumer.andThen(
                e -> handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f)))));
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

    public static UIEntity createLabel(final String name, final int color, final float scale) {
        final UIEntity entity = new UIEntity();
        final UILabel label = new UILabel(name);
        entity.setHeight(15 * scale);
        entity.setInheritWidth(true);
        entity.setScaleX(scale);
        entity.setScaleY(scale);
        label.setTextColor(color);
        entity.add(label);
        return entity;
    }

    public static UIEntity createLabel(final String name, final int color) {
        return createLabel(name, color, 1);
    }

    public static UIEntity createLabel(final String name, final float scale) {
        return createLabel(name, UILabel.DEFAULT_STRING_COLOR, scale);
    }

    public static UIEntity createLabel(final String name) {
        return createLabel(name, 1);
    }
}