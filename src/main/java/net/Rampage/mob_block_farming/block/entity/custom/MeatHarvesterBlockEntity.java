package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.screen.custom.MeatHarvesterMenu;
import net.Rampage.mob_block_farming.util.MobBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MeatHarvesterBlockEntity extends AbstractHarvesterBlockEntity {
    protected final ContainerData data;

    public MeatHarvesterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MEAT_HARVESTER_BLOCK_BE.get(), pPos, pBlockState);

        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {

            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.mob_block_farming.meat_harvester");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MeatHarvesterMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public int getFoodPointCost() {
        return 4;
    }

    @Override
    protected ItemStack getOutputItemStack(MobBlockType mobBlockType) {
        return switch(mobBlockType) {
            case PIG -> new ItemStack(Items.PORKCHOP, 1);
        };
    }
}
