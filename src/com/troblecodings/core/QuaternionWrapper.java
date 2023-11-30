package com.troblecodings.core;

import net.minecraft.util.math.vector.Quaternion;

public class QuaternionWrapper {
    
    public static Quaternion fromXYZ(final float rotateX, final float rotateY, final float rotateZ) {
        final Quaternion quaternion = Quaternion.ONE.copy();
        quaternion.mul(new Quaternion((float)Math.sin(rotateX / 2.0F), 0.0F, 0.0F, (float)Math.cos(rotateX / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, (float)Math.sin(rotateY / 2.0F), 0.0F, (float)Math.cos(rotateY / 2.0F)));
        quaternion.mul(new Quaternion(0.0F, 0.0F, (float) Math.sin(rotateZ / 2.0F),
                (float) Math.cos(rotateZ / 2.0F)));
        return quaternion;
    }
}
