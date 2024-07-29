package com.tunefulturnip.strongerleads.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.tunefulturnip.strongerleads.StrongerLeadsAttachment;
import com.tunefulturnip.strongerleads.item.StrongerLeadsItems;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Leashable.class)
public interface LeashableMixin {

    @Inject(method = "tickLeash(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;distanceTo(Lnet/minecraft/world/entity/Entity;)F"),
            cancellable = true)
    private static <E extends Entity & Leashable> void tickLeash(E pEntity, CallbackInfo ci, @Local(ordinal = 1) Entity entity) {
        if (pEntity.hasData(StrongerLeadsAttachment.LEASH_LENGTH.get()) && pEntity.hasData(StrongerLeadsAttachment.LEASH_LENGTH.get())) {
            float f = pEntity.distanceTo(entity);
            if (!pEntity.handleLeashAtDistance(entity, f)) {
                ci.cancel();
            }

            int leashLength = pEntity.getData(StrongerLeadsAttachment.LEASH_LENGTH) * 2;
            if (leashLength < -5)
                leashLength = -5;

            if (f > 10.0F + (pEntity.getData(StrongerLeadsAttachment.LEASH_STRENGTH) + leashLength)) {
                pEntity.leashTooFarBehaviour();
                pEntity.removeData(StrongerLeadsAttachment.LEASH_LENGTH);
                pEntity.removeData(StrongerLeadsAttachment.LEASH_STRENGTH);
            } else if (f > 6.0F + leashLength) {
                pEntity.elasticRangeLeashBehaviour(entity, f - leashLength);
                pEntity.checkSlowFallDistance();
            } else {
                pEntity.closeRangeLeashBehaviour(entity);
            }
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "restoreLeashFromSave",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private static boolean restoreLeashFromSave(Entity instance, ItemLike pItem) {
        if (instance.hasData(StrongerLeadsAttachment.LEASH_LENGTH)) {
            ItemStack stack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get(), 1);
            stack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord(instance.getData(StrongerLeadsAttachment.LEASH_STRENGTH), instance.getData(StrongerLeadsAttachment.LEASH_LENGTH)));
            instance.spawnAtLocation(stack);
            instance.removeData(StrongerLeadsAttachment.LEASH_STRENGTH);
            instance.removeData(StrongerLeadsAttachment.LEASH_LENGTH);
            return true;
        }
        return false;
    }

    @WrapWithCondition(method = "dropLeash(Lnet/minecraft/world/entity/Entity;ZZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private static boolean dropLeash(Entity instance, ItemLike pItem) {
        if (instance.hasData(StrongerLeadsAttachment.LEASH_LENGTH)) {
            ItemStack stack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get(), 1);
            stack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord(instance.getData(StrongerLeadsAttachment.LEASH_STRENGTH), instance.getData(StrongerLeadsAttachment.LEASH_LENGTH)));
            instance.spawnAtLocation(stack);
            instance.removeData(StrongerLeadsAttachment.LEASH_STRENGTH);
            instance.removeData(StrongerLeadsAttachment.LEASH_LENGTH);
            return false;
        }
        else return true;
    }
}