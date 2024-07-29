package com.tunefulturnip.strongerleads.item;

import com.tunefulturnip.strongerleads.StrongerLeadsAttachment;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class StrongerLeadItem extends LeadItem {
    public StrongerLeadItem(Properties pProperties) {
        super(pProperties.component(StrongerLeadsDataComponents.LEAD, new LeadRecord((byte) 0, (byte) 1)));
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(BlockTags.FENCES)) {
            Player player = pContext.getPlayer();
            if (!level.isClientSide && player != null) {
                bindPlayerMobs(player, level, blockpos, pContext.getItemInHand());
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static void bindPlayerMobs(Player pPlayer, Level pLevel, BlockPos pPos, ItemStack stack) {
        LeashFenceKnotEntity leashfenceknotentity = null;
        List<Entity> list = entitiesInArea(pLevel, pPos, p_353025_ -> p_353025_.getLeashHolder() == pPlayer);

        LeadRecord record = stack.get(StrongerLeadsDataComponents.LEAD);
        if(record == null) return;

        for (Entity leashable : list) {
            if (leashfenceknotentity == null) {
                leashfenceknotentity = LeashFenceKnotEntity.getOrCreateKnot(pLevel, pPos);
                leashfenceknotentity.playPlacementSound();
            }


            leashable.setData(StrongerLeadsAttachment.LEASH_STRENGTH, record.strength());
            leashable.setData(StrongerLeadsAttachment.LEASH_LENGTH, record.length());
            ((Leashable) leashable).setLeashedTo(leashfenceknotentity, true);
        }

        if (!list.isEmpty())
            pLevel.gameEvent(GameEvent.BLOCK_ATTACH, pPos, GameEvent.Context.of(pPlayer));
    }

    public static List<Entity> entitiesInArea(Level level, BlockPos pos, Predicate<Leashable> predicate) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        AABB aabb = new AABB((double)x - 7.0, (double)y - 7.0, (double)z - 7.0, (double)x + 7.0, (double)y + 7.0, (double)z + 7.0);
        return level.getEntitiesOfClass(Entity.class, aabb, entity -> entity instanceof Leashable leashable && predicate.test(leashable));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        if(pStack.has(StrongerLeadsDataComponents.LEAD)) {
            LeadRecord lead = pStack.get(StrongerLeadsDataComponents.LEAD);
            if(lead == null) return;

            int leashLength = lead.length() * 2;
            if(leashLength < -5)
                leashLength = -5;

            pTooltipComponents.add(Component.literal((6+leashLength) + " ").append(Component.translatable("item.stronger_leads.lead_max_length")).withStyle(ChatFormatting.BLUE));
            pTooltipComponents.add(Component.literal((10 + (lead.strength() + leashLength)) + " ").append(Component.translatable("item.stronger_leads.lead_max_constrain")).withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    @NotNull
    public Component getName(ItemStack pStack) {
        LeadRecord lead = pStack.get(StrongerLeadsDataComponents.LEAD);
        if(lead != null) {
            if(lead.strength() > 0) {
                return  Component.translatable("item.stronger_leads.reinforced").append(Component.translatable(this.getDescriptionId(pStack) + lead.length()));
            }
            else return Component.translatable(this.getDescriptionId(pStack) + lead.length());
        }
        else return super.getName(pStack);
    }
}
