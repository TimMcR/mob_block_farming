package net.Rampage.mob_block_farming.block.custom;

import com.mojang.serialization.MapCodec;
import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.block.entity.custom.PigBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PigBlock extends AbstractMobBlock<PigBlockEntity> {
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PigBlockEntity(pos, state);
    }

    public PigBlock(Properties pProperties) {
        super(pProperties, ModBlockEntities.PIG_BLOCK_BE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}
