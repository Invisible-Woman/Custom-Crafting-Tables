/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.api.crafting.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.thedragonteam.cct.api.crafting.IRecipe;
import net.thedragonteam.cct.api.crafting.utils.ShapedOreRecipeUtils;
import net.thedragonteam.cct.container.base.InventoryCraftingImproved;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static net.thedragonteam.thedragonlib.util.ItemStackUtils.getItemStack;

public class BaseShapedOreRecipe implements IRecipe {

    private int xy;
    protected ItemStack output = ItemStack.EMPTY;
    protected Object[] input = null;
    protected int width = 0;
    protected int height = 0;
    protected boolean mirrored = true;

    public BaseShapedOreRecipe(int xy, Block result, Object... recipe) {
        this(xy, new ItemStack(result), recipe);
    }

    public BaseShapedOreRecipe(int xy, Item result, Object... recipe) {
        this(xy, new ItemStack(result), recipe);
    }

    public BaseShapedOreRecipe(int xy, ItemStack result, Object... recipe) {
        this.xy = xy;
        output = result.copy();

        StringBuilder shape = new StringBuilder();
        int idx = 0;

        if (recipe[idx] instanceof Boolean) {
            mirrored = (Boolean) recipe[idx];
            if (recipe[idx + 1] instanceof Object[]) {
                recipe = (Object[]) recipe[idx + 1];
            } else {
                idx = 1;
            }
        }

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            Arrays.stream(parts).forEachOrdered(s -> {
                width = s.length();
                shape.append(s);
            });

            height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape.append(s);
                width = s.length();
                height++;
            }
        }

        ShapedOreRecipeUtils.checkRecipeShape(width, height, shape, output, recipe);

        HashMap<Character, Object> itemMap = new HashMap<>();

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack) {
                itemMap.put(chr, ((ItemStack) in).copy());
            } else if (in instanceof Item) {
                itemMap.put(chr, getItemStack(in));
            } else if (in instanceof Block) {
                itemMap.put(chr, getItemStack(in, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String) {
                itemMap.put(chr, OreDictionary.getOres((String) in));
            } else {
                StringBuilder ret = new StringBuilder("Invalid shaped ore recipe: ");
                Arrays.stream(recipe).forEachOrdered(tmp -> ret.append(tmp).append(", "));
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toString().toCharArray()) {
            input[x++] = itemMap.get(chr);
        }
    }

    BaseShapedOreRecipe(BaseShapedRecipe recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();
        width = recipe.recipeWidth;
        height = recipe.recipeHeight;

        input = new Object[recipe.getInput().size()];

        for (int i = 0; i < input.length; i++) {
            ItemStack ingredient = recipe.getInput().get(i);

            if (ingredient.isEmpty()) continue;

            input[i] = recipe.getInput().get(i);

            for (Map.Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (OreDictionary.itemMatches(replace.getKey(), ingredient, true)) {
                    input[i] = OreDictionary.getOres(replace.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCraftingImproved var1) {
        return output.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(@Nonnull InventoryCraftingImproved inv, @Nonnull World world) {
        return ShapedOreRecipeUtils.matches(xy, xy, width, height, input, inv, mirrored);
    }

    public BaseShapedOreRecipe setMirrored(boolean mirror) {
        mirrored = mirror;
        return this;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     *
     * @return The recipes input vales.
     */
    public Object[] getInput() {
        return input;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}