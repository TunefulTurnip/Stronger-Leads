package com.tunefulturnip.strongerleads.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.tunefulturnip.strongerleads.StrongerLeadsAttachment;
import com.tunefulturnip.strongerleads.item.StrongerLeadsItems;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;


@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract Level level();

    @Shadow
    @Nullable
    public abstract <T> T setData(AttachmentType<T> type, T data);

    @Inject(method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"),
            cancellable = true)
    public void interact(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Leashable leashable = (Leashable) this;
        if (itemstack.is(StrongerLeadsItems.LONGER_LEAD) && leashable.canHaveALeashAttachedToIt()) {
            if (!this.level().isClientSide()) {
                leashable.setLeashedTo(pPlayer, true);
            }

            LeadRecord record = itemstack.get(StrongerLeadsDataComponents.LEAD);
            if (record != null) {
                this.setData(StrongerLeadsAttachment.LEASH_LENGTH.get(), record.length());
                this.setData(StrongerLeadsAttachment.LEASH_STRENGTH.get(), record.strength());
            }
            itemstack.shrink(1);
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
        }
    }
}
