package com.tunefulturnip.strongerleads;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class StrongerLeadsConfig {

    public static class Common {
        public final ModConfigSpec.ConfigValue<Integer> maxLengthUpgradeLevel;
        public final ModConfigSpec.ConfigValue<Integer> maxConstrainUpgradeLevel;
        public Common(ModConfigSpec.Builder builder) {

            builder.push("general");
            maxLengthUpgradeLevel = builder
                    .comment("The maximum number of times a lead's length can be upgraded. Requires a custom resource pack in order to work properly.")
                    .translation("config.stronger_leads.max_length_upgrade_level")
                    .define("Max Length Upgrade Level", 4);
            builder.pop();

            builder.push("general");
            maxConstrainUpgradeLevel = builder
                    .comment("The maximum number of times a lead's constrain can be upgraded. Requires a custom resource pack in order to work properly.")
                    .translation("config.stronger_leads.max_constrain_upgrade_level")
                    .define("Max Constrain Upgrade Level", 3);
            builder.pop();


        }
    }

    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
