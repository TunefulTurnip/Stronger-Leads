package com.tunefulturnip.strongerleads.recipe;

import com.tunefulturnip.strongerleads.item.StrongerLeadsItems;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StrongerLeadCrafting extends CustomRecipe {

    public StrongerLeadCrafting(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int leadCount = 0;
        int stringCount = 0;
        int maxStringCount = 4;
        int sheerCount = 0;
        int maxSheerCount = 3;
        int ironCount = 0;
        int maxIronCount = 3;

        for (int k = 0; k < input.size(); k++) {
            ItemStack itemstack = input.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(StrongerLeadsItems.LONGER_LEAD.get())) {
                    leadCount++;
                    LeadRecord lead = itemstack.get(StrongerLeadsDataComponents.LEAD);
                    if(lead != null) {
                        maxStringCount = 4 - lead.length();
                        maxSheerCount = 3 + lead.length();
                        maxIronCount = 3 - lead.strength();
                    }
                }
                else if(itemstack.is(Items.LEAD)) {
                    leadCount++;
                }
                else if(itemstack.is(Items.SHEARS)) {
                    sheerCount++;
                }
                else if(itemstack.is(Items.IRON_INGOT))
                    ironCount++;
                else {
                    if (!itemstack.is(Items.STRING)) {
                        return false;
                    }

                    stringCount++;
                }

                if ((stringCount >= 1 && sheerCount >= 1) || leadCount > 1) {
                    return false;
                }
            }
        }

        if(stringCount < 1 && sheerCount < 1 && ironCount < 1)
            return false;
        else return stringCount <= maxStringCount && sheerCount <= maxSheerCount && ironCount <= maxIronCount;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        boolean isLead = false;
        int isSheer = 1;
        int count = 0;
        int ironCount = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        //List<ItemStack> shears = new ArrayList<>();


        for (int i = 0; i < input.size(); i++) {
            ItemStack itemstack1 = input.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.is(StrongerLeadsItems.LONGER_LEAD)) {
                    itemstack = itemstack1;
                }
                else if (itemstack1.is(Items.LEAD)) {
                    itemstack = itemstack1;
                    isLead = true;
                }
                else if(itemstack1.is(Items.STRING)) {
                    count++;
                }
                else if(itemstack1.is(Items.SHEARS)) {
                    count++;
                    isSheer = -1;
                }
                else if(itemstack1.is(Items.IRON_INGOT))
                    ironCount++;
            }
        }

        if(isLead || !itemstack.has(StrongerLeadsDataComponents.LEAD)) {
            ItemStack newStack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get(), 1);
            newStack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord((byte) ironCount, (byte) (count * isSheer)));
            return newStack;
        }
        else {
            LeadRecord record = itemstack.get(StrongerLeadsDataComponents.LEAD);
            byte newLength = (byte) (record.length() + (count * isSheer));

            if(newLength == 0)
                return new ItemStack(Items.LEAD, 1);
            else {
                ItemStack newStack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get(), 1);
                newStack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord((byte) (record.strength() + ironCount), newLength));
                return newStack;
            }
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput pInput) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pInput.size(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = pInput.getItem(i);
            if (item.is(Items.SHEARS)) {
                ItemStack stack = item.copy();
                int damage = stack.getDamageValue() + 1;
                stack.setDamageValue(damage);
                if (damage < stack.getMaxDamage()) {
                    nonnulllist.set(i, stack);
                }
            }
        }

        return nonnulllist;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return StrongerLeadsRecipeSerializer.STRONGER_LEADS_CRAFTING.get();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return super.getResultItem(pRegistries);
    }
}
