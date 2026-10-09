package com.troblecodings.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class NBTWrapper {
    public CompoundTag tag;
    private final Runnable onChanged;

    public NBTWrapper(final CompoundTag tag) {
        this(tag, () -> { });
    }

    private NBTWrapper(final CompoundTag tag, final Runnable onChanged) {
        this.tag = tag;
        this.onChanged = onChanged;
    }

    public NBTWrapper() {
        this(new CompoundTag());
    }

    public boolean contains(final String key) {
        return tag.contains(key);
    }

    public void putBoolean(final String key, final boolean value) {
        tag.putBoolean(key, value);
        onChanged.run();
    }

    public void putInteger(final String key, final int value) {
        tag.putInt(key, value);
        onChanged.run();
    }

    public void putByte(final String key, final byte value) {
        tag.putByte(key, value);
        onChanged.run();
    }

    public void putString(final String key, final String value) {
        tag.putString(key, value);
        onChanged.run();
    }

    public void putFloat(final String key, final float value) {
        tag.putFloat(key, value);
        onChanged.run();
    }

    public void putList(final String key, final Iterable<NBTWrapper> value) {
        final ListTag list = new ListTag();
        value.forEach(tagWrapper -> list.add(tagWrapper.tag));
        tag.put(key, list);
        onChanged.run();
    }

    public void putWrapper(final String key, final NBTWrapper value) {
        tag.put(key, value.tag);
        onChanged.run();
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

    public byte getByte(final String key) {
        return tag.getByte(key);
    }

    public String getString(final String key) {
        return tag.getString(key);
    }

    public float getFloat(final String key) {
        return tag.getFloat(key);
    }

    public BlockPos getBlockPos(final String key) {
        final NBTWrapper wrapper = getWrapper(key);
        return wrapper.isTagNull() ? null : wrapper.getAsPos();
    }

    public List<NBTWrapper> getList(final String key) {
        if (isTagNull())
            return new ArrayList<>();
        final ListTag list = (ListTag) tag.get(key);
        final List<NBTWrapper> returnList = new ArrayList<>();
        if (list == null)
            return returnList;
        list.forEach(tag -> returnList.add(new NBTWrapper((CompoundTag) tag, onChanged)));
        return returnList;
    }

    public NBTWrapper getWrapper(final String key) {
        return new NBTWrapper((CompoundTag) tag.get(key), onChanged);
    }

    public BlockPos getAsPos() {
        return new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
    }

    public NBTWrapper copy() {
        return new NBTWrapper(this.tag.copy());
    }

    public Set<String> keySet() {
        return this.tag.getAllKeys();
    }

    public void remove(final String key) {
        tag.remove(key);
        onChanged.run();
    }

    public boolean isTagNull() {
        return tag == null;
    }

    // Keep the legacy X/Y/Z compound format for existing saved data; NbtUtils now writes an int array.
    public static NBTWrapper getBlockPosWrapper(final BlockPos pos) {
        final NBTWrapper wrapper = new NBTWrapper();
        wrapper.putInteger("X", pos.getX());
        wrapper.putInteger("Y", pos.getY());
        wrapper.putInteger("Z", pos.getZ());
        return wrapper;
    }

    public static NBTWrapper getOrCreateWrapper(final ItemStack stack) {
        final CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag();
        final NBTWrapper wrapper = forStack(stack, tag);
        if (!stack.has(DataComponents.CUSTOM_DATA))
            wrapper.onChanged.run();
        return wrapper;
    }

    public static NBTWrapper createForStack(final ItemStack stack) {
        final NBTWrapper wrapper = forStack(stack, new CompoundTag());
        wrapper.onChanged.run();
        return wrapper;
    }

    private static NBTWrapper forStack(final ItemStack stack, final CompoundTag tag) {
        return new NBTWrapper(tag, () -> stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)));
    }

    @Override
    public String toString() {
        return tag.toString();
    }
}
