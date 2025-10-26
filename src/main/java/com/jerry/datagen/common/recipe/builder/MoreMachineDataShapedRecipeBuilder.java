package com.jerry.datagen.common.recipe.builder;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.recipe.upgrade.MekanismShapedRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;

@NothingNullByDefault
public class MoreMachineDataShapedRecipeBuilder extends ExtendedShapedRecipeBuilder {

    private MoreMachineDataShapedRecipeBuilder(ItemLike result, int count) {
        super(result, count);
    }

    public static MoreMachineDataShapedRecipeBuilder shapedRecipe(ItemLike result) {
        return shapedRecipe(result, 1);
    }

    public static MoreMachineDataShapedRecipeBuilder shapedRecipe(ItemLike result, int count) {
        return new MoreMachineDataShapedRecipeBuilder(result, count);
    }

    @Override
    protected Recipe<?> wrapRecipe(ShapedRecipe recipe) {
        return new MekanismShapedRecipe(recipe);
    }
}