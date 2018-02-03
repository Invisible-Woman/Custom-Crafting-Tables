/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.container.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class ContainerTableBase extends ContainerBase {

    public World world;
    public static int recipeSlots;
    public static int mainInventorySlots;
    public static int fullInventorySlots;

    public ContainerTableBase(TileEntity tile, int recipeSlotsIn, int mainInventorySlotsIn, int fullInventorySlotsIn) {
        world = tile.getWorld();
        recipeSlots = recipeSlotsIn;
        mainInventorySlots = mainInventorySlotsIn;
        fullInventorySlots = fullInventorySlotsIn;
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                itemstack1.getItem().onCreated(itemstack1, world, playerIn);

                if (!this.mergeItemStack(itemstack1, recipeSlots, fullInventorySlots, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= recipeSlots && index < mainInventorySlots) {
                if (!this.mergeItemStack(itemstack1, mainInventorySlots, fullInventorySlots, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= mainInventorySlots && index < fullInventorySlots) {
                if (!this.mergeItemStack(itemstack1, recipeSlots, mainInventorySlots, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, recipeSlots, fullInventorySlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    public static void onContainerClosed(EntityPlayer playerIn, boolean isRemote, int recipeSizeTotal, InventoryCraftingImproved removeItemStack) {
        if (!isRemote) {
            IntStream.range(0, recipeSizeTotal).mapToObj(removeItemStack::removeStackFromSlot).filter(itemstack ->
                    !itemstack.isEmpty()
            ).forEachOrdered(itemstack -> playerIn.dropItem(itemstack, false));
        }
    }

}
