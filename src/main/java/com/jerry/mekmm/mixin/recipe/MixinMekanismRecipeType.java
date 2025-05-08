package com.jerry.mekmm.mixin.recipe;

import com.jerry.mekmm.api.recipes.MMRecipeTypes;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.common.recipe.MMRecipeType;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(value = MekanismRecipeType.class, remap = false)
public class MixinMekanismRecipeType {

    @Shadow
    private static <VANILLA_INPUT extends RecipeInput, RECIPE extends MekanismRecipe<VANILLA_INPUT>, INPUT_CACHE extends IInputRecipeCache>
    RecipeTypeRegistryObject<VANILLA_INPUT, RECIPE, INPUT_CACHE> register(
            ResourceLocation name,
            Function<MekanismRecipeType<VANILLA_INPUT, RECIPE, INPUT_CACHE>, INPUT_CACHE> inputCacheCreator) {
        return null;
    }

    @Inject(method = "<clinit>", at= @At("TAIL"))
    private static void mekmm$initRecipe(CallbackInfo ci){
        MMRecipeType.RECYCLER = register(MMRecipeTypes.NAME_RECYCLER, recipeType -> new InputRecipeCache.SingleItem<>(recipeType, RecyclerRecipe::getInput));

        MMRecipeType.PLANTING_STATION = register(MMRecipeTypes.NAME_PLANTING, recipeType -> new InputRecipeCache.ItemChemical<>(recipeType, PlantingRecipe::getItemInput, PlantingRecipe::getChemicalInput));

        MMRecipeType.STAMPING = register(MMRecipeTypes.NAME_STAMPING, recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

        MMRecipeType.LATHE = register(MMRecipeTypes.NAME_LATHE, recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));

        MMRecipeType.ROLLING_MILL = register(MMRecipeTypes.NAME_ROLLING_MILL, recipeType -> new InputRecipeCache.SingleItem<>(recipeType, ItemStackToItemStackRecipe::getInput));
    }
}
