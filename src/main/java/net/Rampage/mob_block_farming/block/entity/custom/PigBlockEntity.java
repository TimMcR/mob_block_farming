package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PigBlockEntity extends AbstractMobBlockEntity {
    public PigBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PIG_BLOCK_BE.get(), pPos, pBlockState);
    }

    public String getMobBlockType() {
        return "pig";
    }
}
