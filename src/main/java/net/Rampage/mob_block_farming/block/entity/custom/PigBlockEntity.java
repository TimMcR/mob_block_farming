package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class PigBlockEntity extends BlockEntity {
    private int foodPoints = 0;
    private int maxFoodPoints = 60;
    private int eatingProgress = 0;
    private static final int TICK_INTERVAL = 60;

    public PigBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PIG_BLOCK_BE.get(), pPos, pBlockState);
    }

    public int getFoodPoints() {
        return foodPoints;
    }

    public void tick(Level pLevel, BlockPos pBlockPos, BlockState pBlockState) {
        BlockEntity northBlock = getNorthFacingBlock(pLevel, pBlockState);

        if (foodPoints < maxFoodPoints && northBlock instanceof TroughBlockEntity trough) {
            LazyOptional<IItemHandler> capability = trough.getCapability(ForgeCapabilities.ITEM_HANDLER);

            capability.ifPresent(handler -> {
                ItemStack stack = handler.getStackInSlot(0);

                if (stack.is(ModItems.VEGAN_SLURRY.get())) {
                    eatingProgress++;
                    setChanged(pLevel, pBlockPos, pBlockState);

                    if (eatingProgress == TICK_INTERVAL) {
                        consumeFood(handler, pLevel, pBlockPos);
                    }
                    else if (eatingProgress % 10 == 0) {
                        pLevel.playSound(null, pBlockPos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS);
                    }
                }
                else {
                    resetEatingProgress();
                }
            });
        }
        else {
            resetEatingProgress();
        }
    }

    private void consumeFood(IItemHandler handler, Level pLevel, BlockPos pBlockPos) {
        handler.extractItem(0, 1, false);
        pLevel.playSound(null, pBlockPos, SoundEvents.PLAYER_BURP, SoundSource.BLOCKS);
        resetEatingProgress();

        foodPoints += 5;
        if (foodPoints > maxFoodPoints)
            foodPoints = maxFoodPoints;
    }

    private BlockEntity getNorthFacingBlock(Level level, BlockState blockState) {
        Direction frontDirection = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        BlockPos frontPos = this.worldPosition.relative(frontDirection);

        return level.getBlockEntity(frontPos);
    }

    private void resetEatingProgress() {
        eatingProgress = 0;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("pig_block.eating_progress", eatingProgress);
        pTag.putInt("pig_block.food_points", foodPoints);
        pTag.putInt("pig_block.max_food_points", maxFoodPoints);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        eatingProgress =  pTag.getInt("pig_block.eating_progress");
        foodPoints = pTag.getInt("pig_block.food_points");
        maxFoodPoints = pTag.getInt("pig_block.max_food_points");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
