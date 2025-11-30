package com.troblecodings.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.troblecodings.core.interfaces.INetworkSaveable;

import net.minecraft.util.math.BlockPos;

public class ReadBuffer {

    public static final Function<ReadBuffer, BlockPos> BLOCKPOS_FUNCTION =
            (buffer) -> buffer.getBlockPos();

    public static final Function<ReadBuffer, Integer> INT_FUNCTION = (buffer) -> buffer.getInt();

    public static final Function<ReadBuffer, Byte> BYTE_FUNCTION = (buffer) -> buffer.getByte();

    public static final Function<ReadBuffer, Integer> BYTE_TO_INT_FUNCTION =
            (buffer) -> buffer.getByteToUnsignedInt();

    public static final Function<ReadBuffer, String> STRING_FUNCTION =
            (buffer) -> buffer.getString();

    /*
     * IMPORTANT NOTICE: If you want to use this Method, your Type t for your clazz
     * needs to have a default constructor with no parameters. Otherwise it will
     * crash! If you can#t provide a default construtor, we advice to write own
     * Wrappers like over this comment.
     */
    public static <T extends INetworkSaveable> Function<ReadBuffer, T> getINetworkSaveableFunction(
            final Class<T> clazz) {
        return buf -> buf.getINetworkSaveable(clazz);
    }

    public static <E extends Enum<E>> Function<ReadBuffer, E> getEnumFunction(
            final Class<E> clazz) {
        return (buffer) -> buffer.getEnumValue(clazz);
    }

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

    public TCBoolean getTcBoolean() {
        return getByte() == 1 ? TCBoolean.valueOf(true) : TCBoolean.valueOf(false);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(readBuffer.getInt(), readBuffer.getInt(), readBuffer.getInt());
    }

    public <T extends INetworkSaveable> T getINetworkSaveable(final Class<T> clazz) {
        try {
            final T type = clazz.newInstance();
            type.readNetwork(this);
            return type;
        } catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Enum<T>> T getEnumValue(final Class<T> enumClass) {
        return enumClass.getEnumConstants()[getInt()];
    }

    public <E> List<E> getList(final Function<ReadBuffer, E> function) {
        final List<E> list = new ArrayList<>();
        final int size = getInt();
        for (int i = 0; i < size; i++) {
            list.add(function.apply(this));
        }
        return list;
    }

    public <K, V> Map<K, V> getMap(final Function<ReadBuffer, K> keyFunction,
            final Function<ReadBuffer, V> valueFunction) {
        final Map<K, V> map = new HashMap<>();
        final int size = getInt();
        for (int i = 0; i < size; i++) {
            map.put(keyFunction.apply(this), valueFunction.apply(this));
        }
        return map;
    }

    public <K, V> Map<K, V> getMapWithCombinedValueFunc(final Function<ReadBuffer, K> keyFunction,
            final BiFunction<ReadBuffer, K, V> valueFunction) {
        final Map<K, V> map = new HashMap<>();
        final int size = getInt();
        for (int i = 0; i < size; i++) {
            final K key = keyFunction.apply(this);
            map.put(key, valueFunction.apply(this, key));
        }
        return map;
    }

    public String getString() {
        final int size = getInt();
        final byte[] array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = getByte();
        }
        try {
            return new String(array, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
