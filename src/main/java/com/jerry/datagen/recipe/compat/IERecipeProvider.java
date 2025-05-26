package com.jerry.datagen.recipe.compat;

import blusunrize.immersiveengineering.api.EnumMetals;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.jerry.mekmm.Mekmm;
import com.jerry.mekmm.api.datagen.recipe.builder.PlantingStationRecipeBuilder;
import com.jerry.mekmm.api.datagen.recipe.builder.StamperRecipeBuilder;
import com.jerry.mekmm.common.registries.MMChemicals;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IERecipeProvider extends CompatRecipeProvider{

    public IERecipeProvider(String modid) {
        super(modid);
    }

    @Override
    protected void registerRecipes(RecipeOutput consumer, String basePath, HolderLookup.Provider registries) {
        PlantingStationRecipeBuilder.planting(
                IngredientCreatorAccess.item().from(IEItems.Misc.HEMP_SEEDS),
                IngredientCreatorAccess.chemicalStack().from(MMChemicals.NUTRIENT_SOLUTION.asStack(1)),
                new ItemStack(IEItems.Misc.HEMP_SEEDS, 3),
                new ItemStack(IEItems.Ingredients.HEMP_FIBER),
                true
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "planting/hemp_seed"));


        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_GOLD),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_PLATE),
                new ItemStack(IEItems.Metals.PLATES.get(EnumMetals.GOLD))
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/gold_plate"));

        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_COPPER),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_PLATE),
                new ItemStack(IEItems.Metals.PLATES.get(EnumMetals.COPPER))
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/copper_plate"));

        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_IRON),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_PLATE),
                new ItemStack(IEItems.Metals.PLATES.get(EnumMetals.IRON))
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/iron_plate"));


        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_IRON),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_ROD),
                new ItemStack(IEItems.Ingredients.STICK_IRON, 2)
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/iron_stick"));

        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_NETHERITE),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_ROD),
                new ItemStack(IEItems.Ingredients.STICK_NETHERITE, 2)
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/netherite_stick"));


        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_COPPER),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_BULLET_CASING),
                new ItemStack(IEItems.Ingredients.EMPTY_SHELL, 2)
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/empty_shell"));


        StamperRecipeBuilder.stamping(
                IngredientCreatorAccess.item().from(Tags.Items.INGOTS_COPPER),
                IngredientCreatorAccess.item().from(IEItems.Molds.MOLD_WIRE),
                new ItemStack(IEItems.Ingredients.WIRE_COPPER, 2)
        ).addCondition(modLoaded).build(consumer, Mekmm.rl(basePath + "stamper/copper_wire"));
    }
}
