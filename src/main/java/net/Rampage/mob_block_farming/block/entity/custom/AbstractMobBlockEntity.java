package net.Rampage.mob_block_farming.block.entity.custom;

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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMobBlockEntity extends BlockEntity {
    protected int foodPoints = 0;
    protected int maxFoodPoints = 60;
    protected int eatingProgress = 0;

    protected int speedMultiplier = 1;
    private static final int BASE_TICK_INTERVAL = 60;

    public AbstractMobBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public abstract String getMobBlockType();

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
        pTag.putInt("mob_block.eating_progress", eatingProgress);
        pTag.putInt("mob_block.food_points", foodPoints);
        pTag.putInt("mob_block.max_food_points", maxFoodPoints);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        eatingProgress = pTag.getInt("mob_block.eating_progress");
        foodPoints = pTag.getInt("mob_block.food_points");
        maxFoodPoints = pTag.getInt("mob_block.max_food_points");
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
                    eatingProgress += speedMultiplier;
                    setChanged(pLevel, pBlockPos, pBlockState);

                    if (eatingProgress == BASE_TICK_INTERVAL) {
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

        foodPoints += 20;
        if (foodPoints > maxFoodPoints)
            foodPoints = maxFoodPoints;
    }

    public boolean subtractFoodPoints(int cost) {
        if (cost <= foodPoints)
        {
            foodPoints -= cost;
            setChanged();
            return true;
        }

        return false;
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
