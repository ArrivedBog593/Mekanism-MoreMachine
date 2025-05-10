package com.jerry.mekmm.api.recipes.cache;

import com.jerry.mekmm.api.recipes.basic.MMBasicItemStackChemicalToItemStackRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipeHelper;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.*;

@NothingNullByDefault
public class ReplicatorCachedRecipe extends CachedRecipe<MMBasicItemStackChemicalToItemStackRecipe> {

    private final IInputHandler<ItemStack> inputHandler;
    private final IInputHandler<ChemicalStack> secondaryInputHandler;
    private final IOutputHandler<ItemStack> outputHandler;
    private final Predicate<ItemStack> inputEmptyCheck;
    private final Predicate<ChemicalStack> secondaryInputEmptyCheck;
    private final Supplier<? extends InputIngredient<ItemStack>> inputSupplier;
    private final Supplier<? extends InputIngredient<ChemicalStack>> secondaryInputSupplier;
    private final BiFunction<ItemStack, ChemicalStack, ItemStack> outputGetter;
    private final Predicate<ItemStack> outputEmptyCheck;
    private final BiConsumer<ItemStack, ChemicalStack> inputsSetter;
    private final Consumer<ItemStack> outputSetter;

    //Note: Our inputs and outputs shouldn't be null in places they are actually used, but we mark them as nullable, so we don't have to initialize them
    @Nullable
    private ItemStack input;
    @Nullable
    private ChemicalStack secondaryInput;
    @Nullable
    private ItemStack output;

    /**
     * @param recipe           Recipe.
     * @param recheckAllErrors Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
     *                         do this every tick or if there is no one viewing recipes.
     */
    private ReplicatorCachedRecipe(MMBasicItemStackChemicalToItemStackRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<ItemStack> inputHandler, IInputHandler<ChemicalStack> secondaryInputHandler,
                                     IOutputHandler<ItemStack> outputHandler, Supplier<InputIngredient<ItemStack>> inputSupplier, Supplier<InputIngredient<ChemicalStack>> secondaryInputSupplier,
                                     BiFunction<ItemStack, ChemicalStack, ItemStack> outputGetter, Predicate<ItemStack> inputEmptyCheck, Predicate<ChemicalStack> secondaryInputEmptyCheck,
                                     Predicate<ItemStack> outputEmptyCheck) {
        super(recipe, recheckAllErrors);
        this.inputHandler = Objects.requireNonNull(inputHandler, "Input handler cannot be null.");
        this.secondaryInputHandler = Objects.requireNonNull(secondaryInputHandler, "Secondary input handler cannot be null.");
        this.outputHandler = Objects.requireNonNull(outputHandler, "Output handler cannot be null.");
        this.inputSupplier = Objects.requireNonNull(inputSupplier, "Input ingredient supplier cannot be null.");
        this.secondaryInputSupplier = Objects.requireNonNull(secondaryInputSupplier, "Secondary input ingredient supplier cannot be null.");
        this.outputGetter = Objects.requireNonNull(outputGetter, "Output getter cannot be null.");
        this.inputEmptyCheck = Objects.requireNonNull(inputEmptyCheck, "Input empty check cannot be null.");
        this.secondaryInputEmptyCheck = Objects.requireNonNull(secondaryInputEmptyCheck, "Secondary input empty check cannot be null.");
        this.outputEmptyCheck = Objects.requireNonNull(outputEmptyCheck, "Output empty check cannot be null.");
        this.inputsSetter = (input, secondary) -> {
            this.input = input;
            this.secondaryInput = secondary;
        };
        this.outputSetter = output -> this.output = output;
    }

    public static ReplicatorCachedRecipe createCache(MMBasicItemStackChemicalToItemStackRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> itemInputHandler,
                                                     IInputHandler<@NotNull ChemicalStack> chemicalInputHandler, IOutputHandler<@NotNull ItemStack> outputHandler) {
        return new ReplicatorCachedRecipe(recipe, recheckAllErrors, itemInputHandler, chemicalInputHandler, outputHandler, recipe::getItemInput, recipe::getChemicalInput,
                recipe::getOutput, ConstantPredicates.ITEM_EMPTY, ConstantPredicates.CHEMICAL_EMPTY, ConstantPredicates.ITEM_EMPTY);
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        CachedRecipeHelper.twoInputCalculateOperationsThisTick(tracker, inputHandler, inputSupplier, secondaryInputHandler, secondaryInputSupplier, inputsSetter,
                outputHandler, outputGetter, outputSetter, inputEmptyCheck, secondaryInputEmptyCheck);
    }

    @Override
    public boolean isInputValid() {
        ItemStack input = inputHandler.getInput();
        if (inputEmptyCheck.test(input)) {
            return false;
        }
        ChemicalStack secondaryInput = secondaryInputHandler.getInput();
        return !secondaryInputEmptyCheck.test(secondaryInput) && recipe.test(input, secondaryInput);
    }

    @Override
    protected void finishProcessing(int operations) {
        //Validate something didn't go horribly wrong
        if (input != null && secondaryInput != null && output != null && !inputEmptyCheck.test(input) && !secondaryInputEmptyCheck.test(secondaryInput) &&
                !outputEmptyCheck.test(output)) {
//            inputHandler.use(input, operations);
            secondaryInputHandler.use(secondaryInput, operations);
            outputHandler.handleOutput(output, operations);
        }
    }
}
