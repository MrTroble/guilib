package eu.gir.girsignals.guis.guilib.entitys;

import java.util.function.IntConsumer;

import eu.gir.girsignals.guis.guilib.UIAutoSync;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class UIEnumerable extends UIComponent implements UIAutoSync {

	private IntConsumer onChange;
	private int index;
	private int max;
	private int min;
	private String id;

	public UIEnumerable(IntConsumer onChange, int max, String id) {
		this.onChange = onChange;
		this.max = max;
		this.id = id;
		this.min = 0;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
	}

	@Override
	public void update() {
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		boolean isChanged = index != this.index;
		if (index >= this.getMin() && index <= this.getMax())
			this.index = index;
		if (isChanged)
			this.onChange.accept(index);
	}

	@Override
	public void write(NBTTagCompound compound) {
		compound.setInteger(id, index);
	}

	@Override
	public void read(NBTTagCompound compound) {
		this.setIndex(compound.getInteger(id));
		this.onChange.accept(this.index);
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public String getId() {
		return id;
	}

	public IntConsumer getOnChange() {
		return onChange;
	}

	public void setOnChange(IntConsumer onChange) {
		this.onChange = onChange;
	}

}
