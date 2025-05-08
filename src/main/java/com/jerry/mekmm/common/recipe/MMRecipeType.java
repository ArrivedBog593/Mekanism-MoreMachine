package com.jerry.mekmm.common.recipe;

import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.vanilla_input.SingleItemChemicalRecipeInput;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class MMRecipeType {

    public static RecipeTypeRegistryObject<SingleRecipeInput, RecyclerRecipe, InputRecipeCache.SingleItem<RecyclerRecipe>> RECYCLER;

    public static RecipeTypeRegistryObject<SingleItemChemicalRecipeInput, PlantingRecipe, InputRecipeCache.ItemChemical<PlantingRecipe>> PLANTING_STATION;

    public static RecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> STAMPING;

    public static RecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> LATHE;

    public static RecipeTypeRegistryObject<SingleRecipeInput, ItemStackToItemStackRecipe, InputRecipeCache.SingleItem<ItemStackToItemStackRecipe>> ROLLING_MILL;
}
