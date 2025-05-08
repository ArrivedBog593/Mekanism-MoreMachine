package com.jerry.mekmm.api.recipes;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.common.Mekanism;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MMRecipeTypes {

    // 由于MekanismRecipeType的限制，这里只能用Mekanism.MODID，这非常狗屎！！！
    public static final ResourceLocation NAME_RECYCLER = ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, "recycler");
    public static final ResourceLocation NAME_PLANTING = ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, "planting");
    public static final ResourceLocation NAME_STAMPING = ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, "stamping");
    public static final ResourceLocation NAME_LATHE = ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, "lathe");
    public static final ResourceLocation NAME_ROLLING_MILL = ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, "rolling_mill");

    public static final DeferredHolder<RecipeType<?>, RecipeType<RecyclerRecipe>> TYPE_RECYCLER = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_RECYCLER);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PlantingRecipe>> TYPE_PLANTING = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_PLANTING);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_STAMPING = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_STAMPING);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_LATHE = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_LATHE);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ItemStackToItemStackRecipe>> TYPE_ROLLING_MILL = DeferredHolder.create(Registries.RECIPE_TYPE, NAME_ROLLING_MILL);
}
