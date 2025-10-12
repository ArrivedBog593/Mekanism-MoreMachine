package com.jerry.mekmm.common.recipe.impl;

import com.jerry.mekmm.common.recipe.MoreMachineRecipeType;
import com.jerry.mekmm.common.registries.MMBlocks;
import com.jerry.mekmm.common.registries.MMRecipeSerializers;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@NothingNullByDefault
public class RollingMillIRecipe extends ItemStackToItemStackRecipe {

    /**
     * @param id     Recipe name.
     * @param input  Input.
     * @param output Output.
     */
    public RollingMillIRecipe(ResourceLocation id, ItemStackIngredient input, ItemStack output) {
        super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return MMRecipeSerializers.ROLLING_MILL.get();
    }

    @Override
    public RecipeType<?> getType() {
        return MoreMachineRecipeType.ROLLING_MILL.get();
    }

    @Override
    public String getGroup() {
        return MMBlocks.CNC_ROLLING_MILL.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return MMBlocks.CNC_ROLLING_MILL.getItemStack();
    }
}
