package com.jerry.mekmm.common.recipe.serializer;

import com.jerry.mekmm.api.recipes.StamperRecipe;
import com.jerry.mekmm.api.recipes.basic.BasicStamperRecipe;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.SerializationConstants;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.serializer.MekanismRecipeSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public record MoreMachineRecipeSerializer<RECIPE extends Recipe<?>>(MapCodec<RECIPE> codec, StreamCodec<RegistryFriendlyByteBuf, RECIPE> streamCodec)
        implements RecipeSerializer<RECIPE> {

    public static MekanismRecipeSerializer<BasicStamperRecipe> stamping(Function3<ItemStackIngredient, ItemStackIngredient, ItemStack, BasicStamperRecipe> factory) {
        return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(StamperRecipe::getMainInput),
                ItemStackIngredient.CODEC.fieldOf("mold").forGetter(StamperRecipe::getExtraInput),
                ItemStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicStamperRecipe::getOutputRaw)
        ).apply(instance, factory)), StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, BasicStamperRecipe::getMainInput,
                ItemStackIngredient.STREAM_CODEC, BasicStamperRecipe::getExtraInput,
                ItemStack.STREAM_CODEC, BasicStamperRecipe::getOutputRaw,
                factory
        ));
    }
}
