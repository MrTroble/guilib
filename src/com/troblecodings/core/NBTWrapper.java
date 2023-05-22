package com.troblecodings.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class NBTWrapper {
    public NBTTagCompound tag;

    public NBTWrapper(final NBTTagCompound tag) {
        super();
        this.tag = tag;
    }

    public NBTWrapper() {
        this(new NBTTagCompound());
    }

    public boolean contains(final String key) {
        return tag.hasKey(key);
    }

    public void putBoolean(final String key, final boolean value) {
        tag.setBoolean(key, value);
    }

    public void putInteger(final String key, final int value) {
        tag.setInteger(key, value);
    }

    public void putString(final String key, final String value) {
        tag.setString(key, value);
    }

    public void putFloat(final String key, final float value) {
        tag.setFloat(key, value);
    }

    public void putList(final String key, final Iterable<NBTWrapper> value) {
        final NBTTagList list = new NBTTagList();
        value.forEach(tagWrapper -> list.appendTag(tagWrapper.tag));
        tag.setTag(key, list);
    }

    public void putWrapper(final String key, final NBTWrapper value) {
        tag.setTag(key, value.tag);
    }

    public void putBlockPos(final String key, final BlockPos value) {
        putWrapper(key, getBlockPosWrapper(value));
    }

    public boolean getBoolean(final String key) {
        return tag.getBoolean(key);
    }

    public int getInteger(final String key) {
        return tag.getInteger(key);
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
        final NBTTagList list = (NBTTagList) tag.getTag(key);
        final List<NBTWrapper> returnList = new ArrayList<>();
        list.forEach(tag -> returnList.add(new NBTWrapper((NBTTagCompound) tag)));
        return returnList;
    }

    public NBTWrapper getWrapper(final String key) {
        return new NBTWrapper((NBTTagCompound) tag.getTag(key));
    }

    public BlockPos getAsPos() {
        return NBTUtil.getPosFromTag(tag);
    }

    public NBTWrapper copy() {
        return new NBTWrapper(this.tag.copy());
    }

    public Set<String> keySet() {
        return this.tag.getKeySet();
    }

    public void remove(final String key) {
        tag.removeTag(key);
    }

    public static NBTWrapper getBlockPosWrapper(final BlockPos pos) {
        return new NBTWrapper(NBTUtil.createPosTag(pos));
    }

    public static NBTWrapper getOrCreateWrapper(final ItemStack stack) {
        return new NBTWrapper(stack.getTagCompound());
    }

    public static NBTWrapper createForStack(final ItemStack stack) {
        final NBTWrapper wrapper = new NBTWrapper();
        stack.setTagCompound(wrapper.tag);
        return wrapper;
    }

}