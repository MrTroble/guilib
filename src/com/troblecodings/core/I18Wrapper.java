package com.troblecodings.core;

import net.minecraft.client.resources.I18n;

public class I18Wrapper {

    public static String format(final String str, final Object... obj) {
        return I18n.get(str, obj);
    }

    public static boolean exists(final String str) {
        return I18n.exists(str);
    }
}
