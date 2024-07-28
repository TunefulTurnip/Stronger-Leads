package com.tunefulturnip.strongerleads.recipe;

import com.tunefulturnip.strongerleads.StrongerLeadsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StrongerLeadsRecipeSerializer {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, StrongerLeadsMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StrongerLeadCrafting>> STRONGER_LEADS_CRAFTING = RECIPE_SERIALIZERS.register("stronger_leads_crafting", () ->
            new SimpleCraftingRecipeSerializer<>(StrongerLeadCrafting::new)
    );
}
