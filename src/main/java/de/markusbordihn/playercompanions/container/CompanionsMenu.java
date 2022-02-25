/**
 * Copyright 2022 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.playercompanions.container;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import de.markusbordihn.playercompanions.Constants;
import de.markusbordihn.playercompanions.container.slots.DummySlot;
import de.markusbordihn.playercompanions.container.slots.InventorySlot;
import de.markusbordihn.playercompanions.data.PlayerCompanionData;
import de.markusbordihn.playercompanions.data.PlayerCompanionsClientData;
import de.markusbordihn.playercompanions.data.PlayerCompanionsServerData;
import de.markusbordihn.playercompanions.entity.type.PlayerCompanionType;

public class CompanionsMenu extends AbstractContainerMenu {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  // Defining basic layout options
  private static int inventoryContainerSize = 16;
  private static int equipmentContainerSize = 8;
  private static int slotSize = 18;

  // Define containers
  private final Container inventoryContainer;
  private final Container equipmentContainer;
  private final Player player;
  private final UUID playerCompanionUUID;
  private final PlayerCompanionData playerCompanionData;
  private final Level level;

  // Define states
  private boolean dataLoaded = false;

  public CompanionsMenu(int windowId, Inventory inventory, UUID playerCompanionUUID) {
    this(windowId, inventory, new SimpleContainer(inventoryContainerSize),
        new SimpleContainer(equipmentContainerSize), playerCompanionUUID);
  }

  public CompanionsMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
    this(windowId, playerInventory, new SimpleContainer(inventoryContainerSize),
        new SimpleContainer(equipmentContainerSize), data.readUUID());
  }

  public CompanionsMenu(final int windowId, final Inventory playerInventory,
      final Container inventoryContainer, final Container equipmentContainer,
      UUID playerCompanionUUID) {
    super(ModContainer.COMPANIONS_MENU.get(), windowId);

    // Make sure the passed container matched the expected sizes
    checkContainerSize(inventoryContainer, inventoryContainerSize);
    checkContainerSize(equipmentContainer, equipmentContainerSize);

    this.dataLoaded = false;
    this.player = playerInventory.player;
    this.inventoryContainer = inventoryContainer;
    this.equipmentContainer = equipmentContainer;
    this.playerCompanionUUID = playerCompanionUUID;
    this.level = this.player.getLevel();

    if (this.level.isClientSide) {
      this.playerCompanionData = PlayerCompanionsClientData.getCompanion(this.playerCompanionUUID);
    } else {
      this.playerCompanionData =
          PlayerCompanionsServerData.get().getCompanion(this.playerCompanionUUID);
    }
    if (this.playerCompanionData == null) {
      log.error("Unable to find Player Companion Data for {} on Client:{}", playerCompanionUUID,
          this.level.isClientSide);
      return;
    }
    PlayerCompanionType companionType = this.playerCompanionData.getType();

    log.debug("CompanionsMenu client:{} companion:{} data:{}", this.level.isClientSide,
        this.playerCompanionUUID, this.playerCompanionData);

    // Player Companion Equipment Slots (left / slot: 0 - 3)
    int playerCompanionEquipmentLeftStartPositionY = 21;
    int playerCompanionEquipmentLeftStartPositionX = 8;
    for (int inventoryRow = 0; inventoryRow < 4; ++inventoryRow) {
      this.addSlot(new DummySlot(this.equipmentContainer, inventoryRow,
          playerCompanionEquipmentLeftStartPositionX,
          playerCompanionEquipmentLeftStartPositionY + inventoryRow * slotSize));
    }

    // Player Companion Equipment Slots (right / slot: 4 - 7)
    int playerCompanionEquipmentRightStartPositionY = 21;
    int playerCompanionEquipmentRightStartPositionX = 77;
    for (int inventoryRow = 0; inventoryRow < 4; ++inventoryRow) {
      this.addSlot(new DummySlot(this.equipmentContainer, inventoryRow + 4,
          playerCompanionEquipmentRightStartPositionX,
          playerCompanionEquipmentRightStartPositionY + inventoryRow * slotSize));
    }

    if (companionType == PlayerCompanionType.COLLECTOR) {
      // Player Companion Inventory Slots
      loadInventory();
      int playerCompanionInventoryStartPositionY = 21;
      int playerCompanionInventoryStartPositionX = 98;
      for (int inventoryRow = 0; inventoryRow < 4; ++inventoryRow) {
        for (int inventoryColumn = 0; inventoryColumn < 4; ++inventoryColumn) {
          this.addSlot(
              new InventorySlot(this, this.inventoryContainer, inventoryRow + inventoryColumn * 4,
                  playerCompanionInventoryStartPositionX + inventoryColumn * slotSize,
                  playerCompanionInventoryStartPositionY + inventoryRow * slotSize));
        }
      }
    }

    // Player Inventory Slots
    int playerInventoryStartPositionY = 110;
    int playerInventoryStartPositionX = 8;
    for (int inventoryRow = 0; inventoryRow < 3; ++inventoryRow) {
      for (int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
        this.addSlot(new Slot(playerInventory, inventoryColumn + inventoryRow * 9 + 9,
            playerInventoryStartPositionX + inventoryColumn * slotSize,
            playerInventoryStartPositionY + inventoryRow * slotSize));
      }
    }

    // Player Hotbar Slots
    int hotbarStartPositionY = 168;
    int hotbarStartPositionX = 8;
    for (int playerInventorySlot = 0; playerInventorySlot < 9; ++playerInventorySlot) {
      this.addSlot(new Slot(playerInventory, playerInventorySlot,
          hotbarStartPositionX + playerInventorySlot * slotSize, hotbarStartPositionY));
    }

    if (!this.level.isClientSide) {
      this.dataLoaded = true;
    }
  }

  public void loadInventory() {
    if (this.playerCompanionData == null) {
      return;
    }
    NonNullList<ItemStack> inventory = this.playerCompanionData.getInventory();
    for (int index = 0; index < inventory.size(); index++) {
      this.inventoryContainer.setItem(index, inventory.get(index));
    }
  }

  public void saveInventory() {
    if (this.playerCompanionData == null) {
      return;
    }
    for (int index = 0; index < inventoryContainerSize; index++) {
      this.playerCompanionData.setInventoryItem(index, this.inventoryContainer.getItem(index));
    }
  }

  public void setChanged(int slot, ItemStack itemStack) {
    if (this.dataLoaded && this.playerCompanionData != null) {
      this.playerCompanionData.setInventoryItem(slot, itemStack);
    }
  }

  public Entity getPlayerCompanionEntity() {
    if (this.playerCompanionData == null) {
      return null;
    }
    return this.level.getEntity(this.playerCompanionData.getEntityId());
  }

  @Override
  public ItemStack quickMoveStack(Player player, int slotIndex) {
    Slot slot = this.slots.get(slotIndex);
    if (!slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemStack = slot.getItem();

    // Store changes if itemStack is not empty.
    if (itemStack.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.inventoryContainer.stillValid(player);
  }

}
