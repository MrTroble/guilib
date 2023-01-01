package com.troblecodings.guilib.ecs.entitys;

import java.util.function.IntConsumer;

import com.troblecodings.core.NBTWrapper;
import com.troblecodings.guilib.ecs.interfaces.UIAutoSync;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UIEnumerable extends UIComponent implements UIAutoSync {

    private IntConsumer onChange;
    private int index;
    private int max;
    private int min;
    private String id;

    public UIEnumerable(final int max, final String id) {
        this.max = max;
        this.id = id;
        this.min = 0;
        this.onChange = e -> {
        };
    }

    @Override
    public void draw(final DrawInfo info) {
    }

    @Override
    public void update() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
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
    public void write(final NBTWrapper compound) {
        if (id != null)
            compound.putInteger(id, index);
    }

    @Override
    public void read(final NBTWrapper compound) {
        if (id != null && compound.contains(id)) {
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

    public void setMin(final int min) {
        this.min = min;
        this.update();
    }

    public void setMax(final int max) {
        this.max = max;
        this.update();
    }

    public String getId() {
        return id;
    }

    public IntConsumer getOnChange() {
        return onChange;
    }

    public void setOnChange(final IntConsumer onChange) {
        this.onChange = onChange;
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
