package com.troblecodings.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.util.TriConsumer;

import com.troblecodings.core.interfaces.INetworkSaveable;

import net.minecraft.core.BlockPos;

public class WriteBuffer {

    public static final BiConsumer<WriteBuffer, BlockPos> BLOCKPOS_CONSUMER = (buffer,
            pos) -> buffer.putBlockPos(pos);

    public static final BiConsumer<WriteBuffer, Integer> INT_CONSUMER = (buffer, i) -> buffer
            .putInt(i);

    public static final BiConsumer<WriteBuffer, Byte> BYTE_CONSUMER = (buffer, b) -> buffer
            .putByte(b);

    public static final BiConsumer<WriteBuffer, Integer> INT_TO_BYTE_CONSUMER = (buffer,
            i) -> buffer.putByte(i.byteValue());

    public static final BiConsumer<WriteBuffer, String> STRING_CONSUMER = (buffer, str) -> buffer
            .putString(str);

    public static <T extends INetworkSaveable> BiConsumer<WriteBuffer, T> getINetworkSaveableConsumer() {
        return (buf, type) -> type.writeNetwork(buf);
    }

    public static <E extends Enum<E>> BiConsumer<WriteBuffer, E> getEnumConsumer() {
        return (buffer, e) -> buffer.putEnumValue(e);
    }

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
            e.printStackTrace();
        }
    }

    public <T extends INetworkSaveable> void putINetworkSaveable(final T type) {
        type.writeNetwork(this);
    }

    public <T extends Enum<T>> void putEnumValue(final Enum<T> enumValue) {
        putInt(enumValue.ordinal());
    }

    public <T> void putList(final List<T> list, final BiConsumer<WriteBuffer, T> consumer) {
        putInt(list.size());
        list.forEach(element -> consumer.accept(this, element));
    }

    public <T extends INetworkSaveable> void putISaveableList(final List<T> list) {
        putList(list, (buf, type) -> type.writeNetwork(buf));
    }

    public <K, V> void putMap(final Map<K, V> map, final BiConsumer<WriteBuffer, K> keyConsumer,
            final BiConsumer<WriteBuffer, V> valueConsumer) {
        putInt(map.size());
        map.forEach((key, value) -> {
            keyConsumer.accept(this, key);
            valueConsumer.accept(this, value);
        });
    }

    public <K extends INetworkSaveable, V extends INetworkSaveable> void putINetworkSaveableMap(
            final Map<K, V> map) {
        putMap(map, (buf, key) -> key.writeNetwork(buf), (buf, value) -> value.writeNetwork(buf));
    }

    public <K, V> void putMapWithCombinedValueConsumer(final Map<K, V> map,
            final BiConsumer<WriteBuffer, K> keyConsumer,
            final TriConsumer<WriteBuffer, K, V> valueConsumer) {
        putInt(map.size());
        map.forEach((key, value) -> {
            keyConsumer.accept(this, key);
            valueConsumer.accept(this, key, value);
        });
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