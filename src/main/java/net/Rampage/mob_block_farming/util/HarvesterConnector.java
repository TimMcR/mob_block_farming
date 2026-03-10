package net.Rampage.mob_block_farming.util;

import net.Rampage.mob_block_farming.block.custom.MeatHarvesterBlock;
import net.Rampage.mob_block_farming.block.entity.custom.PigBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HarvesterConnector {

    public static void tryConnect(Level level, BlockPos pos, BlockState state) {
        if (!state.hasProperty(MeatHarvesterBlock.FACING)) {
            return;
        }

        Direction facing = state.getValue(MeatHarvesterBlock.FACING);

        BlockPos neighbor = pos.relative(facing);
        BlockEntity be = level.getBlockEntity(neighbor);

        if (be instanceof PigBlockEntity controller) {
            controller.addHarvester(pos);
        }
    }

    public static void disconnect(Level level, BlockPos pos, BlockState state) {
        if (!state.hasProperty(MeatHarvesterBlock.FACING)) {
            return;
        }

        Direction facing = state.getValue(MeatHarvesterBlock.FACING);

        BlockPos neighbor = pos.relative(facing);
        BlockEntity be = level.getBlockEntity(neighbor);

        if (be instanceof PigBlockEntity controller) {
            controller.removeHarvester(pos);
        }
    }
}
