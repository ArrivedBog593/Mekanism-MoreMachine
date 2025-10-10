package com.jerry.mekmm.common.registries;

import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.recipes.PlantingRecipe;
import com.jerry.mekmm.api.recipes.RecyclerRecipe;
import com.jerry.mekmm.common.recipe.impl.PlantingIRecipe;
import com.jerry.mekmm.common.recipe.impl.RecyclerIRecipe;
import com.jerry.mekmm.common.recipe.serializer.PlantingRecipeSerializer;
import com.jerry.mekmm.common.recipe.serializer.RecyclerRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class MMRecipeSerializers {

    private MMRecipeSerializers() {
    }

    public static final RecipeSerializerDeferredRegister MM_RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(Mekmm.MOD_ID);

    public static final RecipeSerializerRegistryObject<RecyclerRecipe> RECYCLER = MM_RECIPE_SERIALIZERS.register("recycling", () -> new RecyclerRecipeSerializer<>(RecyclerIRecipe::new));
    public static final RecipeSerializerRegistryObject<PlantingRecipe> PLANTING = MM_RECIPE_SERIALIZERS.register("planting", () -> new PlantingRecipeSerializer<>(PlantingIRecipe::new));

}
