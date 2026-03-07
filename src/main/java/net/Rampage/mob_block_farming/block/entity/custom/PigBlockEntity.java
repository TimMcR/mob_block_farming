package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PigBlockEntity extends BlockEntity {
    private int foodPoints = 0;
    private int eatTimer = 0;

    public PigBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PIG_BLOCK_BE.get(), pPos, pBlockState);
    }
}
