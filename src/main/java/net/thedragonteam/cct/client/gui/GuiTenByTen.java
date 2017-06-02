/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.cct.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.thedragonteam.cct.client.gui.base.GuiBaseBench;
import net.thedragonteam.cct.container.ContainerTenByTen;
import net.thedragonteam.cct.tileentity.TileEntityTenByTen;

import java.util.Objects;

import static net.thedragonteam.cct.CCTConfig.cctGuiNames;

public class GuiTenByTen extends GuiBaseBench {

    public GuiTenByTen(InventoryPlayer playerInv, TileEntityTenByTen tile) {
        super(new ContainerTenByTen(playerInv, tile),"10x10", 256, 256, 9);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(cctGuiNames[9] != null && !Objects.equals(cctGuiNames[9], "") && !Objects.equals(cctGuiNames[9], " ") ? cctGuiNames[9] : new TextComponentTranslation("container.cct.10x10").getFormattedText(), 28, 5, 4210752);
    }
}
