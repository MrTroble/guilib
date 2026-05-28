package com.troblecodings.core;

public class HexConverter {

    public static int decodeARGB(final String hex) {
        String value = hex;
        boolean isDecimal = false;

        if (value.startsWith("0x") || value.startsWith("0X")) {
            value = value.substring(2);
        } else if (value.startsWith("#")) {
            value = value.substring(1);
        } else {
            isDecimal = true;
        }

        return isDecimal ? Integer.valueOf(value) : (int) Long.parseUnsignedLong(value, 16);
    }

}