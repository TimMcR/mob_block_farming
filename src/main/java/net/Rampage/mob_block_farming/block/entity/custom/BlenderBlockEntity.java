package net.Rampage.mob_block_farming.block.entity.custom;

import net.Rampage.mob_block_farming.BlenderRecipeData;
import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.item.ModItems;
import net.Rampage.mob_block_farming.util.SlurryType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BlenderBlockEntity extends BlockEntity {
    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_RESULT = 1;

    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {

            if(slot != 0) return false;

            return BlenderRecipeData.get(stack.getItem()) != null;
        }
    };

    private int progress = 0;
    private final int maxProgress = 100;

    public BlenderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BLENDER_BE.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, BlenderBlockEntity be) {
        if(level.isClientSide) return;

        if(!level.hasNeighborSignal(blockPos)) {
            be.progress = 0;
            return;
        }

        ItemStack input = be.inventory.getStackInSlot(SLOT_INPUT);

        if(input.isEmpty()) return;

        var recipe = BlenderRecipeData.get(input.getItem());

        if(recipe == null) return;

        ItemStack output = be.inventory.getStackInSlot(SLOT_RESULT);

        Item slurryItem = recipe.type() == SlurryType.VEGAN
                ? ModItems.VEGAN_SLURRY.get()
                : ModItems.MEAT_SLURRY.get();

        if(!output.isEmpty() && output.getItem() != slurryItem) return;

        be.progress++;

        if(be.progress < be.maxProgress)
            return;

        input.shrink(1);

        if(output.isEmpty())
            be.inventory.setStackInSlot(SLOT_RESULT, new ItemStack(slurryItem, recipe.amount()));
        else
            output.grow(recipe.amount());

        be.progress = 0;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());

        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory", inventory.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
