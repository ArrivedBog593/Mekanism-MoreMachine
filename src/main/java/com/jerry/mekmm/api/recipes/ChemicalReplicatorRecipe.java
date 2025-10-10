package com.jerry.mekmm.api.recipes;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.ChemicalType;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public abstract class ChemicalReplicatorRecipe extends MekanismRecipe implements BiPredicate<GasStack, BoxedChemicalStack> {

    private final ChemicalType chemicalType;
    private final GasStackIngredient firstInput;
    private final ChemicalStackIngredient<?, ?> secondaryInput;
    private final BoxedChemicalStack output;

    /**
     * @param id Recipe name.
     */
    protected ChemicalReplicatorRecipe(ResourceLocation id, GasStackIngredient firstInput, ChemicalStackIngredient<?, ?> secondaryInput, BoxedChemicalStack output) {
        super(id);
        this.firstInput = Objects.requireNonNull(firstInput, "First Input cannot be null.");
        this.secondaryInput = Objects.requireNonNull(secondaryInput, "Secondary Input cannot be null.");
        this.chemicalType = ChemicalType.getTypeFor(secondaryInput);
        Objects.requireNonNull(output, "Output cannot be null.");
        if (output.isEmpty()) {
            throw new IllegalArgumentException("Output cannot be empty.");
        }
        this.output = output.copy();
    }

    /**
     * Gets the output based on the given input.
     *
     * @param firstInput     Specific input.
     * @param secondaryInput Specific input.
     * @return Output as a constant.
     * @apiNote While Mekanism does not currently make use of the input, it is important to support it and pass the proper value in case any addons define input based
     * outputs where things like NBT may be different.
     * @implNote The passed in input should <strong>NOT</strong> be modified.
     */
    @Contract(value = "_, _ -> new", pure = true)
    public BoxedChemicalStack getOutput(GasStack firstInput, BoxedChemicalStack secondaryInput) {
        return output.copy();
    }

    /**
     * For JEI, gets the output representations to display.
     *
     * @return Representation of the output, <strong>MUST NOT</strong> be modified.
     */
    public List<BoxedChemicalStack> getOutputDefinition() {
        return Collections.singletonList(output);
    }

    @Override
    public boolean test(GasStack gasStack, BoxedChemicalStack chemicalStack) {
        return chemicalType == chemicalStack.getChemicalType() && testInternal(chemicalStack.getChemicalStack()) && firstInput.test(gasStack);
    }

    /**
     * Helper to test this recipe against a chemical stack without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack matches the input.
     *
     * @apiNote See {@link #test(GasStack, BoxedChemicalStack)}.
     */
    public boolean test(ChemicalStack<?> stack) {
        return chemicalType == ChemicalType.getTypeFor(stack) && testInternal(stack);
    }

    /**
     * Helper to test this recipe against a chemical stack's type without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack's type matches the input.
     *
     * @apiNote See {@link #testType(BoxedChemicalStack)}.
     */
    public boolean testType(ChemicalStack<?> stack) {
        return chemicalType == ChemicalType.getTypeFor(stack) && testTypeInternal(stack);
    }

    /**
     * Helper to test this recipe against a chemical stack's type without having to first box it up.
     *
     * @param stack Input stack.
     *
     * @return {@code true} if the stack's type matches the input.
     */
    public boolean testType(BoxedChemicalStack stack) {
        return chemicalType == stack.getChemicalType() && testTypeInternal(stack.getChemicalStack());
    }

    @SuppressWarnings("unchecked")
    private <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> boolean testInternal(STACK stack) {
        return ((ChemicalStackIngredient<CHEMICAL, STACK>) secondaryInput).test(stack);
    }

    @SuppressWarnings("unchecked")
    private <CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> boolean testTypeInternal(STACK stack) {
        return ((ChemicalStackIngredient<CHEMICAL, STACK>) secondaryInput).testType(stack);
    }

    /**
     * Gets the input gas ingredient.
     */
    public GasStackIngredient getFirstInput() {
        return firstInput;
    }

    /**
     * Gets the input chemical ingredient.
     */
    public ChemicalStackIngredient<?, ?> getSecondaryInput() {
        return secondaryInput;
    }

    @Override
    public boolean isIncomplete() {
        return firstInput.hasNoMatchingInstances() || secondaryInput.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        firstInput.logMissingTags();
        secondaryInput.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(chemicalType);
        firstInput.write(buffer);
        secondaryInput.write(buffer);
        output.getChemicalStack().writeToPacket(buffer);
    }
}
