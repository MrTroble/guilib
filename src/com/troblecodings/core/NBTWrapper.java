package com.troblecodings.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class NBTWrapper {
    public CompoundNBT tag;

    public NBTWrapper(final CompoundNBT tag) {
        super();
        this.tag = tag;
    }

    public NBTWrapper() {
        this(new CompoundNBT());
    }

    public boolean contains(final String key) {
        return tag.contains(key);
    }

    public void putBoolean(final String key, final boolean value) {
        tag.putBoolean(key, value);
    }

    public void putInteger(final String key, final int value) {
        tag.putInt(key, value);
    }

    public void putString(final String key, final String value) {
        tag.putString(key, value);
    }

    public void putFloat(final String key, final float value) {
        tag.putFloat(key, value);
    }

    public void putList(final String key, final Iterable<NBTWrapper> value) {
        final ListNBT list = new ListNBT();
        value.forEach(tagWrapper -> list.add(tagWrapper.tag));
        tag.put(key, list);
    }

    public void putWrapper(final String key, final NBTWrapper value) {
        tag.put(key, value.tag);
    }

    public void putBlockPos(final String key, final BlockPos value) {
        putWrapper(key, getBlockPosWrapper(value));
    }

    public boolean getBoolean(final String key) {
        return tag.getBoolean(key);
    }

    public int getInteger(final String key) {
        return tag.getInt(key);
    }

    public String getString(final String key) {
        return tag.getString(key);
    }

    public float getFloat(final String key) {
        return tag.getFloat(key);
    }

    public BlockPos getBlockPos(final String key) {
        return getWrapper(key).getAsPos();
    }

    public List<NBTWrapper> getList(final String key) {
        final ListNBT list = (ListNBT) tag.get(key);
        final List<NBTWrapper> returnList = new ArrayList<>();
        if (list == null)
            return returnList;
        list.forEach(tag -> returnList.add(new NBTWrapper((CompoundNBT) tag)));
        return returnList;
    }

    public NBTWrapper getWrapper(final String key) {
        return new NBTWrapper((CompoundNBT) tag.get(key));
    }

    public BlockPos getAsPos() {
        return NBTUtil.readBlockPos(tag);
    }

    public NBTWrapper copy() {
        return new NBTWrapper(this.tag.copy());
    }

    public Set<String> keySet() {
        return this.tag.getAllKeys();
    }

    public void remove(final String key) {
        tag.remove(key);
    }
    
    public boolean isTagNull() {
        return tag == null;
    }

    public static NBTWrapper getBlockPosWrapper(final BlockPos pos) {
        return new NBTWrapper(NBTUtil.writeBlockPos(pos));
    }

    public static NBTWrapper getOrCreateWrapper(final ItemStack stack) {
        return new NBTWrapper(stack.getOrCreateTag());
    }

    public static NBTWrapper createForStack(final ItemStack stack) {
        final NBTWrapper wrapper = new NBTWrapper();
        stack.setTag(wrapper.tag);
        return wrapper;
    }

    @Override
    public String toString() {
        return tag.toString();
    }

}