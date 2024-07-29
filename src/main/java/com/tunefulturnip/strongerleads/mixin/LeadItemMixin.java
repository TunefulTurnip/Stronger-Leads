package com.tunefulturnip.strongerleads.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(LeadItem.class)
public class LeadItemMixin extends Item {

    public LeadItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal( "6 ").append(Component.translatable("item.stronger_leads.lead_max_length")).withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.literal("10 ").append(Component.translatable("item.stronger_leads.lead_max_constrain")).withStyle(ChatFormatting.BLUE));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
