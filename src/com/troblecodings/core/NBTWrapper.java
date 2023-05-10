package com.troblecodings.core;

import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;

public class NBTWrapper {
    public CompoundTag tag;

    public NBTWrapper(final CompoundTag tag) {
        super();
        this.tag = tag;
    }

    public NBTWrapper() {
        this(new CompoundTag());
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
        final ListTag list = new ListTag();
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
        final ListTag list = (ListTag) tag.get(key);
        return list.stream().map(tag -> new NBTWrapper((CompoundTag) tag)).toList();
    }

    public NBTWrapper getWrapper(final String key) {
        return new NBTWrapper((CompoundTag) tag.get(key));
    }

    public BlockPos getAsPos() {
        return NbtUtils.readBlockPos(tag);
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

    public static NBTWrapper getBlockPosWrapper(final BlockPos pos) {
        return new NBTWrapper(NbtUtils.writeBlockPos(pos));
    }

    public static NBTWrapper getOrCreateWrapper(final ItemStack stack) {
        return new NBTWrapper(stack.getOrCreateTag());
    }

    public static NBTWrapper createForStack(final ItemStack stack) {
        final NBTWrapper wrapper = new NBTWrapper();
        stack.setTag(wrapper.tag);
        return wrapper;
    }

}