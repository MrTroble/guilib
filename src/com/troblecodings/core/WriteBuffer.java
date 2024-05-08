package com.troblecodings.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class WriteBuffer {

    private final List<Byte> allBytes;
    private ByteBuffer buildedBuffer;

    public WriteBuffer() {
        this.allBytes = new ArrayList<>();
    }

    public void putByte(final Byte b) {
        allBytes.add(b);
    }

    public void putInt(final int i) {
        for (final byte b : ByteBuffer.allocate(4).putInt(i).array())
            putByte(b);
    }

    public void putFloat(final float f) {
        for (final byte b : ByteBuffer.allocate(4).putFloat(f).array())
            putByte(b);
    }

    public void putDouble(final double d) {
        for (final byte b : ByteBuffer.allocate(8).putDouble(d).array())
            putByte(b);
    }

    public void putBlockPos(final BlockPos pos) {
        for (final byte b : ByteBuffer.allocate(12).putInt(pos.getX()).putInt(pos.getY())
                .putInt(pos.getZ()).array())
            putByte(b);
    }

    public void putBoolean(final boolean bool) {
        putByte((byte) (bool ? 1 : 0));
    }

    public void putString(final String str) {
        try {
            final byte[] array = str.getBytes("UTF-8");
            putInt(array.length);
            for (final byte b : array)
                putByte(b);
        } catch (final UnsupportedEncodingException e) {
        }
    }

    public <T extends Enum<T>> void putEnumValue(final Enum<T> enumValue) {
        putInt(enumValue.ordinal());
    }

    public void resetBuilder() {
        allBytes.clear();
    }

    public ByteBuffer getBuildedBuffer() {
        if (buildedBuffer == null)
            return build();
        return buildedBuffer;
    }

    public ByteBuffer build() {
        buildedBuffer = ByteBuffer.allocate(allBytes.size());
        allBytes.forEach(buildedBuffer::put);
        resetBuilder();
        return buildedBuffer;
    }
}