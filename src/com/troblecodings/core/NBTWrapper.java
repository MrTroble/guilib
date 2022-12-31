package com.troblecodings.core;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

public class NBTWrapper {
	public CompoundTag tag;

	public NBTWrapper(CompoundTag tag) {
		super();
		this.tag = tag;
	}
	
	public NBTWrapper() {
		this(new CompoundTag());
	}
	
	public boolean contains(String key) {
		return tag.contains(key);
	}
	
	public void putBoolean(String key, boolean value) {
		tag.putBoolean(key, value);
	}
	
	public void putInteger(String key, int value) {
		tag.putInt(key, value);
	}

	public void putString(String key, String value) {
		tag.putString(key, value);
	}

	public void putFloat(String key, float value) {
		tag.putFloat(key, value);
	}

	public void putList(String key, Iterable<NBTWrapper> value) {
		ListTag list = new ListTag();
		value.forEach(tagWrapper -> list.add(tagWrapper.tag));
		tag.put(key, list);
	}
	
	public void putWrapper(String key, NBTWrapper value) {
		tag.put(key, value.tag);
	}
	
	public void putBlockPos(String key, BlockPos value) {
		putWrapper(key, getBlockPosWrapper(value));
	}

	public boolean getBoolean(String key) {
		return tag.getBoolean(key);
	}

	public int getInteger(String key) {
		return tag.getInt(key);
	}

	public String getString(String key) {
		return tag.getString(key);
	}

	public float getFloat(String key) {
		return tag.getFloat(key);
	}
	
	public BlockPos getBlockPos(String key) {
		return getWrapper(key).getAsPos();
	}

	public List<NBTWrapper> getList(String key) {
		ListTag list = (ListTag) tag.get(key);
		return list.stream().map(tag -> new NBTWrapper((CompoundTag) tag)).toList();
	}

	public NBTWrapper getWrapper(String key) {
		return new NBTWrapper((CompoundTag) tag.get(key));
	}
	
	public BlockPos getAsPos() {
		return NbtUtils.readBlockPos(tag);
	}
	
	public NBTWrapper copy() {
		return new NBTWrapper(this.tag.copy());
	}
	
	public static NBTWrapper getBlockPosWrapper(BlockPos pos) {
		return new NBTWrapper(NbtUtils.writeBlockPos(pos));
	}
}