package com.tunefulturnip.strongerleads.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tunefulturnip.strongerleads.StrongerLeadsMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StrongerLeadsDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(StrongerLeadsMod.MODID);

    public static final Codec<LeadRecord> LEAD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BYTE.fieldOf("strength").forGetter(LeadRecord::strength),
                    Codec.BYTE.fieldOf("length").forGetter(LeadRecord::length)
            ).apply(instance, LeadRecord::new)
    );
    public static final StreamCodec<ByteBuf, LeadRecord> LEAD_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE, LeadRecord::strength,
            ByteBufCodecs.BYTE, LeadRecord::length,
            LeadRecord::new
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LeadRecord>> LEAD = DATA_COMPONENT_TYPES.registerComponentType(
            "lead",
            builder -> builder
                    .persistent(LEAD_CODEC)
                    .networkSynchronized(LEAD_STREAM_CODEC)
    );
}
