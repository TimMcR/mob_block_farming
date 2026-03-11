package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.item.ModItems;
import net.Rampage.mob_block_farming.util.IHarvester;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

import java.util.*;

public class PigBlockEntity extends BlockEntity {
    private int foodPoints = 0;
    private int maxFoodPoints = 60;
    private int eatingProgress = 0;

    private int speedMultiplier = 1;
    private static final int BASE_TICK_INTERVAL = 60;

    private final Set<BlockPos> connectedHarvesters = new HashSet<>();

    public PigBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PIG_BLOCK_BE.get(), pPos, pBlockState);
    }

    public void tick(Level pLevel, BlockPos pBlockPos, BlockState pBlockState) {
        BlockEntity northBlock = getNorthFacingBlock(pLevel, pBlockState);

        if (foodPoints > 0) {
            outputToConnectedHarvesters(pLevel);
        }
        else {
            resetConnectedHarvesters(pLevel);
        }

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

    private void resetConnectedHarvesters(Level pLevel) {
        List<IHarvester> harvesters = getConnectedHarvesters(pLevel);

        for (IHarvester harvester : harvesters) {
            harvester.resetProgress();
        }
    }

    private void outputToConnectedHarvesters(Level pLevel) {
        List<IHarvester> harvesters = getConnectedHarvesters(pLevel);

        // Sort by highest food point cost
        harvesters.sort(Comparator.comparingInt(IHarvester::getFoodPointCost).reversed());

        for (IHarvester harvester : harvesters) {
            produceHarvesterOutput(harvester);
            if(foodPoints == 0)
                break;
        }
    }

    private List<IHarvester> getConnectedHarvesters(Level pLevel) {
        Iterator<BlockPos> iterator = connectedHarvesters.iterator();

        List<IHarvester> harvesters = new ArrayList<>();

        boolean shouldRemove = false;

        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();

            BlockEntity blockEntity = pLevel.getBlockEntity(pos);

            if(blockEntity instanceof IHarvester harvester)
            {
                harvesters.add(harvester);
            }
            else {
                iterator.remove();
                shouldRemove = true;
            }
        }

        if (shouldRemove) {
            setChanged();
        }

        return harvesters;
    }

    private void produceHarvesterOutput(IHarvester harvester) {
        int cost = harvester.getFoodPointCost();

        if (foodPoints < cost)
            return;

        var canAccept = harvester.acceptHarvesterOutput("PIG");
        if (!canAccept)
            return;

        foodPoints -= cost;
        setChanged();
    }

    private void consumeFood(IItemHandler handler, Level pLevel, BlockPos pBlockPos) {
        handler.extractItem(0, 1, false);
        pLevel.playSound(null, pBlockPos, SoundEvents.PLAYER_BURP, SoundSource.BLOCKS);
        resetEatingProgress();

        foodPoints += 5;
        if (foodPoints > maxFoodPoints)
            foodPoints = maxFoodPoints;
    }

    // TODO - register feeder blocks the same as harvesters
    public void addHarvester(BlockPos pBlockPos) {
        if(connectedHarvesters.add(pBlockPos)) {
            setChanged();
        }
    }

    public void removeHarvester(BlockPos pBlockPos) {
        if (connectedHarvesters.remove(pBlockPos)) {
            setChanged();
        }
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

        ListTag harvesterList = new ListTag();

        for(BlockPos pos : connectedHarvesters) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());

            harvesterList.add(posTag);
        }

        pTag.put("Harvesters", harvesterList);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        eatingProgress = pTag.getInt("pig_block.eating_progress");
        foodPoints = pTag.getInt("pig_block.food_points");
        maxFoodPoints = pTag.getInt("pig_block.max_food_points");

        connectedHarvesters.clear();

        ListTag harvesterList = pTag.getList("Harvesters", Tag.TAG_COMPOUND);

        for (int i = 0; i < harvesterList.size(); i++) {
            CompoundTag posTag = harvesterList.getCompound(i);

            BlockPos pos = new BlockPos(
                    posTag.getInt("x"),
                    posTag.getInt("y"),
                    posTag.getInt("z")
            );

            connectedHarvesters.add(pos);
        }
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
