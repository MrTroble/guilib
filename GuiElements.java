package eu.gir.girsignals.guis.guilib;

import java.util.function.IntConsumer;

import net.minecraft.client.Minecraft;

public class GuiElements {

	public static UIEntity createBoolElement(IIntegerable<?> property, IntConsumer consumer) {
		final UIEntity middle = new UIEntity();
		middle.setBounds(Minecraft.getMinecraft().fontRenderer.getStringWidth(property.getLocalizedName()) + 20, 20);

		final UICheckBox middleButton = new UICheckBox(property.getName());
		final UIClickable clickable = new UIClickable(e -> {
			middleButton.setChecked(!middleButton.isChecked());
			consumer.accept(middleButton.isChecked() ? 1:0);
		});
		middleButton.setOnChange(consumer);
		middleButton.setText(property.getLocalizedName());
		middle.add(middleButton);
		middle.add(clickable);
		return middle;
	}

	public static UIEntity createEnumElement(IIntegerable<?> property, IntConsumer consumer) {
		return createEnumElement(property, consumer, property.getMaxWidth(Minecraft.getMinecraft().fontRenderer) + 8);
	}

	public static UIEntity createEnumElement(IIntegerable<?> property, IntConsumer consumer, int minWidth) {
		final UIEntity middle = new UIEntity();
		middle.setBounds(minWidth, 20);

		final UIButton leftButton = new UIButton("<");
		final UIButton rightButton = new UIButton(">");

		final UIButton middleButton = new UIButton(property.getNamedObj(0));
		final UIEnumerable enumerable = new UIEnumerable(null, property.count(), property.getName());
		enumerable.setOnChange(consumer.andThen(in -> {
			middleButton.setText(property.getNamedObj(in));
			rightButton.setEnabled(true);
			leftButton.setEnabled(true);
			if (in <= enumerable.getMin())
				leftButton.setEnabled(false);
			if (in >= enumerable.getMax() - 1)
				rightButton.setEnabled(false);
		}));
		middle.add(middleButton);
		middle.add(enumerable);
		
		final UIEntity left = new UIEntity();
		left.setBounds(20, 20);

		final UIClickable leftclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int min = enumerable.getMin();
			if (id <= min)
				return;
			enumerable.setIndex(id - 1);
		});
		left.add(leftButton);
		left.add(leftclickable);

		final UIEntity right = new UIEntity();
		right.setBounds(20, 20);

		final UIClickable rightclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int max = enumerable.getMax() - 1;
			if (id >= max)
				return;
			enumerable.setIndex(id + 1);
		});
		right.add(rightButton);
		right.add(rightclickable);
		
		enumerable.setIndex(0);

		final UIEntity hbox = new UIEntity();
		hbox.add(new UIHBox(1));
		hbox.add(left);
		hbox.add(middle);
		hbox.add(right);
		hbox.setBounds(left.getWidth() + middle.getWidth() + right.getWidth(), 20);
		return hbox;
	}

	public static UIEntity createPageSelect(UIVBox vbox) {
		final UIEntity middle = new UIEntity();
		middle.setBounds(100, 20);

		final UIButton leftButton = new UIButton("<");
		final UIButton rightButton = new UIButton(">");

		final UIButton middleButton = new UIButton("DDDD");
		middle.add(middleButton);

		final UIEnumerable enumerable = new UIEnumerable(null, 0, "pageselect");
		enumerable.setOnChange(in -> {
			middleButton.setText("Page: " + in);
			rightButton.setEnabled(true);
			leftButton.setEnabled(true);
			if (in <= enumerable.getMin())
				leftButton.setEnabled(false);
			if (in >= enumerable.getMax() - 1)
				rightButton.setEnabled(false);
			vbox.setPage(in);
		});

		vbox.parent.add(new UIOnUpdate(() -> {
			final int max = vbox.getMaxPages();
			if (max < 1)
				return;
			enumerable.setMax(max);
			final int current = enumerable.getIndex();
			enumerable.setIndex(current >= max ? max - 1 : current);
		}));
		middle.add(enumerable);

		final UIEntity left = new UIEntity();
		left.setBounds(20, 20);

		final UIClickable leftclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int min = enumerable.getMin();
			if (id <= min)
				return;
			enumerable.setIndex(id - 1);
		});
		left.add(leftButton);
		left.add(leftclickable);

		final UIEntity right = new UIEntity();
		right.setBounds(20, 20);

		final UIClickable rightclickable = new UIClickable(e -> {
			final int id = enumerable.getIndex();
			final int max = enumerable.getMax() - 1;
			if (id >= max)
				return;
			enumerable.setIndex(id + 1);
		});
		right.add(rightButton);
		right.add(rightclickable);

		final UIEntity hbox = new UIEntity();
		hbox.add(new UIHBox(1));
		hbox.add(left);
		hbox.add(middle);
		hbox.add(right);
		hbox.setBounds(left.getWidth() + middle.getWidth() + right.getWidth(), 20);
		return hbox;
	}
}