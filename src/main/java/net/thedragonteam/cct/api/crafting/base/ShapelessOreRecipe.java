/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.api.crafting.base;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.thedragonteam.thedragonlib.util.ItemStackUtils.getItemStack;

public class ShapelessOreRecipe implements IRecipe {
    public ItemStack output = ItemStack.EMPTY;
    public NonNullList<Object> input = NonNullList.create();

    public ShapelessOreRecipe(Block result, Object... recipe) {
        this(getItemStack(result), recipe);
    }

    public ShapelessOreRecipe(Item result, Object... recipe) {
        this(getItemStack(result), recipe);
    }

    public ShapelessOreRecipe(ItemStack result, Object... recipe) {
        output = result.copy();
        for (Object in : recipe) {
            if (in instanceof ItemStack) {
                input.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                input.add(getItemStack((Item) in));
            } else if (in instanceof Block) {
                input.add(getItemStack((Block) in));
            } else if (in instanceof String) {
                input.add(OreDictionary.getOres((String) in));
            } else {
                StringBuilder ret = new StringBuilder("Invalid shapeless ore recipe: ");
                for (Object tmp : recipe) {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
    }

    ShapelessOreRecipe(ShapelessRecipe recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();

        for (ItemStack ingredient : recipe.input) {
            Object finalObj = ingredient;
            for (Map.Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (OreDictionary.itemMatches(replace.getKey(), ingredient, false)) {
                    finalObj = OreDictionary.getOres(replace.getValue());
                    break;
                }
            }
            input.add(finalObj);
        }
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize() {
        return input.size();
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return output;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
        return output.copy();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World world) {
        NonNullList<Object> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty()) {
                boolean inRecipe = false;

                for (Object aRequired : required) {
                    boolean match = false;

                    if (aRequired instanceof ItemStack) {
                        match = OreDictionary.itemMatches((ItemStack) aRequired, slot, false);
                    } else if (aRequired instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) aRequired).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(aRequired);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     *
     * @return The recipes input vales.
     */
    public NonNullList<Object> getInput() {
        return this.input;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
