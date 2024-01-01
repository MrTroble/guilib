package com.troblecodings.core;

import org.joml.Quaternionf;

public class QuaternionWrapper {
    
    public static Quaternionf fromXYZ(final float rotateX, final float rotateY, final float rotateZ) {
        final Quaternionf quaternion = new Quaternionf();
        quaternion.mul(new Quaternionf((float)Math.sin(rotateX / 2.0F), 0.0F, 0.0F, (float)Math.cos(rotateX / 2.0F)));
        quaternion.mul(new Quaternionf(0.0F, (float)Math.sin(rotateY / 2.0F), 0.0F, (float)Math.cos(rotateY / 2.0F)));
        quaternion.mul(new Quaternionf(0.0F, 0.0F, (float) Math.sin(rotateZ / 2.0F),
                (float) Math.cos(rotateZ / 2.0F)));
        return quaternion;
    }

}
