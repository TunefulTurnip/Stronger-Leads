package com.tunefulturnip.strongerleads;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class StrongerLeadsConfig {

    public static class Common {
        public final ModConfigSpec.ConfigValue<Integer> maxUpgradeLevel;
        public Common(ModConfigSpec.Builder builder) {

            builder.push("general");
            maxUpgradeLevel = builder
                    .comment("The maximum number of times a lead can be upgraded")
                    .translation("config.stronger_leads.max_upgrade_level")
                    .define("Max Upgrade Level", 5);
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
