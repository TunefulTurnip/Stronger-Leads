package com.tunefulturnip.strongerleads.compat.jei;

import com.tunefulturnip.strongerleads.StrongerLeadsMod;
import com.tunefulturnip.strongerleads.item.StrongerLeadsItems;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class StrongerLeadsJEIPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(StrongerLeadsMod.MODID, "jei_plugin");

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        // Lead + String = Longer Lead (length +1)
        recipes.add(createRecipe("lead_plus_string",
                createLongerLead(0, 1),
                Ingredient.of(Items.LEAD),
                Ingredient.of(Items.STRING)));

        // Lead + 2 String = Longer Lead (length +2)
        recipes.add(createRecipe("lead_plus_2_string",
                createLongerLead(0, 2),
                Ingredient.of(Items.LEAD),
                Ingredient.of(Items.STRING),
                Ingredient.of(Items.STRING)));

        // Lead + Iron Ingot = Reinforced Lead (strength +1)
        recipes.add(createRecipe("lead_plus_iron",
                createLongerLead(1, 0),
                Ingredient.of(Items.LEAD),
                Ingredient.of(Items.IRON_INGOT)));

        // Lead + String + Iron = Reinforced Longer Lead
        recipes.add(createRecipe("lead_plus_string_plus_iron",
                createLongerLead(1, 1),
                Ingredient.of(Items.LEAD),
                Ingredient.of(Items.STRING),
                Ingredient.of(Items.IRON_INGOT)));

        // Longer Lead + Shears = Shorter Lead (demonstrates shortening)
        ItemStack longerLeadInput = createLongerLead(0, 2);
        recipes.add(createRecipe("longer_lead_plus_shears",
                createLongerLead(0, 1),
                Ingredient.of(longerLeadInput),
                Ingredient.of(Items.SHEARS)));

        registration.addRecipes(RecipeTypes.CRAFTING, recipes);
    }

    private ItemStack createLongerLead(int strength, int length) {
        ItemStack stack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get());
        stack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord((byte) strength, (byte) length));
        return stack;
    }

    private RecipeHolder<CraftingRecipe> createRecipe(String name, ItemStack output, Ingredient... inputs) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (Ingredient input : inputs) {
            ingredients.add(input);
        }

        ShapelessRecipe recipe = new ShapelessRecipe(
                StrongerLeadsMod.MODID,
                CraftingBookCategory.MISC,
                output,
                ingredients
        );

        return new RecipeHolder<>(
                ResourceLocation.fromNamespaceAndPath(StrongerLeadsMod.MODID, "jei/" + name),
                recipe
        );
    }
}
