package net.thedragonteam.cct.compat.jei.base;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.thedragonteam.cct.api.crafting.IRecipe;
import net.thedragonteam.cct.compat.jei.JEIUtils;

import java.util.Arrays;

public class ShapedOreRecipeWrapper implements IShapedCraftingRecipeWrapper {

    private final IJeiHelpers jeiHelpers;
    private final IRecipe recipe;
    private final Object[] inputItems;
    private final int width;
    private final int height;

    public ShapedOreRecipeWrapper(IJeiHelpers jeiHelpers, IRecipe recipe, Object[] inputItems, int width, int height) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
        this.inputItems = inputItems;
        this.width = width;
        this.height = height;
        Arrays.stream(inputItems).filter(input ->
                input instanceof ItemStack
        ).map(input -> (ItemStack) input).filter(itemStack ->
                !itemStack.isEmpty() && itemStack.getCount() != 1
        ).forEachOrdered(itemStack -> itemStack.setCount(1));
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        JEIUtils.getIngredients(ingredients, recipe, jeiHelpers, Arrays.asList(inputItems));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

}