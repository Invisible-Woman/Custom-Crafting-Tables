/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.tileentity.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public abstract class TileEntityBase extends TileEntity {


    public TileEntityBase() {
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        IItemHandler handler = this.getItemHandler(facing);
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && handler != null || super.hasCapability(capability, facing));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            IItemHandler handler = this.getItemHandler(facing);
            if (handler != null) return (T) handler;
        }
        return super.getCapability(capability, facing);
    }

    public IItemHandler getItemHandler(EnumFacing facing) {
        return null;
    }
}
