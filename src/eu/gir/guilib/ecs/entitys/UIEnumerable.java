package eu.gir.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import eu.gir.guilib.ecs.interfaces.UIAutoSync;
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
	
	public UIEnumerable(int max, String id) {
		this.max = max;
		this.id = id;
		this.min = 0;
		this.onChange = e -> {
		};
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
		final boolean isChanged = index != this.index;
		if (index >= this.getMin() && index <= this.getMax()) {
			this.index = index;
		} else {
			this.index = this.getMin();
		}
		if (isChanged)
			this.onChange.accept(index);
	}
	
	@Override
	public void write(NBTTagCompound compound) {
		if (id != null)
			compound.setInteger(id, index);
	}
	
	@Override
	public void read(NBTTagCompound compound) {
		if (id != null && compound.hasKey(id)) {
			this.setIndex(compound.getInteger(id));
		} else {
			this.setIndex(this.min);
		}
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
		this.update();
	}
	
	public void setMax(int max) {
		this.max = max;
		this.update();
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
	
	@Override
	public String getID() {
		return this.id;
	}
	
	@Override
	public void setID(String id) {
		this.id = id;
	}
	
}