package com.tunefulturnip.strongerleads;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class StrongerLeadsAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, StrongerLeadsMod.MODID);

    public static final Supplier<AttachmentType<Byte>> LEASH_STRENGTH = ATTACHMENT_TYPES.register(
            "leash_strength", () -> AttachmentType.builder(() -> (byte) 1).serialize(Codec.BYTE).build()
    );

    public static final Supplier<AttachmentType<Byte>> LEASH_LENGTH = ATTACHMENT_TYPES.register(
            "leash_length", () -> AttachmentType.builder(() -> (byte) 1).serialize(Codec.BYTE).build()
    );
}
