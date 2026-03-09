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
import net.minecraft.world.inventory.ContainerData;
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
    protected final ContainerData data;

    private int foodPoints = 0;
    private int eatTimer = 0;

    private static final int TICK_INTERVAL = 20;

    public PigBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PIG_BLOCK_BE.get(), pPos, pBlockState);

        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> PigBlockEntity.this.foodPoints;
                    case 1 -> PigBlockEntity.this.eatTimer;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: PigBlockEntity.this.foodPoints = value;
                    case 1: PigBlockEntity.this.eatTimer = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        Direction frontDirection = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        BlockPos frontPos = this.worldPosition.relative(frontDirection);

        BlockEntity northBlock = level.getBlockEntity(frontPos);

        if (northBlock instanceof TroughBlockEntity trough) {
            LazyOptional<IItemHandler> capability = trough.getCapability(ForgeCapabilities.ITEM_HANDLER);

            capability.ifPresent(handler -> {
                ItemStack stack = handler.getStackInSlot(0);

                if (stack.is(ModItems.VEGAN_SLURRY.get())) {
                    eatTimer++;
                    setChanged(level, blockPos, blockState);

                    if (eatTimer == TICK_INTERVAL) {
                        handler.extractItem(0, 1, false);
                        foodPoints += 5;
                        level.playSound(null, blockPos, SoundEvents.PLAYER_BURP, SoundSource.BLOCKS);
                        resetEatingProgress();
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

    private void resetEatingProgress() {
        eatTimer = 0;
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
