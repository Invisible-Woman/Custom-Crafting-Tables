package net.thedragonteam.cct.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.recipes.BrokenCraftingRecipeException;
import mezz.jei.util.ErrorUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class JEIUtils {

    public static void setRecipe(IRecipeLayout rL, IRecipeWrapper rW, IIngredients in, ICraftingGridHelper cGH, int xPos, int YPos, int height, int width, int inputSlot, int outputSlot) {
        IGuiItemStackGroup guiItemStacks = rL.getItemStacks();

        guiItemStacks.init(outputSlot, false, xPos, YPos);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int index = inputSlot + x + (y * height);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        if (rW instanceof ICustomCraftingRecipeWrapper) {
            ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) rW;
            customWrapper.setRecipe(rL, in);
            return;
        }

        List<List<ItemStack>> inputs = in.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = in.getOutputs(ItemStack.class);

        if (rW instanceof IShapedCraftingRecipeWrapper) {
            IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) rW;
            cGH.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
        } else {
            cGH.setInputs(guiItemStacks, inputs);
            rL.setShapeless();
        }
        guiItemStacks.set(outputSlot, outputs.get(0));
    }

    public static void getIngredients(IIngredients ingredients, IRecipe recipe, List<ItemStack> recipeItems) {
        ItemStack recipeOutput = recipe.getRecipeOutput();
        try {
            ingredients.setInputs(ItemStack.class, recipeItems);
            if (!recipeOutput.isEmpty()) {
                ingredients.setOutput(ItemStack.class, recipeOutput);
            }
        } catch (RuntimeException e) {
            String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(recipe, recipeItems, recipeOutput);
            throw new BrokenCraftingRecipeException(info, e);
        }
    }


    public static void getIngredients(IIngredients ingredients, IRecipe recipe, IJeiHelpers jeiHelpers, List inputItems) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        ItemStack recipeOutput = recipe.getRecipeOutput();

        try {
            List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(inputItems);
            ingredients.setInputLists(ItemStack.class, inputs);
            if (!recipeOutput.isEmpty()) {
                ingredients.setOutput(ItemStack.class, recipeOutput);
            }
        } catch (RuntimeException e) {
            String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(recipe, inputItems, recipeOutput);
            throw new BrokenCraftingRecipeException(info, e);
        }
    }
}
