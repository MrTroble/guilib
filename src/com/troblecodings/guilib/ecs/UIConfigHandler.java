package com.troblecodings.guilib.ecs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class UIConfigHandler {
    
    private static final ForgeConfigSpec.Builder UI_CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    public static final Client UI_CLIENT = new Client(UI_CLIENT_BUILDER);

    public static final ForgeConfigSpec UI_CLIENT_SPEC = UI_CLIENT_BUILDER.build();

    public static class Client {

        public final ConfigValue<Integer> basicTextColor;
        public final ConfigValue<Integer> infoTextColor;
        public final ConfigValue<Integer> errorTextColor;

        public Client(final ForgeConfigSpec.Builder builder) {
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

}
