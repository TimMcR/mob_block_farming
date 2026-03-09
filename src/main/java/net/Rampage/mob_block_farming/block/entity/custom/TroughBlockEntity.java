package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.item.custom.SlurryItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class TroughBlockEntity extends BlockEntity {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof SlurryItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public TroughBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TROUGH_BE.get(), pPos, pBlockState);
    }
}
