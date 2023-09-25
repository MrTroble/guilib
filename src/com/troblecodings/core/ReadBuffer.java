package com.troblecodings.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import net.minecraft.core.BlockPos;

public class ReadBuffer {

    private final ByteBuffer readBuffer;

    public ReadBuffer(final ByteBuffer buffer) {
        this.readBuffer = buffer;
    }

    public byte getByte() {
        return readBuffer.get();
    }

    public int getInt() {
        return readBuffer.getInt();
    }

    public float getFloat() {
        return readBuffer.getFloat();
    }

    public double getDouble() {
        return readBuffer.getDouble();
    }

    public long getLong() {
        return readBuffer.getLong();
    }

    public int getByteToUnsignedInt() {
        return Byte.toUnsignedInt(readBuffer.get());
    }

    public boolean getBoolean() {
        return getByte() == 1 ? true : false;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(readBuffer.getInt(), readBuffer.getInt(), readBuffer.getInt());
    }

    public <T extends Enum<T>> T getEnumValue(final Class<T> enumClass) {
        return enumClass.getEnumConstants()[getInt()];
    }

    public String getString() {
        final int size = readBuffer.getInt();
        final byte[] array = new byte[size];
        for (int i = size; i < size; i++)
            array[i] = readBuffer.get();
        try {
            return new String(array, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
}