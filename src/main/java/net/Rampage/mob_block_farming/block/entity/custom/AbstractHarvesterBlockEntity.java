package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.recipe.HarvesterRecipe;
import net.Rampage.mob_block_farming.recipe.HarvesterRecipeInput;
import net.Rampage.mob_block_farming.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractHarvesterBlockEntity extends BlockEntity implements MenuProvider {
    private BlockPos mobBlockPos;

    public AbstractHarvesterBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    protected final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected int speedMultiplier = 1;

    private int progressTimer = 0;
    private static final int BASE_TICK_INTERVAL = 60;

    public void resetProgress() {
        progressTimer = 0;
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
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

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("harvester_block.progress", progressTimer);

        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));

        if(mobBlockPos != null) {
            pTag.putLong("mob_block", mobBlockPos.asLong());
        }

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        progressTimer = pTag.getInt("harvester_block.progress");

        if(pTag.contains("mob_block")) {
            mobBlockPos = BlockPos.of(pTag.getLong("mob_block"));
        }
    }

    private Optional<RecipeHolder<HarvesterRecipe>> getCurrentRecipe(String mobBlockType) {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.HARVESTER_TYPE.get(),
                        new HarvesterRecipeInput(mobBlockType, getHarvesterType()), level);
    }

    public abstract String getHarvesterType();

    private boolean canRunHarvester(@Nullable AbstractMobBlockEntity mobBlock) {
        if (mobBlock == null) return false;

        Optional<RecipeHolder<HarvesterRecipe>> recipe = getCurrentRecipe(mobBlock.getMobBlockType());
        if(recipe.isEmpty())
            return false;

        if (mobBlock.getFoodPoints() < recipe.get().value().foodCost())
            return false;

        ItemStack output = recipe.get().value().result().copy();
        return canInsertItemIntoOutputSlot(output);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        ItemStack remainder = ItemHandlerHelper.insertItemStacked(itemHandler, output, true);

        return remainder.getCount() == 0;
    }

    public void tick(Level pLevel, BlockPos pBlockPos, BlockState pBlockState) {
        if (mobBlockPos == null) {
            resetProgress();
            return;
        }

        BlockEntity beFront = pLevel.getBlockEntity(mobBlockPos);

        if (beFront instanceof AbstractMobBlockEntity mobBlock) {
            if (!canRunHarvester(mobBlock)) {
                resetProgress();
                return;
            }

            progressTimer += speedMultiplier;
            setChanged(pLevel, pBlockPos, pBlockState);

            if (progressTimer >= BASE_TICK_INTERVAL) {
                harvestOutput(mobBlock);
                resetProgress();
            }
        }
        else {
            mobBlockPos = null;
        }
    }

    private void harvestOutput(AbstractMobBlockEntity mobBlock) {
        Optional<RecipeHolder<HarvesterRecipe>> recipe = getCurrentRecipe(mobBlock.getMobBlockType());
        ItemStack output = recipe.get().value().result().copy();

        boolean hasSubtracted = mobBlock.subtractFoodPoints(recipe.get().value().foodCost());

        if (!hasSubtracted)
            return;

        ItemHandlerHelper.insertItemStacked(itemHandler, output, false);
    }

    public void connectToMobBlock(AbstractMobBlockEntity mobBlock) {
        resetProgress();
        mobBlockPos = mobBlock.getBlockPos();
    }
}
