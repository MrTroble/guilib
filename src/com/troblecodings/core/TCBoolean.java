package com.troblecodings.core;

import java.io.Serializable;

public class TCBoolean implements Serializable, Comparable<TCBoolean> {

    public static final TCBoolean TRUE = new TCBoolean(true);

    public static final TCBoolean FALSE = new TCBoolean(false);

    /*
     * @SuppressWarnings("unchecked") public static final Class<TCBoolean> TYPE =
     * (Class<TCBoolean>) Class.getPrimitiveClass("boolean");
     */

    private final boolean value;

    private static final long serialVersionUID = -3665804199014368530L;

    public TCBoolean(final boolean value) {
        this.value = value;
    }

    public TCBoolean(final String s) {
        this(parseBoolean(s));
    }

    public static boolean parseBoolean(final String s) {
        return ((s != null) && s.equalsIgnoreCase("true"));
    }

    public boolean booleanValue() {
        return value;
    }

    public static TCBoolean valueOf(final boolean b) {
        return (b ? TRUE : FALSE);
    }

    public static TCBoolean valueOf(final String s) {
        return parseBoolean(s) ? TRUE : FALSE;
    }

    public static String toString(final boolean b) {
        return b ? "true" : "false";
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public int hashCode() {
        return TCBoolean.hashCode(value);
    }

    public static int hashCode(final boolean value) {
        return value ? 1231 : 1237;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof TCBoolean)
            return value == ((TCBoolean) obj).booleanValue();
        return false;
    }

    public static boolean getBoolean(final String name) {
        boolean result = false;
        try {
            result = parseBoolean(System.getProperty(name));
        } catch (IllegalArgumentException | NullPointerException e) {
        }
        return result;
    }

    @Override
    public int compareTo(final TCBoolean b) {
        return compare(this.value, b.value);
    }

    public static int compare(final boolean x, final boolean y) {
        return (x == y) ? 0 : (x ? 1 : -1);
    }

    public static boolean logicalAnd(final boolean a, final boolean b) {
        return a && b;
    }

    public static boolean logicalOr(final boolean a, final boolean b) {
        return a || b;
    }

    public static boolean logicalXor(final boolean a, final boolean b) {
        return a ^ b;
    }

}
