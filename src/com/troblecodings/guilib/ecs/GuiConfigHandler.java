package com.troblecodings.guilib.ecs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class GuiConfigHandler {

    public static ConfigValue<Integer> basicTextColor;
    public static ConfigValue<Integer> infoTextColor;
    public static ConfigValue<Integer> errorTextColor;

    public GuiConfigHandler(final ForgeConfigSpec.Builder builder) {
        String desc;
        builder.push("Client Only");

        desc = "Change the color of a default text. Default: -16777216";
        basicTextColor = builder.comment(desc).define("Basic text color", 0xFF000000);

        desc = "Change the color of an info text. Default: -16777046";
        infoTextColor = builder.comment(desc).define("info text color", 0xFF0000AA);

        desc = "Change the color of an error text. Default: -16776961";
        errorTextColor = builder.comment(desc).define("Error text color", 0xFF0000FF);

        builder.pop();
    }
}
