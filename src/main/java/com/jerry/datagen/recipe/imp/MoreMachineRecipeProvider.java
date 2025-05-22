package com.jerry.datagen.recipe.imp;

import com.jerry.datagen.recipe.BaseRecipeProvider;
import com.jerry.datagen.recipe.ISubRecipeProvider;
import com.jerry.datagen.recipe.pattern.Pattern;
import com.jerry.datagen.recipe.pattern.RecipePattern;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.recipe.ClearConfigurationRecipe;
import mekanism.common.registries.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@NothingNullByDefault
public class MoreMachineRecipeProvider extends BaseRecipeProvider {

    static final char DIAMOND_CHAR = 'D';
    static final char GLASS_CHAR = 'G';
    static final char PERSONAL_STORAGE_CHAR = 'P';
    static final char MIXING_CHAR = 'M';
    static final char ROBIT_CHAR = 'R';
    static final char SORTER_CHAR = 'S';
    static final char TELEPORTATION_CORE_CHAR = 'T';

    //TODO: Do we want to use same pattern for fluid tank and chemical tank at some point
    static final RecipePattern TIER_PATTERN = RecipePattern.createPattern(
          RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY),
          RecipePattern.TripleLine.of(Pattern.INGOT, Pattern.PREVIOUS, Pattern.INGOT),
          RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CIRCUIT, Pattern.ALLOY));
    static final RecipePattern STORAGE_PATTERN = RecipePattern.createPattern(
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT));
    static final RecipePattern TYPED_STORAGE_PATTERN = RecipePattern.createPattern(
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.PREVIOUS, Pattern.CONSTANT),
          RecipePattern.TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT));
    public static final RecipePattern BASIC_MODULE = RecipePattern.createPattern(
          RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.CONSTANT, Pattern.ALLOY),
          RecipePattern.TripleLine.of(Pattern.ALLOY, Pattern.PREVIOUS, Pattern.ALLOY),
          RecipePattern.TripleLine.of(Pattern.HDPE_CHAR, Pattern.HDPE_CHAR, Pattern.HDPE_CHAR));

    private final List<ISubRecipeProvider> compatProviders = new ArrayList<>();
    private final Set<String> disabledCompats = new HashSet<>();

    public MoreMachineRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, existingFileHelper);

        //Mod Compat Recipe providers
//        checkCompat("ae2", AE2RecipeProvider::new);
//        checkCompat("biomesoplenty", BiomesOPlentyRecipeProvider::new);
//        checkCompat("biomeswevegone", BWGRecipeProvider::new);
//        checkCompat("farmersdelight", FarmersDelightRecipeProvider::new);
    }

    private void checkCompat(String modid, Function<String, ISubRecipeProvider> providerCreator) {
//        if (ModList.get().isLoaded(modid)) {
//            compatProviders.add(providerCreator.apply(modid));
//        } else {
//            disabledCompats.add(modid);
//        }
    }

    public Set<String> getDisabledCompats() {
        return Collections.unmodifiableSet(disabledCompats);
    }

    @Override
    protected void addRecipes(RecipeOutput consumer, HolderLookup.Provider registries) {
        addMiscRecipes(consumer);
        addGearModuleRecipes(consumer);
        addLateGameRecipes(consumer);
        for (ISubRecipeProvider compatProvider : compatProviders) {
            compatProvider.addRecipes(consumer, registries);
        }
    }

    @Override
    protected List<ISubRecipeProvider> getSubRecipeProviders() {
        return List.of(
                new AdvancedFactoryRecipeProvider()
        );
    }

    private void addMiscRecipes(RecipeOutput consumer) {
        SpecialRecipeBuilder.special(ClearConfigurationRecipe::new).save(consumer, MekanismRecipeSerializersInternal.CLEAR_CONFIGURATION.getId());
    }

    private void addGearModuleRecipes(RecipeOutput consumer) {

    }

    private void addLateGameRecipes(RecipeOutput consumer) {
    }
}