package net.Rampage.mob_block_farming.util;

import net.Rampage.mob_block_farming.block.entity.custom.PigBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MachineConnector {
    public static void tryConnect(Level level, BlockPos pos) {
        BlockPos neighbor = pos.relative(Direction.NORTH);
        BlockEntity be = level.getBlockEntity(neighbor);

        if (be instanceof PigBlockEntity controller) {
            controller.addHarvester(pos);
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_COW_BELL.get(), SoundSource.BLOCKS);
        }
    }

    public static void disconnect(Level level, BlockPos pos) {
        BlockPos neighbor = pos.relative(Direction.NORTH);
        BlockEntity be = level.getBlockEntity(neighbor);

        if (be instanceof PigBlockEntity controller) {
            controller.removeHarvester(pos);
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BANJO.get(), SoundSource.BLOCKS);
        }
    }
}
