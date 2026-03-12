package net.Rampage.mob_block_farming.block.custom;

import com.mojang.serialization.MapCodec;
import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.block.entity.custom.MeatHarvesterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MeatHarvesterBlock extends AbstractHarvesterBlock<MeatHarvesterBlockEntity> {
    public static final MapCodec<MeatHarvesterBlock> CODEC = simpleCodec(MeatHarvesterBlock::new);

    public MeatHarvesterBlock(Properties pProperties) {
        super(pProperties, ModBlockEntities.MEAT_HARVESTER_BLOCK_BE);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MeatHarvesterBlockEntity(pPos, pState);
    }
}
