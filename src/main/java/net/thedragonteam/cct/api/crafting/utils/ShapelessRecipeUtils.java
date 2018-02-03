package net.thedragonteam.cct.api.crafting.utils;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import net.thedragonteam.cct.container.base.InventoryCraftingImproved;

import java.util.List;

public class ShapelessRecipeUtils {

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public static boolean matches(NonNullList<ItemStack> input, InventoryCraftingImproved inv) {
        List<ItemStack> list = Lists.newArrayList(input);

        for (int posY = 0; posY < inv.getHeight(); ++posY) {
            for (int posX = 0; posX < inv.getWidth(); ++posX) {
                ItemStack itemstack = inv.getStackInRowAndColumn(posX, posY);

                if (!itemstack.isEmpty()) {
                    boolean flag = false;

                    for (ItemStack itemstack1 : list) {
                        if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getMetadata() == OreDictionary.WILDCARD_VALUE || itemstack.getMetadata() == itemstack1.getMetadata())) {
                            flag = true;
                            list.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag) return false;
                }
            }
        }

        return list.isEmpty();
    }
}
