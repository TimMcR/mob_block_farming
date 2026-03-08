package net.Rampage.mob_block_farming.block.custom;

import com.mojang.serialization.MapCodec;
import net.Rampage.mob_block_farming.block.entity.ModBlockEntities;
import net.Rampage.mob_block_farming.block.entity.custom.BlenderBlockEntity;
import net.Rampage.mob_block_farming.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlenderBlock extends BaseEntityBlock {

    public static final MapCodec<BlenderBlock> CODEC = simpleCodec(BlenderBlock::new);

    public static final BooleanProperty RUNNING = BooleanProperty.create("running");

    public BlenderBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(RUNNING, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
         return new BlenderBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState,
                            boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof BlenderBlockEntity blenderBlockEntity) {
                blenderBlockEntity.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof BlenderBlockEntity blenderBlockEntity) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(blenderBlockEntity, Component.literal("Blender")), pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide())
            return null;

        return createTickerHelper(pBlockEntityType, ModBlockEntities.BLENDER_BE.get(),
                (level, blockPos, blockState, blenderBlockEntity) -> blenderBlockEntity.tick(level, blockPos, blockState));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if(!pState.getValue(RUNNING) || pRandom.nextInt(20) != 0)
            return;

        pLevel.playLocalSound(pPos, ModSounds.BLENDER_LOOP.get(), SoundSource.BLOCKS, 0.6f, 1.0f, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUNNING);
    }
}
