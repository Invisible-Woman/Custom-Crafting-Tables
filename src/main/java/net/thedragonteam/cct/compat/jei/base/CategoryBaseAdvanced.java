/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.compat.jei.base;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CategoryBaseAdvanced extends CategoryBase {

    private final int buttonX;
    private final int buttonY;

    public CategoryBaseAdvanced(int u, int v, int widthU, int heightV, int outputXPos, int outputYPos, int xy, String category, int buttonX, int buttonY) {
        super(u, v, widthU, heightV, outputXPos, outputYPos, xy, category);
        this.buttonX = buttonX;
        this.buttonY = buttonY;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        super.setRecipe(recipeLayout, recipeWrapper, ingredients);
        recipeLayout.setRecipeTransferButton(buttonX, buttonY);
    }
}
